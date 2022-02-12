package `in`.kyle.chess.util

import `in`.kyle.chess.ChessBoard
import `in`.kyle.chess.Move

object Fen {

    const val STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"

    fun toFen(board: ChessBoard): String {
        val pieces = (7 downTo 0).joinToString("/") { rank ->
            var str = ""
            var empty = 0
            for (file in 0..7) {
                if (board[rank, file] == ' ') {
                    empty++
                } else {
                    if (empty > 0) {
                        str += empty.toString()
                        empty = 0
                    }
                    str += board[rank, file]
                }
            }
            if (empty > 0) {
                str += empty.toString()
            }
            str
        }

        val castlingStr = board.castling.ifEmpty { "-" }
        val enPassant =
            board.enPassant?.let { Move.getChessNotation(it) } ?: "-"

        return "$pieces ${board.turn} $castlingStr $enPassant ${board.halfMoveClock} ${board.fullMoveNumber}"
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
                board[i, j] = c
                j++
            }
        }

        if (parts.size == 1) {
            return board
        }

        board.turn = parts[1][0]

        val castling = parts[2]
        board.castling = castling.filter { it.isLetter() }

        board.enPassant =
            if (parts[3] == "-") null else Pair(
                parts[3][1].toInt() - 49,
                parts[3][0].toInt() - 97
            )

        board.halfMoveClock = parts[4].toInt()
        board.fullMoveNumber = parts[5].toInt()
        return board
    }
}
