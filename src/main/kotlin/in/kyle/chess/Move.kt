package `in`.kyle.chess

// a chess move
data class Move(
    val from: Pair<Int, Int>,
    val to: Pair<Int, Int>,
    val piece: Char,
    val encoding: Encoding = Encoding.QUIET_MOVE
) {
    private val promotions = Encoding.values().filter { "PROMOTION" in it.name }.associateWith {
        when(it.name.split("_")[0]) {
            "QUEEN" -> "q"
            "ROOK" -> "r"
            "BISHOP" -> "b"
            "KNIGHT" -> "n"
            else -> Error()
        }
    }

    override fun toString(): String {
        val from = getChessNotation(from)
        val to = getChessNotation(to)
        val promotion = promotions[encoding] ?: ""
        return "$from$to$promotion"
    }

    companion object {
        fun getChessNotation(loc: Pair<Int, Int>) =
            "${loc.second.toChar() + 97}${loc.first.toChar() + 49}"
    }

    // https://www.chessprogramming.org/Encoding_Moves
    enum class Encoding {
        QUIET_MOVE,
        DOUBLE_PAWN_PUSH,
        KING_CASTLE,
        QUEEN_CASTLE,
        CAPTURES,
        EP_CAPTURE,
        KNIGHT_PROMOTION,
        BISHOP_PROMOTION,
        ROOK_PROMOTION,
        QUEEN_PROMOTION,
        KNIGHT_PROMOTION_CAPTURE,
        BISHOP_PROMOTION_CAPTURE,
        ROOK_PROMOTION_CAPTURE,
        QUEEN_PROMOTION_CAPTURE
    }
}
