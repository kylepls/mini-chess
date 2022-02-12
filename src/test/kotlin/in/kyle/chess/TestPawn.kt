package `in`.kyle.chess

import `in`.kyle.chess.reference.testSameMovesAsReference
import io.kotest.core.spec.style.StringSpec

class TestPawn : StringSpec({

    "test pawn moves forward 1 square" {
        testSameMovesAsReference("8/1k6/8/8/8/1P6/6K1/8 w - - 0 1")
    }

    "test double pawn push on 2nd rank" {
        testSameMovesAsReference("8/1k6/8/8/8/8/1P4K1/8 w - - 0 1")
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

    "test pawn cannot capture if pinned" {
        testSameMovesAsReference("8/1kr5/8/8/3p4/2P5/2K5/8 w - - 0 1")
    }

    "test en passant capture right" {
        testSameMovesAsReference("8/1k6/8/5Pp1/8/8/6K1/8 w - g6 0 1")
    }

    "test en passant capture left" {
        testSameMovesAsReference("8/1k6/8/4pP2/8/8/6K1/8 w - e6 0 1")
    }

    "test cannot en passant if pinned" {
        testSameMovesAsReference("5r2/1k6/8/4pP2/8/8/5K2/8 w - e6 0 1")
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
