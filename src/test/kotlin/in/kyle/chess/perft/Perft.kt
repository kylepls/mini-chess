package `in`.kyle.chess.perft

object Perft {

    val PERFT_RESULTS = arrayOf(
        PerftResult(0, 0),
        PerftResult(1, 20),
        PerftResult(2, 400),
        PerftResult(3, 8902),
        PerftResult(4, 197281),
        PerftResult(5, 4865609),
        PerftResult(6, 119060324),
        PerftResult(7, 3195901860L),
        PerftResult(8, 84998978956L),
        PerftResult(9, 2439530234167L),
    )

    data class PerftResult(val depth: Int, val nodes: Long)
}
