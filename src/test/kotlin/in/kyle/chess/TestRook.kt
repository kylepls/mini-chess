package `in`.kyle.chess

import `in`.kyle.chess.reference.testSameMovesAsReference
import io.kotest.core.spec.style.StringSpec

class TestRook : StringSpec({

    "rook can move on open files" {
        testSameMovesAsReference("8/1k6/8/8/8/8/7K/R7 w - - 0 1")
    }

    "rook can attack other pieces" {
        testSameMovesAsReference("8/pk6/p7/8/8/8/6K1/R7 w - - 0 1")
    }

    "rook can be blocked" {
        testSameMovesAsReference("8/1k6/8/8/8/8/K7/R7 w - - 0 1")
    }
})
