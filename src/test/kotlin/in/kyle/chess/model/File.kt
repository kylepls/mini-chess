package `in`.kyle.chess.model

enum class File {
    A, B, C, D, E, F, G, H;

    val squares: List<Square>
        get() = Square.values().filter { it.file == this.ordinal }
}
