package `in`.kyle.chess.extensions

import `in`.kyle.chess.ChessBoard
import `in`.kyle.chess.bitboard.bitboard
import `in`.kyle.chess.debug.PrettyBoard
import `in`.kyle.chess.model.*

val ChessBoard.currentBoard: IntArray
    get() = this.board[this.hmc]

val ChessBoard.currentColorOccupancy: LongArray
    get() = this.colorOccupancy[this.hmc]

val ChessBoard.currentPieceOccupancies: LongArray
    get() = this.pieceOcc[this.hmc]

val ChessBoard.currentOccupancy: Long
    get() = this.occupancy[this.hmc]

fun ChessBoard.pretty(): String = PrettyBoard.print(this)

operator fun ChessBoard.get(square: Square): Piece? {
    return this[square.index].takeIf { it != 0 }?.let { Piece.fromBits(it) }
}

operator fun ChessBoard.set(square: Square, piece: Piece?) {
    val previousPiece = this[square]
    this.clear(square.index, previousPiece?.color?.bits ?: 1)
    if (piece != null) {
        this[square.index] = piece.bits
    }
}

fun ChessBoard.getPieceAttacks(square: Square, accountForFriendlyPieces: Boolean = true): Long {
    val piece = this[square] ?: error("No piece at $square")
    val pieceBitboard = bitboard {
        add(square)
    }

    val friendlyPiecesMaskInv =
        if (accountForFriendlyPieces) currentColorOccupancy[color].inv() else 0UL.toLong().inv()

    return when (piece.pieceType) {
        PieceType.PAWN -> pawnAttack(square.index, piece.color.bits)
        PieceType.KNIGHT -> getAllKnightAttacks(pieceBitboard)
        PieceType.BISHOP -> getSingleBishopAttacks(square.index, friendlyPiecesMaskInv)
        PieceType.ROOK -> getSingleRookAttacks(square.index, friendlyPiecesMaskInv)
        PieceType.QUEEN -> getSingleRookAttacks(square.index, friendlyPiecesMaskInv) or
                getSingleBishopAttacks(square.index, friendlyPiecesMaskInv)
        PieceType.KING -> getKingAttacks(piece.bits)
    }
}

fun ChessBoard.getAttackedSquares(color: Color): Long {
    return this.getAttackedSquares(color.bits)
}

fun ChessBoard.getHumanCastleMoves(attackedSquares: Long): List<Move> {
    val list = mutableListOf<Move>()
    val consumer: (Int) -> Unit = { list.add(Move(it)) }
    this.getCastleMoves(attackedSquares, consumer)
    return list
}
