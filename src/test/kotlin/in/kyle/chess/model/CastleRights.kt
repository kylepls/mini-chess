package `in`.kyle.chess.model

import `in`.kyle.chess.ChessBoard

enum class CastleRights(
    val canQueensideCastle: Boolean,
    val canKingsideCastle: Boolean,
    private val uncoloredBits: Int
) {
    NONE(false, false, 0),
    KING_SIDE(false, true, 0b1),
    QUEEN_SIDE(true, false, 0b10),
    KING_SIDE_AND_QUEEN_SIDE(true, true, 0b11);

    fun getBits(color: Color): Int {
        return if (color == Color.WHITE) uncoloredBits shl 2 else uncoloredBits
    }

    companion object {
        fun getCastleRights(bits: Int): Map<Color, CastleRights> {
            val whiteBits = bits and 3
            val blackBits = bits shr 2

            val find = { bits: Int -> values().find { it.uncoloredBits == bits }!! }

            return mapOf(
                Color.BLACK to find(blackBits),
                Color.WHITE to find(whiteBits)
            )
        }

        fun getCastleRights(board: ChessBoard) = getCastleRights(board.castling)
    }
}

fun ChessBoard.castleRights() = CastleRights.getCastleRights(this)
