package `in`.kyle.chess.bitboard

import `in`.kyle.chess.LSB64
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TestLsb64Table : StringSpec({
    "decoded table should be equal" {
        val decoded = LSB64
        val actual = Lsb64Table.lsb64Table
        decoded shouldBe actual
    }
})
