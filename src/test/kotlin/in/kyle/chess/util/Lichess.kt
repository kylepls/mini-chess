package `in`.kyle.chess.util

object Lichess {
    fun analysisUrl(fen: String): String {
        return "https://lichess.org/analysis/standard/$fen"
    }
}
