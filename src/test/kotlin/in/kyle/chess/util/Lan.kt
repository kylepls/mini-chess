package `in`.kyle.chess.util

import `in`.kyle.chess.Move
import `in`.kyle.chess.Move.Encoding.*

object Lan {
    fun format(move: Move): String {
        val piece = if (!move.piece.equals('p', true)) move.piece.uppercase() else ""
        val capture = if (move.encoding.name.contains("CAPTURE")) "x" else ""
        val from = "${move.from.second.toChar() + 97}${move.from.first + 1}"
        val to = "${move.to.second.toChar() + 97}${move.to.first + 1}"
        return "$piece$from$capture$to${promotions(move.encoding)}"
    }

    private fun promotions(encoding: Move.Encoding): String {
        return when (encoding) {
            QUEEN_PROMOTION -> "Q"
            ROOK_PROMOTION -> "R"
            BISHOP_PROMOTION -> "B"
            KNIGHT_PROMOTION -> "N"
            QUEEN_PROMOTION_CAPTURE -> "Q"
            ROOK_PROMOTION_CAPTURE -> "R"
            BISHOP_PROMOTION_CAPTURE -> "B"
            KNIGHT_PROMOTION_CAPTURE -> "N"
            else -> ""
        }
    }
}
