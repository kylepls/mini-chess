package `in`.kyle.chess

import `in`.kyle.chess.model.Encoding
import `in`.kyle.chess.model.Move
import `in`.kyle.chess.model.Piece
import `in`.kyle.chess.model.Square
import io.kotest.core.spec.style.FreeSpec

class TestCastle : FreeSpec({

    "move white kingside rook revokes kingside castling" {
        testMoveAgainstReference(
            "r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1",
            Move(Square.H1, Square.H2, Piece.WHITE_ROOK)
        )
    }

    "move white queenside rook revokes queenside castling" {
        testMoveAgainstReference(
            "r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1",
            Move(Square.A1, Square.A2, Piece.WHITE_ROOK)
        )
    }

    "move white king revokes castling" {
        testMoveAgainstReference(
            "r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1",
            Move(Square.E1, Square.D1, Piece.WHITE_KING)
        )
    }

    "white capture kingside rook revokes castling" {
        testMoveAgainstReference(
            "r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1",
            Move(Square.H1, Square.H8, Piece.WHITE_ROOK, Encoding.CAPTURE)
        )
    }

    "white capture queenside rook revokes castling" {
        testMoveAgainstReference(
            "r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1",
            Move(Square.A1, Square.A8, Piece.WHITE_ROOK, Encoding.CAPTURE)
        )
    }

    "move black kingside rook revokes kingside castling" {
        testMoveAgainstReference(
            "r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 0 1",
            Move(Square.H8, Square.H2, Piece.BLACK_ROOK)
        )
    }

    "move black queenside rook revokes queenside castling" {
        testMoveAgainstReference(
            "r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 0 1",
            Move(Square.A8, Square.A2, Piece.BLACK_ROOK)
        )
    }

    "move black king revokes castling" {
        testMoveAgainstReference(
            "r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 0 1",
            Move(Square.E8, Square.D8, Piece.BLACK_KING)
        )
    }

    "black capture kingside rook revokes castling" {
        testMoveAgainstReference(
            "r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 0 1",
            Move(Square.H8, Square.H1, Piece.BLACK_ROOK, Encoding.CAPTURE)
        )
    }

    "black capture queenside rook revokes castling" {
        testMoveAgainstReference(
            "r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 0 1",
            Move(Square.A8, Square.A1, Piece.BLACK_ROOK, Encoding.CAPTURE)
        )
    }

})
