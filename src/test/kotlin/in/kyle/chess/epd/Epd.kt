package `in`.kyle.chess.epd

data class Epd(
    val fen: String,
    val operations: List<Operation>
) {

    operator fun get(key: OperationKey) = operations.first { it.key == key }.value

    companion object {
        // https://www.chessprogramming.org/Extended_Position_Description
        fun readEpd(file: String): List<Epd> {
            val resourceAsStream =
                javaClass.getResourceAsStream("/$file")
                    ?: throw IllegalArgumentException("File not found")
            val epdText = resourceAsStream.bufferedReader().use { it.readText() }
            return epdText.lines().filter { it.isNotBlank() }.map {
                Epd(toFen(it), Operation.parse(it))
            }
        }

        private fun toFen(epd: String): String {
            val split = epd.split(" ")
            val pieces = split[0]
            val toMove = split[1]
            val castling = split[2]
            val enPassant = split[3]
            return "$pieces $toMove $castling $enPassant 1 1"
        }
    }

    enum class OperationKey(val prefix: String) {
        ANALYSIS_COUNT_DEPTH("adc"),
        ANALYSIS_COUNT_NODES("acn"),
        ANALYSIS_COUNT_SECONDS("acs"),
        AVOID_MOVES("am"),
        BEST_MOVE("bm"),
        CENTIPAWN_EVALUATION("ce"),
        DIRECT_MATE_MOVES("dm"),
        DRAW_ACCEPT("da"),
        DRAW_CLAIM("dc"),
        DRAW_OFFER("do"),
        DRAW_REJECT("dr"),
        ID("id"),
        COMMENT("c"),
        ;

        companion object {
        }
    }
}
