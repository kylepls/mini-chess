package `in`.kyle.chess.debug

object MoveUtils {
    fun getSquare(square: String): Int {
        val rank = square[1].digitToInt() - 1
        val file = square[0].code - 97
        return rank * 8 + file
    }

    fun getChessNotation(square: Int): String {
        val rank = square / 8
        val file = square % 8
        return "${file.toChar() + 97}${rank + 1}"
    }
}
