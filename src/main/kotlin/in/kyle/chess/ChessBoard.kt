package `in`.kyle.chess

import `in`.kyle.chess.Move.Encoding.*
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class ChessBoard {

    var castling = "KQkq"
    var enPassant: Pair<Int, Int>? = null
    var halfMoveClock = 1
    var fullMoveNumber = 1
    var turn = 'w'

    private var board: Array<CharArray> = Array(8) { CharArray(8) { ' ' } }

    fun makeMove(move: Move) {
        set(move.from, ' ')
        set(move.to, move.piece)

        if (move.piece.equals('k', true)) {
            val str = if (move.piece == 'K') "KQ" else "kq"
            castling = castling.replace(str, "")
        }

        if (move.piece.equals('r', true)) {
            val lookup = when (move.from) {
                Pair(0, 0) -> "Q"
                Pair(0, 7) -> "K"
                Pair(7, 0) -> "q"
                Pair(7, 7) -> "k"
                else -> ""
            }
            castling = castling.replace(lookup, "")
        }

        enPassant = if (move.encoding == DOUBLE_PAWN_PUSH) {
            val direction = if (turn == 'w') 1 else -1
            Pair(move.from.first + direction, move.to.second)
        } else {
            null
        }

        if (move.encoding == EP_CAPTURE) {
            val direction = if (turn == 'w') -1 else 1
            board[move.to.first + direction][move.to.second] = ' '
        }

        if (move.encoding in arrayOf(KING_CASTLE, QUEEN_CASTLE)) {
            val direction = if (move.encoding == KING_CASTLE) 1 else -1
            val rookFile = if (move.encoding == KING_CASTLE) 7 else 0

            set(Pair(move.to.first, move.to.second + direction), copyCase(move.piece, 'R'))
            set(Pair(move.to.first, rookFile), ' ')
        }

        val promotions = mapOf(
            QUEEN_PROMOTION_CAPTURE to 'q',
            QUEEN_PROMOTION to 'q',
            ROOK_PROMOTION_CAPTURE to 'r',
            ROOK_PROMOTION to 'r',
            BISHOP_PROMOTION_CAPTURE to 'b',
            BISHOP_PROMOTION to 'b',
            KNIGHT_PROMOTION_CAPTURE to 'n',
            KNIGHT_PROMOTION to 'n'
        )
        if (move.encoding in promotions) {
            set(move.to, copyCase(move.piece, promotions[move.encoding]!!))
        }

        halfMoveClock++
        if (turn == 'b') {
            fullMoveNumber++
        }

        turn = if (turn == 'w') 'b' else 'w'
    }

    fun getValidMoves(color: Char = turn, simple: Boolean = false): List<Move> {
        val otherColor = if (color == 'w') 'b' else 'w'
        val attackedSquares = if (!simple) getAttackedSquares(otherColor) else emptySet()
        val flatMap = allSquares()
            .filter { getColor(it.first, it.second) == color }
            .flatMap { getPseudolegalMoves(it, simple, attackedSquares) }
        return flatMap
            .filter { simple || !copy().apply { makeMove(it) }.isCheck(color) }
    }

    private fun getKingMoves(loc: Pair<Int, Int>, piece: Char, attackedSquares: Set<Pair<Int, Int>>) =
        (-1..1).flatMap { i -> (-1..1).map { Pair(it, i) } }
            .filter { it != Pair(0, 0) }
            .map { Move(loc, loc.first + it.first to loc.second + it.second, piece) }
            .filter { it.to !in attackedSquares }

    private fun getKnightMoves(loc: Pair<Int, Int>, piece: Char) =
        listOf(1 to 2, 2 to 1, -1 to 2, -2 to 1, 1 to -2, 2 to -1, -1 to -2, -2 to -1)
            .map { Move(loc, loc.first + it.first to loc.second + it.second, piece) }

    private fun getPseudolegalMoves(
        loc: Pair<Int, Int>,
        simple: Boolean,
        attackedSquares: Set<Pair<Int, Int>>
    ): List<Move> {
        val (rank, file) = loc
        val piece = get(rank, file) ?: return emptyList()

        val solveDirectional = { axes: Int, add45: Boolean ->
            sequence {
                for (i in 0 until axes) {
                    val ff = if (add45) Math.PI * 2.0 / 8.0 else 0.0
                    val dx = cos(Math.PI * 2.0 / axes * i + ff).roundToInt()
                    val dy = sin(Math.PI * 2.0 / axes * i + ff).roundToInt()
                    var k = 1

                    while (true) {
                        val target = get(rank + dx * k, file + dy * k) ?: break
                        yield(Move(rank to file, rank + dx * k to file + dy * k, piece))
                        if (target != ' ') {
                            break
                        }
                        k++
                    }
                }
            }.toList()
        }

        val pawnMoves: (Int) -> List<Move> = { direction ->
            sequence {
                for (i in listOf(-1, 1)) {
                    get(rank + direction, file + i)
                        ?.takeIf { (it.isLetter() || simple) && getColor(it) != getColor(piece) }
                        ?.let { yield(Move(rank to file, rank + direction to file + i, piece)) }

                    val test = Pair(rank + direction, file + i)
                    if (test == enPassant) {
                        yield(Move(rank to file, test, piece, EP_CAPTURE))
                    }
                }
                if (simple) return@sequence
                get(rank + direction, file).takeIf { it == ' ' }?.let {
                    yield(Move(rank to file, rank + direction to file, piece))
                    val doubleMoveLoc = Pair(rank + direction * 2, file)
                    get(doubleMoveLoc)
                        .takeIf { it == ' ' && if (direction == 1) rank == 1 else rank == 6 }
                        ?.let { yield(Move(rank to file, doubleMoveLoc, piece, DOUBLE_PAWN_PUSH)) }
                }
            }.toList().flatMap { move ->
                val mapping = mapOf(
                    true to values().filter { it.name.endsWith("CAPTURE") },
                    false to values().filter { it.name.endsWith("PROMOTION") },
                )
                if (move.to.first in arrayOf(0, 7)) {
                    mapping[get(move.to) != ' ']!!
                        .map { Move(move.from, move.to, move.piece, it) }
                } else {
                    listOf(move)
                }
            }
        }

        val castle: () -> List<Move> = {
            val rookRank = if (piece == 'K') 0 else 7

            sequence {
                val clearSpots = arrayOf(
                    (if (piece == 'K') 'K' else 'k') to arrayOf(6, 5),
                    (if (piece == 'K') 'Q' else 'q') to arrayOf(2, 3, 1),
                )

                for ((key, spots) in clearSpots) {
                    if (key in castling && spots.all {
                            get(rookRank, it) == ' ' && Pair(rookRank, it) !in attackedSquares
                        }) {
                        val encoding = if (key.lowercase() == "q") QUEEN_CASTLE else KING_CASTLE
                        yield(Move(rank to file, rookRank to spots[0], piece, encoding))
                    }
                }
            }.toList()
        }

        val moves = when (piece) {
            'P' -> pawnMoves(1)
            'p' -> pawnMoves(-1)
            'r', 'R' -> solveDirectional(4, false)
            'n', 'N' -> getKnightMoves(loc, piece)
            'b', 'B' -> solveDirectional(4, true)
            'q', 'Q' -> solveDirectional(8, false)
            'k', 'K' -> getKingMoves(loc, piece, attackedSquares) + if (simple) emptyList() else castle()
            else -> emptyList()
        }.filter { getColor(it.piece) == getColor(piece) }
            .filter { getColor(it.to.first, it.to.second) != getColor(piece) && get(it.to) != null }

        return moves
    }

    fun getAttackedSquares(color: Char): Set<Pair<Int, Int>> {
        val opposingKing = if (color == 'w') 'k' else 'K'

        // temp remove the opponent king
        val kingSquare = allSquares().find { get(it) == opposingKing }
        kingSquare?.let { set(kingSquare, ' ') }

        val attackedSquares = getValidMoves(color, true).map { it.to }.toSet()

        kingSquare?.let { set(kingSquare, opposingKing) }
        return attackedSquares
    }

    fun isCheck(color: Char = turn): Boolean {
        val king = if (color == 'w') 'K' else 'k'
        val otherColor = if (color == 'w') 'b' else 'w'
        return getAttackedSquares(otherColor).any { get(it) == king }
    }

    fun isCheckmate(): Boolean {
        return isCheck() && getValidMoves(turn).isEmpty()
    }

    fun isStalemate(): Boolean {
        return !isCheck() && getValidMoves(turn).isEmpty()
    }

    fun isDraw(): Boolean {
        if (fullMoveNumber > 50) return true
        if (isStalemate()) return true
        // TODO: 3 move repetition
        // note, 3 move repetition can require 4 moves if castling privileges are lost during a move
        return false
    }

    override fun toString(): String {
        val sb = StringBuilder("\n")
        for (i in 7 downTo 0) {
            sb.append("${i + 1} ${board[i].joinToString(" ")} \n")
        }
        sb.append("  ${('a'..'h').joinToString(" ")}\n")
        return sb.toString()
    }

    private fun allSquares() = (0..7).flatMap { r -> (0..7).map { c -> Pair(r, c) } }

    private fun getColor(rank: Int, file: Int) = get(rank, file)?.let { getColor(it) }

    private fun getColor(piece: Char) =
        if (piece.isUpperCase()) 'w' else if (piece.isLowerCase()) 'b' else ' '

    operator fun get(rank: Int, file: Int) =
        if (rank in 0..7 && file in 0..7) board[rank][file] else null

    fun get(loc: Pair<Int, Int>) = get(loc.first, loc.second)

    operator fun set(rank: Int, file: Int, piece: Char) = set(Pair(rank, file), piece)

    fun set(loc: Pair<Int, Int>, piece: Char) {
        board[loc.first][loc.second] = piece
    }

    private fun copyCase(source: Char, target: Char): Char =
        if (source.isUpperCase()) target.toUpperCase() else target.toLowerCase()

    fun copy(): ChessBoard {
        val board = ChessBoard()
        board.board = this.board.map { it.map { it }.toCharArray() }.toTypedArray()
        board.turn = this.turn
        board.castling = this.castling
        board.enPassant = this.enPassant
        board.halfMoveClock = this.halfMoveClock
        board.fullMoveNumber = this.fullMoveNumber
        return board
    }
}
