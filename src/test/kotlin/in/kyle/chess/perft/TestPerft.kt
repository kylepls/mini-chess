package `in`.kyle.chess.perft

import `in`.kyle.chess.ChessBoard
import `in`.kyle.chess.debug.Fen
import `in`.kyle.chess.debug.Lan
import `in`.kyle.chess.model.getHumanMoves
import io.kotest.core.spec.style.FreeSpec
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class TestPerft : FreeSpec({
    listOf(
        row("Perft 1", 1, 20),
        row("Perft 2", 2, 400),
        row("Perft 3", 3, 8902),
        row("Perft 4", 4, 197281),
        row("Perft 5", 5, 4865609),
        row("Perft 6", 6, 119060324),
        row("Perft 7", 7, 3195901860L),
    ).map { (description, depth, expected) ->
        description {
            val board = Fen.toBoard(Fen.STARTING_POSITION)
            val actual = perftRun(board, depth)
            actual shouldBe expected
        }
    }
})

fun perftRun(board: ChessBoard, depth: Int): Long {
    if (depth == 0) {
        return 1
    }
    var nodes: Long = 0
    var consumer: (Int) -> Unit = {}
    consumer = { move: Int ->
        if (board.hmc < depth - 1) {
            board.makeMove(move)
            board.getMoves(consumer)
            board.undoMove()
        } else {
            nodes++
        }
    }
    board.getMoves(consumer)
    return nodes
}
