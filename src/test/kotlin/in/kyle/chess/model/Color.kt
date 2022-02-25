package `in`.kyle.chess.model

import `in`.kyle.chess.ChessBoard

enum class Color {
    WHITE,
    BLACK;

    val bits: Int
        get() = ordinal

    fun opposite() = when (this) {
        WHITE -> BLACK
        BLACK -> WHITE
    }
}

val ChessBoard.humanColor: Color
    get() = if (this.color == 0) Color.WHITE else Color.BLACK
