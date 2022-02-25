package `in`.kyle.chess.perft

import `in`.kyle.chess.ChessBoard
import `in`.kyle.chess.debug.Fen
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class TestPerft : FreeSpec({
    (1..7).associateWith { Perft.PERFT_RESULTS[it] }.map { (depth, expected) ->
        "perft $depth" {
            val board = Fen.toBoard(Fen.STARTING_POSITION)
            val actual = perftRun(board, depth)
            actual shouldBe expected.nodes
        }
    }
})

fun perftRun(board: ChessBoard, depth: Int): Long {
    if (depth == 0) {
        return 1
    }
    var nodes: Long = 0
    val moves: List<Int> = board.getMoves()
    for (move in moves) {
        board.makeMove(move)
        nodes += perftRun(board, depth - 1)
        board.undoMove()
    }
    return nodes
}
