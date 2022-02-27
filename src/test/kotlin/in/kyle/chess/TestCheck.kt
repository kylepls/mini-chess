package `in`.kyle.chess

import `in`.kyle.chess.bitboard.toBitboardString
import `in`.kyle.chess.debug.Fen
import `in`.kyle.chess.extensions.pretty
import `in`.kyle.chess.model.Color
import `in`.kyle.chess.model.Piece
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class TestCheck : FreeSpec({

    "test checks on king" - {
        listOf(
            row(Piece.BLACK_PAWN, "8/1k6/8/8/8/8/3p4/4K3 w - - 1 2"),
            row(Piece.BLACK_KNIGHT, "8/1k6/8/8/8/5n2/8/4K3 w - - 1 2"),
            row(Piece.BLACK_BISHOP, "8/1k6/8/8/7b/8/8/4K3 w - - 1 2"),
            row(Piece.BLACK_ROOK, "8/1k2r3/8/8/8/8/8/4K3 w - - 1 2"),
            row(Piece.BLACK_QUEEN, "8/1k6/8/8/8/4q3/8/4K3 w - - 1 2"),
            row(Piece.WHITE_PAWN, "8/1k6/2P5/8/8/8/1K6/8 b - - 1 2"),
            row(Piece.WHITE_KNIGHT, "8/1k6/8/2N5/8/8/1K6/8 b - - 1 2"),
            row(Piece.WHITE_BISHOP, "8/1k6/8/3B4/8/8/1K6/8 b - - 1 2"),
            row(Piece.WHITE_ROOK, "8/1k4R1/8/8/8/8/1K6/8 b - - 1 2"),
            row(Piece.WHITE_QUEEN, "8/1k6/8/8/4Q3/8/1K6/8 b - - 1 2")
        ).map { (piece, fen) ->
            "check with $piece" {
                val board = Fen.toBoard(fen)
                val attacked = board.getAttackedSquares(piece.color.bits)
                withClue(buildString {
                    appendLine("Fen: $fen")
                    appendLine("Piece: $piece")
                    appendLine("Board:")
                    appendLine(board.pretty())
                    appendLine()
                    appendLine("Attacked:")
                    appendLine(attacked.toBitboardString())
                }) {
                    board.isCheck(piece.color.bits) shouldBe false
                    board.isCheck(piece.color.opposite().bits) shouldBe true
                }
            }
        }
    }
})
