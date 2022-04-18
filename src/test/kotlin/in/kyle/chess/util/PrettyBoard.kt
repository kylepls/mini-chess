package `in`.kyle.chess.util

import `in`.kyle.chess.ChessBoard

object PrettyBoard {

    private const val ideographicSpace = "\u3000"
    private const val rankLabels = "０１２３４５６７８９"
    private const val fileLabels = "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ"

//    fun printUnicode(board: ChessBoard): String {
//        val prefixStr = "  "
//
//        var string = "$prefixStr┌───┬───┬───┬───┬───┬───┬───┬───┐\n"
//        for (rank in 7 downTo 0) {
//
//            string += "${rank + 1} "
//            val rankString = (0..7).map { file ->
//                val piece = board[rank * 8 + file]
//                if (piece == 0) ideographicSpace else Piece.fromBits(piece).prettyString
//            }.joinToString("│") { " $it " }
//
//            string += "│$rankString│\n"
//
//            if (rank != 0) string += "$prefixStr├───┼───┼───┼───┼───┼───┼───┼───┤\n"
//        }
//
//        string += "$prefixStr└───┴───┴───┴───┴───┴───┴───┴───┘\n"
//
//        string += "$prefixStr"
//        for (file in 0..7) {
//            string += "  ${fileLabels[file]}"
//        }
//        string += "\n"
//
//        return string
//    }

    private val pieceMap = mapOf(
        1 to "P",
        2 to "N",
        3 to "B",
        4 to "R",
        5 to "Q",
        6 to "K",
        7 to "p",
        8 to "n",
        9 to "b",
        10 to "r",
        11 to "q",
        12 to "k"
    )

    fun print(board: ChessBoard): String {
        return (7 downTo 0).joinToString("\n") { rank ->
            (rank+1).toString() + (0..7).joinToString("") { file ->
                val piece = board[rank * 8 + file]
                if (piece == 0) "." else pieceMap[piece]!!
            }
        } + "\n " + (0..7).joinToString("") { file -> ('A'+file).toString() }
    }

}
