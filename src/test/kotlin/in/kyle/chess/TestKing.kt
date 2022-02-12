package `in`.kyle.chess

import `in`.kyle.chess.reference.testSameMovesAsReference
import io.kotest.core.spec.style.StringSpec

class TestKing : StringSpec({

    "test generates all 8 king moves" {
        testSameMovesAsReference("8/1k6/8/8/4K3/8/8/8 w - - 0 1")
    }

    "test king must move out of check" {
        testSameMovesAsReference("r7/8/8/8/8/1k6/8/K7 w - - 0 1")
    }

    "test king must move out of check - xray" {
        testSameMovesAsReference("r7/8/8/8/1k6/8/K7/8 w - - 0 1")
    }

    "white can castle kingside" {
        testSameMovesAsReference("8/1k6/8/8/8/8/8/4K2R w K - 0 1")
    }

    "white cannot castle kingside" {
        testSameMovesAsReference("8/1k6/8/8/8/8/8/4K2R w - - 0 1")
    }

    "cannot castle through check" {
        testSameMovesAsReference("5r2/1k6/8/8/8/8/8/4K2R w K - 0 1")
    }

    "white can castle queenside" {
        testSameMovesAsReference("8/1k6/8/8/8/8/8/R3K3 w Q - 0 1")
    }

    "white must block checks" {
        testSameMovesAsReference("8/1k2r3/8/8/8/7R/3P1P2/3RKR2 w KQ - 0 1")
    }

    "white cannot move pinned piece" {
        testSameMovesAsReference("8/1k6/8/8/7b/8/5R2/4K3 w - - 0 1")
    }

    "white cannot move into check" {
        testSameMovesAsReference("8/8/8/8/8/4k3/8/4K3 w - - 0 1")
    }

    "white is checkmated" {
        testSameMovesAsReference("8/8/8/8/8/4k3/8/r3K3 w - - 0 1")
    }

    "must promote to block check" {
        testSameMovesAsReference("r2K4/2P5/3k4/8/8/8/8/8 w - - 0 1")
    }
})
