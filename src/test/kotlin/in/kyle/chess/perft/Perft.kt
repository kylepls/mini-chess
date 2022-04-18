package `in`.kyle.chess.perft

import `in`.kyle.chess.util.Fen

// https://www.chessprogramming.org/Perft_Results
enum class Perft(val fen: String, val nodes: List<Long>) {

    INITIAL_POSITION(
        Fen.STARTING_POSITION, listOf(
            20,
            400,
            8902,
            197281,
            4865609,
            119060324,
            3195901860L,
            84998978956L,
            2439530234167L,
        )
    ),
    KIWI_PETE(
        "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -", listOf(
            48,
            2039,
            97862,
            4085603,
            193690690,
            8031647685L,
        )
    ),
    POSITION_3(
        "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -", listOf(
            14,
            191,
            2812,
            43238,
            674624,
            11030083,
            178633661,
            3009794393L,
        )
    ),
    POSITION_4(
        "r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1", listOf(
            6,
            264,
            9467,
            422333,
            15833292,
            706045033L,
        )
    ),
    POSITION_5(
        "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8", listOf(
            44,
            1486,
            62379,
            2103487,
            89941194,
            3894528066L,
        )
    ),
    POSITION_6(
        "r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10", listOf(
            46,
            2079,
            89890,
            3894594,
            164860436,
        )
    ),

}
