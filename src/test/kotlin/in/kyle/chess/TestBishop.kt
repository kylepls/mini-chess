package `in`.kyle.chess

import `in`.kyle.chess.reference.testSameMovesAsReference
import `in`.kyle.chess.util.Fen
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestBishop : StringSpec({

    "bishop has correct moves" {
        testSameMovesAsReference("8/1k6/8/8/8/8/6K1/B7 w - - 0 1")
    }

    "bishop can capture" {
        testSameMovesAsReference("7r/1k4p1/8/8/8/8/6K1/B7 w - - 0 1")
    }

    "bishop can be blocked" {
        testSameMovesAsReference("8/1k6/8/8/8/8/1K6/B7 w - - 0 1")
    }
})
