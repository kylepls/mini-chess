package `in`.kyle.chess.chessboard

import `in`.kyle.chess.shift
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class TestGenShift : FreeSpec({
    "should shift left" {
        shift(1L, 3) shouldBe (1 shl 3)
    }

    "should shift right" {
        shift(0b10000, -3) shouldBe 0b00010
    }
})
