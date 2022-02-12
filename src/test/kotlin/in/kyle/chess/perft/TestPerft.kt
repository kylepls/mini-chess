package `in`.kyle.chess.perft

import `in`.kyle.chess.ChessBoard
import `in`.kyle.chess.Move
import `in`.kyle.chess.util.Fen
import io.kotest.core.spec.style.StringSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class TestPerft : StringSpec({

    val inputs = (1..6).associateWith { Perft.PERFT_RESULTS[it] }

    withData(
        nameFn = { "perft(${it.key}) should return ${it.value.nodes}" },
        inputs.entries
    ) { (depth, expected) ->
        val board = Fen.toBoard(Fen.STARTING_POSITION)
        val result = perftRun(board, depth)
        result shouldBe expected.nodes
    }

})
fun perftRun(board: ChessBoard, depth: Int): Long {
    if (depth == 0) {
        return 1
    }
    var tmp = board
    var nodes: Long = 0
    val moves: List<Move> = tmp.getValidMoves()
    for (move in moves) {
        val copy = tmp.copy()
        tmp.makeMove(move)
        nodes += perftRun(tmp, depth - 1)
        tmp = copy
    }
    return nodes
}
