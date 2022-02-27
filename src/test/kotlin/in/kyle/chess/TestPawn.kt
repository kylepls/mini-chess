package `in`.kyle.chess

import `in`.kyle.chess.bitboard.bitboard
import `in`.kyle.chess.bitboard.shouldBeBitboard
import `in`.kyle.chess.model.Square
import `in`.kyle.chess.reference.testSameMovesAsReference
import `in`.kyle.chess.debug.Fen
import `in`.kyle.chess.model.Color
import io.kotest.core.spec.style.StringSpec

class TestPawn : StringSpec({

    "black - test single pawn attack mask" {
        val board = Fen.toBoard("8/8/8/4p3/8/8/8/8 w - - 0 1")
        val attacks = board.getPawnAttacks(Color.BLACK.bits)

        val expected = bitboard {
            add(Square.D4, Square.F4)
        }

        attacks shouldBeBitboard expected
    }

    "black - test single pawn attack mask in right corner" {
        val board = Fen.toBoard("7p/8/8/8/8/8/8/8 w - - 0 1")
        val attacks = board.getPawnAttacks(Color.BLACK.bits)

        val expected = bitboard {
            add(Square.G7)
        }

        attacks shouldBeBitboard expected
    }

    "black - test single pawn attack mask in left corner" {
        val board = Fen.toBoard("p7/8/8/8/8/8/8/8 w - - 0 1")
        val attacks = board.getPawnAttacks(Color.BLACK.bits)

        val expected = bitboard {
            add(Square.B7)
        }

        attacks shouldBeBitboard expected
    }

    "white - test single pawn attack mask" {
        val board = Fen.toBoard("8/8/8/8/8/2P5/8/8 w - - 0 1")
        val attacks = board.getPawnAttacks(Color.WHITE.bits)

        val expected = bitboard {
            add(Square.B4, Square.D4)
        }

        attacks shouldBeBitboard expected
    }

    "white - test single pawn attack mask in right corner" {
        val board = Fen.toBoard("8/8/8/8/8/8/8/7P w - - 0 1")
        val attacks = board.getPawnAttacks(Color.WHITE.bits)

        val expected = bitboard {
            add(Square.G2)
        }

        attacks shouldBeBitboard expected
    }

    "white - test single pawn attack mask in left corner" {
        val board = Fen.toBoard("8/8/8/8/8/8/8/P7 w - - 0 1")
        val attacks = board.getPawnAttacks(Color.WHITE.bits)

        val expected = bitboard {
            add(Square.B2)
        }

        attacks shouldBeBitboard expected
    }

    "test double pawn push on 2nd rank" {
        testSameMovesAsReference("8/1k6/8/8/8/8/1P4K1/8 w - - 0 1")
    }

    "test single push" {
        testSameMovesAsReference("8/1k6/8/8/8/1P6/6K1/8 w - - 0 1")
    }

    "test black double pawn push on 7th rank" {
        testSameMovesAsReference("8/1k4p1/8/8/8/8/6K1/8 b - - 0 1")
    }

    "test pawn captures left" {
        testSameMovesAsReference("8/1k6/8/8/1p6/2P5/6K1/8 w - - 0 1")
    }

    "test pawn captures right" {
        testSameMovesAsReference("8/1k6/8/8/3p4/2P5/6K1/8 w - - 0 1")
    }

    "test en passant capture right" {
        testSameMovesAsReference("8/1k6/8/5Pp1/8/8/6K1/8 w - g6 0 1")
    }

    "test en passant capture left" {
        testSameMovesAsReference("8/1k6/8/4pP2/8/8/6K1/8 w - e6 0 1")
    }

    "test can promote" {
        testSameMovesAsReference("8/1k4P1/8/8/8/8/5K2/8 w - - 0 1")
    }

    "test pawn cannot capture forwards" {
        testSameMovesAsReference("8/1k6/8/4p3/4P3/8/2K5/8 w - - 0 1")
    }

    "test pawn cannot attack king forwards" {
        testSameMovesAsReference("8/1k6/8/4p3/8/4K3/8/8 w - - 0 1")
    }
})
