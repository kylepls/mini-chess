package `in`.kyle.chess

import `in`.kyle.chess.reference.testSameMovesAsReference
import io.kotest.core.spec.style.FreeSpec

class TestBishop : FreeSpec({

    "test simple diagonals" {
        testSameMovesAsReference("8/1k6/8/4B3/8/8/6K1/8 w - - 0 1")
    }

    "bishop can capture" {
        testSameMovesAsReference("7n/1k4p1/8/8/8/8/6K1/B7 w - - 0 1")
    }

    "bishop can be blocked" {
        testSameMovesAsReference("8/1k6/8/8/8/8/1K6/B7 w - - 0 1")
    }
})
