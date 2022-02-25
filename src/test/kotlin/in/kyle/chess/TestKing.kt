package `in`.kyle.chess

import `in`.kyle.chess.bitboard.toBitboardString
import `in`.kyle.chess.model.Color
import `in`.kyle.chess.model.Piece
import `in`.kyle.chess.model.StandardMoves.kingsideCastleMoveBlack
import `in`.kyle.chess.model.StandardMoves.kingsideCastleMoveWhite
import `in`.kyle.chess.model.StandardMoves.queensideCastleMoveBlack
import `in`.kyle.chess.model.StandardMoves.queensideCastleMoveWhite
import `in`.kyle.chess.reference.testSameMovesAsReference
import `in`.kyle.chess.debug.Fen
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TestKing : StringSpec({

    "test king can be in check" {
        val board = Fen.toBoard("8/1r6/8/8/8/8/1K6/8 w - - 0 1")
        board.isCheck(Color.WHITE.bits) shouldBe true
    }

    "test king cannot be in check" {
        val board = Fen.toBoard("8/2r5/8/8/8/8/1K6/8 w - - 0 1")
        board.isCheck(Color.WHITE.bits) shouldBe false
    }

    "test generates all 8 king moves" {
        testSameMovesAsReference("8/1k6/8/8/4K3/8/8/8 w - - 0 1")
    }

    "test king can move into corner" {
        testSameMovesAsReference("8/1k6/8/8/8/8/6K1/8 w - - 0 1")
    }

    "test king moves in corners" {
        testSameMovesAsReference("8/8/8/8/8/8/8/7K w - - 0 1")
    }

    "test king must move out of check" {
        testSameMovesAsReference("r7/8/8/8/8/1k6/8/K7 w - - 0 1")
    }

    "test king must move out of check - xray" {
        testSameMovesAsReference("r7/8/8/8/1k6/8/K7/8 w - - 0 1") {
            focusPiece(Piece.WHITE_KING)
            runBefore {
                println("Attacks: \n${board.getAttackedSquares(1).toBitboardString()}")
            }
        }
    }

    "white can castle kingside" {
        val board = Fen.toBoard("8/1k6/8/8/8/8/8/4K2R w K - 0 1")
        val moves = board.getCastleMoves(board.getAttackedSquares(1))
        moves shouldBe listOf(kingsideCastleMoveWhite.bits)
    }

    "white can castle queenside" {
        val board = Fen.toBoard("8/8/8/8/8/8/8/R3K3 w Q - 0 1")
        val moves = board.getCastleMoves(board.getAttackedSquares(1))
        moves shouldBe listOf(queensideCastleMoveWhite.bits)
    }

    "white cannot castle queenside obstructed" {
        val board = Fen.toBoard("8/8/8/8/8/8/8/RB2K3 w Q - 0 1")
        val moves = board.getCastleMoves(board.getAttackedSquares(1))
        moves shouldBe emptyList()
    }

    "white cannot castle queenside" {
        val board = Fen.toBoard("8/8/8/8/8/8/8/R3K3 w - - 0 1")
        val moves = board.getCastleMoves(board.getAttackedSquares(1))
        moves shouldBe emptyList()
    }

    "white cannot castle kingside" {
        val board = Fen.toBoard("8/8/8/8/8/8/8/4K2R w - - 0 1")
        val moves = board.getCastleMoves(board.getAttackedSquares(1))
        moves shouldBe emptyList()
    }

    "black can castle kingside" {
        val board = Fen.toBoard("4k2r/8/8/8/8/8/8/8 b k - 0 1")
        val moves = board.getCastleMoves(board.getAttackedSquares(0))
        moves shouldBe listOf(kingsideCastleMoveBlack.bits)
    }

    "black can castle queenside" {
        val board = Fen.toBoard("r3k3/8/8/8/8/8/8/8 b q - 0 1")
        val moves = board.getCastleMoves(board.getAttackedSquares(0))
        moves shouldBe listOf(queensideCastleMoveBlack.bits)
    }

    "black cannot castle queenside obstructed" {
        val board = Fen.toBoard("rb2k3/8/8/8/8/8/8/8 b q - 0 1")
        val moves = board.getCastleMoves(board.getAttackedSquares(0))
        moves shouldBe emptyList()
    }

    "black cannot castle queenside" {
        val board = Fen.toBoard("r3k3/8/8/8/8/8/8/8 b - - 0 1")
        val moves = board.getCastleMoves(board.getAttackedSquares(0))
        moves shouldBe emptyList()
    }

    "black cannot castle kingside" {
        val board = Fen.toBoard("4k2r/8/8/8/8/8/8/8 b - - 0 1")
        val moves = board.getCastleMoves(board.getAttackedSquares(0))
        moves shouldBe emptyList()
    }

    "white cannot castle kingside - reference" {
        testSameMovesAsReference("8/1k6/8/8/8/8/8/4K2R w - - 0 1")
    }

    "cannot castle through check" {
        testSameMovesAsReference("5r2/1k6/8/8/8/8/8/4K2R w K - 0 1")
    }

    "white must block checks" {
        testSameMovesAsReference("2R5/k7/8/8/8/8/PP6/K2r4 w - - 0 1") {
            focusPiece(Piece.WHITE_ROOK)
        }
    }

    "white cannot move pinned piece" {
        testSameMovesAsReference("8/1k6/8/8/7b/8/5R2/4K3 w - - 0 1") {
            focusPiece(Piece.WHITE_ROOK)
        }
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
