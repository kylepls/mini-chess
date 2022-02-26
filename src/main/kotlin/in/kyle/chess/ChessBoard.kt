package `in`.kyle.chess

import java.util.*

const val MASK_FROM = 0x3F
const val MASK_TO = MASK_FROM shl 6
const val MASK_PIECE = 0b1111 shl 12
const val MASK_ENCODING = 0b1111 shl 16
const val notAFile = 0xfefefefefefefefeUL
const val notHFile = 0x7f7f7f7f7f7f7f7fUL
const val MAX_MOVES = 256

// https://www.chessprogramming.org/Efficient_Generation_of_Sliding_Piece_Attacks#Squares_and_Bitindex
val diagonalMasks8 = ulongArrayOf(
    0x8040201008040201UL, 0x4020100804020100UL, 0x2010080402010000UL, 0x1008040201000000UL,
    0x804020100000000UL, 0x402010000000000UL, 0x201000000000000UL, 0x100000000000000UL, 0x0UL,
    0x80UL, 0x8040UL, 0x804020UL, 0x80402010UL, 0x8040201008UL, 0x804020100804UL, 0x80402010080402UL
)
val antiDiagonalMasks8 = ulongArrayOf(
    0x102040810204080UL, 0x1020408102040UL, 0x10204081020UL, 0x102040810UL, 0x1020408UL,
    0x10204UL, 0x102UL, 0x1UL, 0x0UL, 0x8000000000000000UL, 0x4080000000000000UL,
    0x2040800000000000UL, 0x1020408000000000UL, 0x810204080000000UL, 0x408102040800000UL,
    0x204081020408000UL
)
val onePawnAttacks = ulongArrayOf(0x2UL, 0x5UL, 0xaUL, 0x14UL, 0x28UL, 0x50UL, 0xa0UL, 0x40UL)
val lsb64Table = Base64.getDecoder()
    .decode("Px4DIDsOCyE8GDIJNxMVIj0dAjUzFykSOBwBKy4bACM+HzoEBTE2Bg80DCgHKi0QGTkwDQonCCwULyYWESUkGg==")
    .map { it.toInt() }.toIntArray()

@OptIn(ExperimentalUnsignedTypes::class)
class ChessBoard {

    var castling = 0
    var enPassant = 0
    var halfMoveClock = 0
    var fullMoveNumber = 1
    var color = 0

    var board = IntArray(64)
    var occupancy: ULong = 0UL
    var colorOccupancy = ULongArray(2)
    var pieceOccupancies = ULongArray(13)
    var empty = 0UL

    private val previousOccupancy = ULongArray(MAX_MOVES)
    private val previousBoard = Array(MAX_MOVES) { IntArray(64) }
    private val previousColorOccupancy = Array(MAX_MOVES) { ULongArray(2) }
    private val previousPieceOccupancies = Array(MAX_MOVES) { ULongArray(13) }
    private val previousCastling = IntArray(MAX_MOVES)
    private val previousEnPassant = IntArray(MAX_MOVES)
    val moves = IntArray(MAX_MOVES) { 0 }

    fun makeMove(move: Int) {
        previousCastling[halfMoveClock] = castling
        previousEnPassant[halfMoveClock] = enPassant
//        System.arraycopy(board, 0, previousBoard[halfMoveClock], 0, 64)
        previousBoard[halfMoveClock] = board.copyOf()
        previousOccupancy[halfMoveClock] = occupancy
//        System.arraycopy(colorOccupancy, 0, previousColorOccupancy[halfMoveClock], 0, 2)
        previousColorOccupancy[halfMoveClock] = colorOccupancy.copyOf()
//        System.arraycopy(pieceOccupancies, 0, previousPieceOccupancies[halfMoveClock], 0, 13)
        previousPieceOccupancies[halfMoveClock] = pieceOccupancies.copyOf()
        moves[halfMoveClock] = move

        val from = move and MASK_FROM
        val to = (move and MASK_TO) shr 6
        val encoding = (move and MASK_ENCODING) shr 16

        set(from, 0)
        enPassant = if (encoding == 1) to + (from - to) / 2 else 0
        if (encoding == 2 || encoding == 3) {
            castling = castling xor (1 shl ((encoding - 2) + (color * 2)))
        }
        if (encoding == 5) set(to - (8 * ((color xor 1) * 2 - 1)), 0)
        if (encoding < 6) set(to, (move and MASK_PIECE) shr 12)
        if (encoding >= 6) set(to, ((encoding - 6) % 4 + 2) + (color * 6))

        if (encoding == 2) {
            set(7 + color * 56, 0)
            set(5 + color * 56, 4 + color * 6)
        } else if (encoding == 3) {
            set(color * 56, 0)
            set(3 + color * 56, 4 + color * 6)
        }

        fullMoveNumber += ++halfMoveClock % 2
        color = color xor 1
    }

    fun undoMove() {
        castling = previousCastling[halfMoveClock - 1]
        enPassant = previousEnPassant[halfMoveClock - 1]
        board = previousBoard[halfMoveClock - 1]
        occupancy = previousOccupancy[halfMoveClock - 1]
        colorOccupancy = previousColorOccupancy[halfMoveClock - 1]
        pieceOccupancies = previousPieceOccupancies[halfMoveClock - 1]
        empty = occupancy.inv()
        color = color xor 1
        if (halfMoveClock-- % 2 != 0) fullMoveNumber--
    }

    fun getSingleRookAttacks(square: Int, friendlyOccupancyInv: ULong): ULong {
        val fileMask = 72340172838076673UL shl (square % 8)
        val rankMask = 255UL shl ((square / 8) * 8)
        return O2ON(square, fileMask, rankMask, friendlyOccupancyInv)
    }

    fun getSingleBishopAttacks(square: Int, friendlyOccupancyInv: ULong): ULong {
        val m1 = diagonalMasks8[((square / 8) - (square % 8)) and 15]
        val m2 = antiDiagonalMasks8[((square / 8) + (square % 8)) xor 7]
        return O2ON(square, m1, m2, friendlyOccupancyInv)
    }

    fun O2ON(square: Int, m1: ULong, m2: ULong, friendlyOccupancyInv: ULong): ULong {
        val pieceBitboard = 1UL shl square
        return (((((occupancy and m1) - (2UL * pieceBitboard)) xor (((occupancy and m1).reverse() - (2UL * pieceBitboard.reverse())).reverse())) and m1) or ((((occupancy and m2) - (2UL * pieceBitboard)) xor (((occupancy and m2).reverse() - (2UL * pieceBitboard.reverse())).reverse())) and m2)) and friendlyOccupancyInv
    }

    fun getKingAttacks(piece: Int): ULong {
        val pieceBitboard = pieceOccupancies[piece]
        var attacks =
            ((pieceBitboard shl 1) and notAFile) or ((pieceBitboard shr 1) and notHFile) or pieceBitboard
        attacks = attacks or (attacks shl 8) or (attacks shr 8)
        attacks = attacks xor pieceBitboard
        return attacks
    }

    fun getAllKnightAttacks(knights: ULong): ULong {
        val l1 = (knights shr 1) and 0x7F7F7F7F7F7F7F7FUL
        val l2 = (knights shr 2) and 0x3F3F3F3F3F3F3F3FUL
        val r1 = (knights shl 1) and 0xFEFEFEFEFEFEFEFEUL
        val r2 = (knights shl 2) and 0xFCFCFCFCFCFCFCFCUL
        val h1 = l1 or r1
        val h2 = l2 or r2
        return (h1 shl 16) or (h1 shr 16) or (h2 shl 8) or (h2 shr 8)
    }

    fun getCastleMoves(attackedSquares: ULong): List<Int> {
        val kingSafetyMasks =
            ulongArrayOf(0x70UL, 0x1cUL, 0x7000000000000000UL, 0x1c00000000000000UL)
        val occupancyMask = ulongArrayOf(0x60UL, 0xeUL, 0x6000000000000000UL, 0xe00000000000000UL)
        val castleMoves = intArrayOf(156036, 221316, 184252, 249532)
        val castleTests = intArrayOf(1, 2, 4, 8)
        val moves = ArrayList<Int>(2)

        for (i in 0..1) {
            if (((castleTests[color * 2 + i] and castling) != 0) and (attackedSquares and kingSafetyMasks[color * 2 + i] == 0UL) and (occupancy and occupancyMask[color * 2 + i] == 0UL)) {
                moves.add(castleMoves[color * 2 + i])
            }
        }
        return moves
    }

    fun getSinglePawnAttacks(square: Int): ULong {
        val temp = (onePawnAttacks[square % 8] shl ((square / 8) * 8))
        return if (this[square] > 6) (temp shr 8) else (temp shl 8)
    }

    inline fun wPawnAttacks() =
        (((pieceOccupancies[1] shl 9) and notAFile) or ((pieceOccupancies[1] shl 7) and notHFile) or epMask())

    inline fun bPawnAttacks() =
        (((pieceOccupancies[7] shr 9) and notHFile) or ((pieceOccupancies[7] shr 7) and notAFile) or epMask())

    inline fun epMask() = if (enPassant != 0) 1UL shl enPassant else 0UL

    inline fun wSinglePawnPushes() = ((pieceOccupancies[1] shl 8) and empty) shr 8
    inline fun bSinglePawnPushes() = ((pieceOccupancies[7] shr 8) and empty) shl 8

    inline fun wDoublePawnPushes() =
        ((pieceOccupancies[1] shl 16) and (wSinglePawnPushes() shl 16) and empty and 0xFF000000UL) shr 16

    inline fun bDoublePawnPushes() =
        ((pieceOccupancies[7] shr 16) and (bSinglePawnPushes() shr 16) and empty and 0xFF00000000UL) shl 16

    fun getPawnMoves(): List<Int> {
        val moves = mutableListOf<Int>()
        val maskSinglePush = if (color == 0) wSinglePawnPushes() else bSinglePawnPushes()
        val maskDoublePush = if (color == 0) wDoublePawnPushes() else bDoublePawnPushes()
        val opposingOccupancy = colorOccupancy[color xor 1]
        var maskPawns = pieceOccupancies[1 + 6 * color]
        while (maskPawns != 0UL) {
            val square = bitscanForward(maskPawns)
            val pieceMask = (1UL shl square)
            maskPawns = maskPawns and pieceMask.inv()
            if (maskDoublePush and pieceMask != 0UL) {
                moves.add(newMove(square, square + 16 - (32 * color), 1 + 6 * color, 1))
            }
            if (maskSinglePush and pieceMask != 0UL) {
                val to = square + 8 - (16 * color)
                if (to < 8 || to > 55) {
                    moves.addAll((6..9).map { newMove(square, to, 1 + 6 * color, it) })
                } else {
                    moves.add(newMove(square, to, 1 + 6 * color, 0))
                }
            }
            var pawnAttackMask = getSinglePawnAttacks(square) and (opposingOccupancy or epMask())
            while (pawnAttackMask != 0UL) {
                val to = bitscanForward(pawnAttackMask)
                pawnAttackMask = pawnAttackMask and (1UL shl to).inv()
                if (to < 8 || to > 55) {
                    moves.addAll((10..13).map { newMove(square, to, 1 + 6 * color, it) })
                } else {
                    val encoding = if (to == enPassant) 5 else 4
                    moves.add(newMove(square, to, 1 + 6 * color, encoding))
                }
            }
        }
        return moves
    }

    fun isCheck(color: Int) =
        (getAttackedSquares(color xor 1) and pieceOccupancies[6 + 6 * color]) != 0UL

    fun getAttackedSquares(color: Int): ULong {
        var attacks = 0UL
        val otherKing = pieceOccupancies[6 + (color xor 1) * 6]
        occupancy = occupancy xor otherKing // remove the king for calculation
        attacks = attacks or (if (color == 0) wPawnAttacks() else bPawnAttacks())
        attacks = attacks or getAllKnightAttacks(pieceOccupancies[2 + color * 6])
        for (i in 3..5) {
            val piece = i + color * 6
            var pieceBitboard = pieceOccupancies[piece]

            while (pieceBitboard != 0UL) {
                val square = bitscanForward(pieceBitboard)
                val pieceMask = (1UL shl square)
                pieceBitboard = pieceBitboard and pieceMask.inv()

                attacks = attacks or when (piece) {
                    3, 9 -> getSingleBishopAttacks(square, 0UL.inv())
                    4, 10 -> getSingleRookAttacks(square, 0UL.inv())
                    5, 11 -> getSingleBishopAttacks(square, 0UL.inv()) or
                            getSingleRookAttacks(square, 0UL.inv())
                    else -> throw IllegalStateException("piece $piece is not a supported")
                }
            }
        }

        attacks = attacks or getKingAttacks(6 + color * 6)
        occupancy = occupancy or otherKing
        return attacks
    }

    fun getMoves(): List<Int> {
        val moves = mutableListOf<Int>()
        val opposingOccupancy = colorOccupancy[color xor 1]
        val friendlyMaskInv = colorOccupancy[color].inv()
        val attackedSquares = getAttackedSquares(color xor 1)

        val pieceRange = if (color == 0) 2..6 else 8..12
        for (piece in pieceRange) {
            var pieceBitboard = pieceOccupancies[piece]

            while (pieceBitboard != 0UL) {
                val square = bitscanForward(pieceBitboard)
                val pieceMask = (1UL shl square)
                pieceBitboard = pieceBitboard and pieceMask.inv()

                val bitboardMoves = when (piece) {
                    2, 8 -> getAllKnightAttacks(pieceMask)
                    3, 9 -> getSingleBishopAttacks(square, friendlyMaskInv)
                    4, 10 -> getSingleRookAttacks(square, friendlyMaskInv)
                    5, 11 -> getSingleBishopAttacks(square, friendlyMaskInv) or
                            getSingleRookAttacks(square, friendlyMaskInv)
                    6, 12 -> getKingAttacks(piece) and attackedSquares.inv()
                    else -> throw IllegalStateException("piece $piece is not a supported")
                } and friendlyMaskInv

                var movesBitboard = bitboardMoves
                val capturesMoves = bitboardMoves and opposingOccupancy
                while (movesBitboard != 0UL) {
                    val destination = bitscanForward(movesBitboard)
                    val destinationMask = 1UL shl destination
                    movesBitboard = movesBitboard and destinationMask.inv()
                    val encoding = if ((capturesMoves and destinationMask) == 0UL) 0 else 4
                    moves.add(newMove(square, destination, piece, encoding))
                }
            }
        }

        moves.addAll(getPawnMoves())

        moves.removeIf {
            makeMove(it)
            val check = isCheck(color xor 1)
            undoMove()
            check
        }

        moves.addAll(getCastleMoves(attackedSquares))

        return moves
    }

    operator fun set(square: Int, piece: Int) {
        val oldPiece = board[square]
        val inv = (1UL shl square).inv()
        if (piece != 0) {
            pieceOccupancies[piece] = pieceOccupancies[piece] or (1UL shl square)
            occupancy = occupancy or (1UL shl square)
            val color = if (piece <= 6) 0 else 1
            colorOccupancy[color] = colorOccupancy[color] or (1UL shl square)
            colorOccupancy[color xor 1] = colorOccupancy[color xor 1] and inv
        } else {
            occupancy = occupancy and inv
            colorOccupancy[0] = colorOccupancy[0] and inv
            colorOccupancy[1] = colorOccupancy[1] and inv
        }
        pieceOccupancies[oldPiece] = pieceOccupancies[oldPiece] and inv
        empty = occupancy.inv()
        board[square] = piece
    }

    operator fun get(square: Int) = board[square]
}

fun ULong.reverse() = java.lang.Long.reverse(this.toLong()).toULong()

fun bitscanForward(input: ULong): Int {
    val bb = input xor (input - 1U)
    return lsb64Table[((bb xor (bb shr 32)).toUInt() * 0x78291ACFU shr 26).toInt()]
}

fun newMove(from: Int, to: Int, piece: Int, encoding: Int) =
    (from and MASK_FROM) or ((to and MASK_FROM) shl 6) or ((piece and 0xF) shl 12) or ((encoding and 0xF) shl 16)
