package `in`.kyle.chess

import `in`.kyle.chess.bitboard.bitboard
import `in`.kyle.chess.bitboard.shouldBeBitboard
import `in`.kyle.chess.extensions.currentPieceOccupancies
import `in`.kyle.chess.extensions.set
import `in`.kyle.chess.model.*
import `in`.kyle.chess.model.Square.*
import `in`.kyle.chess.reference.ReferenceBoard
import `in`.kyle.chess.util.Fen
import `in`.kyle.chess.util.Lan
import `in`.kyle.chess.util.PrettyBoard
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.data.row
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe

class TestMove : FreeSpec({
    "test pawn single push" {
        testMoveAgainstReference("8/8/8/8/8/8/4P3/8 w - - 0 1", Move(E2, E3, Piece.WHITE_PAWN))
    }

    "test double single push white" {
        testMoveAgainstReference(
            "8/8/8/8/8/8/4P3/8 w - - 0 1",
            Move(E2, E4, Piece.WHITE_PAWN, Encoding.DOUBLE_PAWN_PUSH)
        )
    }

    "test double pawn push black" {
        testMoveAgainstReference(
            "8/4p3/8/8/8/8/8/8 b - - 0 1",
            Move(E7, E5, Piece.BLACK_PAWN, Encoding.DOUBLE_PAWN_PUSH)
        )
    }

    "test pawn single capture" {
        testMoveAgainstReference(
            "8/8/8/8/8/8/4P3/8 w - - 0 1",
            Move(E2, F3, Piece.WHITE_PAWN, Encoding.CAPTURE)
        )
    }

    "test en passant black left" {
        testMoveAgainstReference(
            "8/1k6/8/8/4Pp2/8/1K6/8 b - e3 0 1",
            Move(F4, E3, Piece.BLACK_PAWN, Encoding.EN_PASSANT_CAPTURE)
        )
    }

    "test en passant black right" {
        testMoveAgainstReference(
            "8/1k6/8/8/3pP3/8/1K6/8 b - e3 0 1",
            Move(D4, E3, Piece.BLACK_PAWN, Encoding.EN_PASSANT_CAPTURE)
        )
    }

    "test en passant white left" {
        testMoveAgainstReference(
            "8/1k6/8/5pP1/8/8/1K6/8 w - f6 0 1",
            Move(G5, F6, Piece.WHITE_PAWN, Encoding.EN_PASSANT_CAPTURE)
        )
    }

    "test en passant white right" {
        testMoveAgainstReference(
            "8/1k6/8/4Pp2/8/8/1K6/8 w - f6 0 1",
            Move(E5, F6, Piece.WHITE_PAWN, Encoding.EN_PASSANT_CAPTURE)
        )
    }

    "test king castle white" {
        testMoveAgainstReference(
            "k7/8/8/8/8/8/8/4K2R w K - 0 1",
            Move(E1, G1, Piece.WHITE_KING, Encoding.KING_CASTLE)
        )
    }

    "test queen castle white" {
        testMoveAgainstReference(
            "7k/8/8/8/8/8/8/R3K3 w Q - 0 1",
            Move(E1, C1, Piece.WHITE_KING, Encoding.QUEEN_CASTLE)
        )
    }

    "test king castle black" {
        testMoveAgainstReference(
            "4k2r/8/8/8/8/8/8/4K3 b k - 0 1",
            Move(E8, G8, Piece.BLACK_KING, Encoding.KING_CASTLE)
        )
    }

    "test queen castle black" {
        testMoveAgainstReference(
            "r3k3/8/8/8/8/8/8/4K3 b q - 0 1",
            Move(E8, C8, Piece.BLACK_KING, Encoding.QUEEN_CASTLE)
        )
    }

    "promotions should work" - {
        val promotions = Encoding.values().filter { it.isPromotion && !it.isCapture }
        val captures = Encoding.values().filter { it.isPromotion && it.isCapture }
        listOf(
            row(
                "promotions work for white",
                Color.WHITE,
                "8/6P1/8/8/8/k7/8/K7 w - - 0 1",
                G7,
                G8,
                promotions
            ),
            row(
                "promotions work for black",
                Color.BLACK,
                "8/8/8/8/8/k7/6p1/K7 b - - 0 1",
                G2,
                G1,
                promotions
            ),

            row(
                "promotion captures work for white",
                Color.WHITE,
                "5r2/6P1/8/8/8/k7/8/K7 w - - 0 1",
                G7,
                F8,
                captures
            ),
            row(
                "promotion captures work for black",
                Color.BLACK,
                "8/8/8/8/8/k7/6p1/K4R2 b - - 0 1",
                G2,
                F1,
                captures
            )
        ).map { (name, color, fen, from, to, encodings) ->
            name - {
                encodings.map { encoding ->
                    val move = Move(from, to, PieceType.PAWN.getPiece(color), encoding)
                    withClue("Move: ${Lan.format(move.bits)}") {
                        val encodingStr = encoding.name.lowercase().replace("_", " ")
                        "$encodingStr should work" {
                            testMoveAgainstReference(fen, move)
                        }
                    }
                }
            }
        }
    }

    "test making and undoing a move restores correct state" {
        val board = ChessBoard()
        board[E1] = Piece.WHITE_KING

        val move = Move(E1, E2, Piece.WHITE_KING)
        board.makeMove(move.bits)

        board.currentPieceOccupancies[Piece.WHITE_KING.bits] shouldBeBitboard bitboard {
            add(E2)
        }

        board.undoMove()
        board.currentPieceOccupancies[Piece.WHITE_KING.bits] shouldBeBitboard bitboard {
            add(E1)
        }
    }

    "test en-passant works with moves white" - {

        val board = Fen.toBoard("8/1p6/8/P7/8/8/8/8 b - - 0 1")
        board.makeMove(Move(B7, B5, Piece.BLACK_PAWN, Encoding.DOUBLE_PAWN_PUSH).bits)

        board.enPassant shouldBe B6.bitboard

    }
})

fun testMoveAgainstReference(startingFen: String, move: Move) {
    val board = Fen.toBoard(startingFen)
    val referenceBoard = ReferenceBoard().apply { setPosition(startingFen) }

    board.makeMove(move.bits)
    referenceBoard.makeMove(move)

    var referenceOccupancies = referenceBoard.getOccupancies()
    var boardOccupancies = values().associateWith {
        if (board[it.index] == 0) {
            null
        } else {
            Piece.fromBits(board[it.index])
        }
    }

    assertSoftly {
        board.castleRights() shouldContainExactly referenceBoard.castleRights()
    }

    withClue(lazy {
        buildString {
            append("My Board:\n${PrettyBoard.print(board)}\n")
            append("Reference Board:\n${PrettyBoard.print(Fen.toBoard(referenceBoard.getFen()))}")
        }
    }) {
        boardOccupancies shouldBe referenceOccupancies
    }

    board.undoMove()
    referenceBoard.undoMove()

    referenceOccupancies = referenceBoard.getOccupancies()
    boardOccupancies = values().associateWith {
        if (board[it.index] == 0) {
            null
        } else {
            Piece.fromBits(board[it.index])
        }
    }

    assertSoftly {
        board.castleRights() shouldContainExactly referenceBoard.castleRights()
    }

    withClue(lazy {
        buildString {
            append("My Board:\n${PrettyBoard.print(board)}\n")
            append("Reference Board:\n${PrettyBoard.print(Fen.toBoard(referenceBoard.getFen()))}")
        }
    }) {
        boardOccupancies shouldBe referenceOccupancies
    }
}

