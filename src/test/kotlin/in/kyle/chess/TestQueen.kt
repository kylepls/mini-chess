package `in`.kyle.chess

import `in`.kyle.chess.reference.testSameMovesAsReference
import io.kotest.core.spec.style.StringSpec

class TestQueen : StringSpec({

    "generates valid moves" {
        testSameMovesAsReference("8/1k6/8/4Q3/8/8/6K1/8 w - - 0 1")
    }

    "can be blocked vertically" {
        testSameMovesAsReference("8/1k6/8/8/8/8/K7/Q7 w - - 0 1")
    }

    "can be blocked diagonally" {
        testSameMovesAsReference("8/1k6/8/8/8/8/1K6/Q7 w - - 0 1")
    }

})
