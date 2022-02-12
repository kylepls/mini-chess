package `in`.kyle.chess.reference

import io.kotest.core.spec.style.StringSpec

class TestReferenceCastles : StringSpec({
    "kingside castle should be the same" {
        testSameMovesAsReference("k7/8/8/8/8/8/8/4K2R w K - 0 1")
    }

    "queenside castle should be the same" {
        testSameMovesAsReference("7k/8/8/8/8/8/8/R3K3 w Q - 0 1")
    }
})
