package `in`.kyle.chess.util

import `in`.kyle.chess.ChessBoard
import `in`.kyle.chess.Move
import `in`.kyle.chess.Move.Encoding.*

object San {
    // https://www.thechessdrum.net/PGN_Reference.txt
    fun format(
        move: Move,
        chessBoard: ChessBoard
    ): String {
        val castles = mapOf(KING_CASTLE to "0-0", QUEEN_CASTLE to "0-0-0")
        if (move.encoding in castles) {
            return castles[move.encoding]!!
        }

        val piece = move.piece

        var san = ""
        if (piece.lowercase() != "p") {
            san += piece
        }

        val from = Move.getChessNotation(move.from)

        val ambiguousMoves = chessBoard.getValidMoves()
            .filter { it.to == move.to && it.piece == piece && it.from != move.from }
            .filter { it != move }
            .toMutableList()

        if (ambiguousMoves.any { it.from.first == move.from.first }) {
            // try disambiguate file
            san += from[0]
        } else if (ambiguousMoves.any { it.from.second == move.from.second }) {
            // try disambiguate rank
            san += from[1]
        } else if (ambiguousMoves.isNotEmpty()) {
            san += from
        }

        if (move.encoding in arrayOf(
                CAPTURES,
                CAPTURES,
                BISHOP_PROMOTION_CAPTURE,
                ROOK_PROMOTION_CAPTURE,
                QUEEN_PROMOTION_CAPTURE,
                KNIGHT_PROMOTION_CAPTURE
            )
        ) {
            san += "x"
        }

        val to = Move.getChessNotation(move.to)
        if (move.encoding == EP_CAPTURE) {
            san += to[1]
        } else {
            san += to
        }

        if (move.encoding in arrayOf(
                BISHOP_PROMOTION,
                ROOK_PROMOTION,
                QUEEN_PROMOTION,
                KNIGHT_PROMOTION
            )
        ) {
            san += "=" + move.encoding.toString().toLowerCase()
        }

        val newBoard = chessBoard.copy().apply { makeMove(move) }

        if (newBoard.isCheckmate()) {
            san += "#"
        } else if (newBoard.isCheck()) {
            san += "+"
        }

        return san
    }
}
