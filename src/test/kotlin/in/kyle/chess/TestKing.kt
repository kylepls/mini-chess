package `in`.kyle.chess

import `in`.kyle.chess.bitboard.toBitboardString
import `in`.kyle.chess.reference.testSameMovesAsReference
import `in`.kyle.chess.util.Fen
import `in`.kyle.chess.extensions.getHumanCastleMoves
import `in`.kyle.chess.model.*
import `in`.kyle.chess.model.Move.Companion.KINGSIDE_CASTLE_BLACK
import `in`.kyle.chess.model.Move.Companion.KINGSIDE_CASTLE_WHITE
import `in`.kyle.chess.model.Move.Companion.QUEENSIDE_CASTLE_BLACK
import `in`.kyle.chess.model.Move.Companion.QUEENSIDE_CASTLE_WHITE
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
        val moves = board.getHumanCastleMoves(board.getAttackedSquares(1))
        moves shouldBe listOf(KINGSIDE_CASTLE_WHITE)
    }

    "white can castle queenside" {
        val board = Fen.toBoard("8/8/8/8/8/8/8/R3K3 w Q - 0 1")
        val moves = board.getHumanCastleMoves(board.getAttackedSquares(1))
        moves shouldBe listOf(QUEENSIDE_CASTLE_WHITE)
    }

    "white cannot castle queenside obstructed" {
        val board = Fen.toBoard("8/8/8/8/8/8/8/RB2K3 w Q - 0 1")
        val moves = board.getHumanCastleMoves(board.getAttackedSquares(1))
        moves shouldBe emptyList()
    }

    "white cannot castle queenside" {
        val board = Fen.toBoard("8/8/8/8/8/8/8/R3K3 w - - 0 1")
        val moves = board.getHumanCastleMoves(board.getAttackedSquares(1))
        moves shouldBe emptyList()
    }

    "white cannot castle kingside" {
        val board = Fen.toBoard("8/8/8/8/8/8/8/4K2R w - - 0 1")
        val moves = board.getHumanCastleMoves(board.getAttackedSquares(1))
        moves shouldBe emptyList()
    }

    "black can castle kingside" {
        val board = Fen.toBoard("4k2r/8/8/8/8/8/8/8 b k - 0 1")
        val moves = board.getHumanCastleMoves(board.getAttackedSquares(0))
        moves shouldBe listOf(KINGSIDE_CASTLE_BLACK)
    }

    "black can castle queenside" {
        val board = Fen.toBoard("r3k3/8/8/8/8/8/8/8 b q - 0 1")
        val moves = board.getHumanCastleMoves(board.getAttackedSquares(0))
        moves shouldBe listOf(QUEENSIDE_CASTLE_BLACK)
    }

    "black cannot castle queenside obstructed" {
        val board = Fen.toBoard("rb2k3/8/8/8/8/8/8/8 b q - 0 1")
        val moves = board.getHumanCastleMoves(board.getAttackedSquares(0))
        moves shouldBe emptyList()
    }

    "black cannot castle queenside" {
        val board = Fen.toBoard("r3k3/8/8/8/8/8/8/8 b - - 0 1")
        val moves = board.getHumanCastleMoves(board.getAttackedSquares(0))
        moves shouldBe emptyList()
    }

    "black cannot castle kingside" {
        val board = Fen.toBoard("4k2r/8/8/8/8/8/8/8 b - - 0 1")
        val moves = board.getHumanCastleMoves(board.getAttackedSquares(0))
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

    "test white move castle revokes rights" {
        testMoveAgainstReference("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1",
            Move(Square.E1, Square.G1, Piece.WHITE_KING, Encoding.KING_CASTLE)
        )
    }

    "test black move castle revokes rights" {
        testMoveAgainstReference("r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 0 1",
            Move(Square.E8, Square.G8, Piece.BLACK_KING, Encoding.KING_CASTLE)
        )
    }
})
