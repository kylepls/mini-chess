package `in`.kyle.chess.model

enum class Rank {
    RANK_1,
    RANK_2,
    RANK_3,
    RANK_4,
    RANK_5,
    RANK_6,
    RANK_7,
    RANK_8;

    val squares: List<Square>
        get() = Square.values().filter { it.rank == this.ordinal }

}
