package `in`.kyle.chess.bitboard

import `in`.kyle.chess.lsb64Table
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TestLsb64Table : StringSpec({
    "decoded table should be equal" {
        val decoded = lsb64Table
        val actual = Lsb64Table.lsb64Table
        decoded shouldBe actual
    }
})
