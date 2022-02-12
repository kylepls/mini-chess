package `in`.kyle.chess

import `in`.kyle.chess.reference.testSameMovesAsReference
import io.kotest.core.spec.style.StringSpec

class TestKnight : StringSpec({

    "generates all 8 moves" {
        testSameMovesAsReference("8/1k6/8/4N3/8/8/1K6/8 w - - 0 1")
    }

    "generates 2 moves in a corner square" {
        testSameMovesAsReference("8/1k6/8/8/8/8/1K6/7N w - - 0 1")
    }

    "can be blocked" {
        testSameMovesAsReference("8/1k6/8/8/8/6K1/8/7N w - - 0 1")
    }
})
