package `in`.kyle.chess

import `in`.kyle.chess.bitboard.bitboard
import `in`.kyle.chess.bitboard.shouldBeBitboard
import `in`.kyle.chess.model.Piece
import `in`.kyle.chess.model.File
import `in`.kyle.chess.model.Square
import `in`.kyle.chess.reference.testSameMovesAsReference
import `in`.kyle.chess.debug.Fen
import `in`.kyle.chess.extensions.currentColorOccupancy
import io.kotest.core.spec.style.StringSpec

class TestRook : StringSpec({

    "rook can move on open files" {
        testSameMovesAsReference("8/1k6/8/8/8/8/7K/R7 w - - 0 1") {
            focusPiece(Piece.WHITE_ROOK)
        }
    }

    "rook can attack other pieces" {
        testSameMovesAsReference("8/pk6/p7/8/8/8/6K1/R7 w - - 0 1")
    }

    "rook can be blocked" {
        testSameMovesAsReference("8/1k6/8/8/8/8/K7/R7 w - - 0 1")
    }

    "test multirook attacks" {

    }

    "rook attacks can be blocked" {
        val board = Fen.toBoard("8/1R4P1/8/8/8/8/1P4R1/4K3 w - - 0 1")
        val attacks = board.getSingleRookAttacks(Square.H1.index, board.currentColorOccupancy[0].inv())

        val expected = bitboard {
            add(Square.G1, Square.F1)
            addFile(File.H)
            remove(Square.H1)
        }

        attacks shouldBeBitboard expected
    }
})
