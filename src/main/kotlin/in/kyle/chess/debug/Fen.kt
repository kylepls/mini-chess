package `in`.kyle.chess.debug

import `in`.kyle.chess.ChessBoard

object Fen {

    const val STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"

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

    fun format(board: ChessBoard): String {
        val pieces = (7 downTo 0).joinToString("/") { rank ->
            var str = ""
            var empty = 0
            for (file in 0..7) {
                val square = rank * 8 + file
                if (board[square] == 0) {
                    empty++
                } else {
                    if (empty > 0) {
                        str += empty.toString()
                        empty = 0
                    }
                    str += pieceMap[(board[square])]
                }
            }
            if (empty > 0) {
                str += empty.toString()
            }
            str
        }

        val color = if (board.color == 0) "w" else "b"

        val castlingStr = if (board.castling == 0) "-" else {
            var str = ""
            if (board.castling and 1 != 0) str += "K"
            if (board.castling and 2 != 0) str += "Q"
            if (board.castling and 4 != 0) str += "k"
            if (board.castling and 8 != 0) str += "q"
            str
        }

        val enPassant =
            if (board.enPassant == 0) "-" else MoveUtils.getChessNotation(board.enPassant)

        return "$pieces $color $castlingStr $enPassant ${board.halfMoveClock} ${board.fullMoveNumber}"
    }

    fun toBoard(fen: String): ChessBoard {
        val board = ChessBoard()

        var i = 7
        var j = 0
        val parts = fen.split(" ")

        for (c in parts[0]) {
            if (c == '/') {
                i--
                j = 0
            } else if (c.isDigit()) {
                j += c.toString().toInt()
            } else {
                board[i * 8 + j] = pieceMap.entries.first { it.value == c.toString() }.key
                j++
            }
        }

        if (parts.size == 1) {
            return board
        }

        board.color = if (parts[1][0] == 'w') 0 else 1

        val castling = parts[2]

        board.castling = if (castling == "-") 0 else {
            var bits = 0
            if (castling.contains("K")) bits += 1
            if (castling.contains("Q")) bits += 2
            if (castling.contains("k")) bits += 4
            if (castling.contains("q")) bits += 8
            bits
        }

        board.enPassant = if (parts[3] == "-") 0 else MoveUtils.getSquare(parts[3])

        board.halfMoveClock = parts[4].toInt()
        board.fullMoveNumber = parts[5].toInt()
        return board
    }
}
