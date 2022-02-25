package `in`.kyle.chess.perft

import `in`.kyle.chess.ChessBoard
import `in`.kyle.chess.debug.Fen
import `in`.kyle.chess.model.Move
import `in`.kyle.chess.model.getHumanMoves
import `in`.kyle.chess.reference.ReferenceBoard
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

class TestPerftReference : FreeSpec({

    val inputs = (1..7).associateWith { Perft.PERFT_RESULTS[it] }

    inputs.entries.map { (depth, expected) ->
        "perft($depth) should return ${expected.nodes}" {
            val board = Fen.toBoard(Fen.STARTING_POSITION)
            val reference = ReferenceBoard().apply { setPosition(Fen.STARTING_POSITION) }

            perftRun(board, reference, depth, 0) shouldBe expected.nodes
        }
    }
})

fun perftRun(board: ChessBoard, reference: ReferenceBoard, depth: Int, ply: Int): Long {
    if (depth == 0) {
        return 1
    }
    var nodes: Long = 0

    val humanMoves = board.getHumanMoves()
    val moves = humanMoves.map { it.toString() }.sorted()
    val referenceMoves = reference.getLegalMoves().sorted()

    withClue(lazy {
        """
            $board
            board: ${Fen.format(board)}
            my moves: $moves
            reference moves: $referenceMoves
            moves made so far: ${board.moves.reversed().map { Move(it).toString() }}
            moves bits: ${board.moves.reversed()}
        """.trimIndent()
    }) {
        moves shouldContainExactly referenceMoves
    }

    for (move in humanMoves) {
        board.makeMove(move.bits)
        reference.makeMove(move)
        nodes += perftRun(board, reference, depth - 1, ply + 1)
        reference.undoMove()
        board.undoMove()
    }
    return nodes
}
