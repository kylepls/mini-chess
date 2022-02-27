package `in`.kyle.chess.bitboard

import `in`.kyle.chess.bitscanForward
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly

class TestBitboard : StringSpec({
    "bitboard scans should contain next index" {
        val testValue = 0b1000UL.toLong()
        val testScan = bitscanForward(testValue)
        testScan shouldBeExactly 3
    }
})
