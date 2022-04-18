package `in`.kyle.chess.perft

import `in`.kyle.chess.model.Move
import `in`.kyle.chess.model.Piece
import `in`.kyle.chess.model.Square
import `in`.kyle.chess.util.Fen
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class Simple : FreeSpec({

    "test king move" {
        val board =
            Fen.toBoard("r3k2r/p1ppqpb1/b3pnp1/3PN3/np2P3/2N1BQ1p/PPP1BPPP/R3K2R w KQkq - 0 1")

        board.makeMove(Move(Square.H1, Square.F1, Piece.WHITE_ROOK).bits)

        withClue("Fen: ${Fen.format(board)}") {
            board.castling shouldBe 3
        }

    }
})
