package `in`.kyle.chess.perft

import `in`.kyle.chess.ChessBoard
import `in`.kyle.chess.model.Move
import `in`.kyle.chess.model.castleRights
import `in`.kyle.chess.model.getHumanMoves
import `in`.kyle.chess.reference.ReferenceBoard
import `in`.kyle.chess.util.Fen
import `in`.kyle.chess.util.Lichess
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe


const val WITH_REFERENCE = false
val MAX_NODES = if (WITH_REFERENCE) 1000000 else 4000000 * 5 // 5 seconds on my machine

class TestPerft : FreeSpec({
    Perft.values().forEach { perft ->
        perft.name - {
            perft.nodes.forEachIndexed { index, nodes ->
                val depth = index + 1
                "depth $depth=$nodes".config(enabled = nodes < MAX_NODES) - {
                    val board = Fen.toBoard(perft.fen)
                    val referenceBoard = ReferenceBoard().apply { setPosition(perft.fen) }
                    val actual = if (WITH_REFERENCE) perftRunWithReference(
                        board,
                        referenceBoard,
                        depth
                    ) else perftRun(board, depth)
                    actual shouldBe nodes
                }
            }
        }
    }
})

fun main() {
    val perftTest = Perft.KIWI_PETE
    val board = Fen.toBoard(perftTest.fen)
    val start = System.currentTimeMillis()
    (1..6).forEach {
        perftRun(board, it)
        val ms = System.currentTimeMillis() - start + 1
        val mNodesPerSecond = (perftTest.nodes[it-1] / (ms/1000.0) / 1000000.0)
        println("$it:\t ${ms}ms\t\t ${"%.6f".format(mNodesPerSecond)} M/s")
    }
}

fun perftRun(board: ChessBoard, depth: Int): Long {
    val start = board.hmc
    if (depth == 0) {
        return 1
    }
    var nodes: Long = 0
    var consumer: (Int) -> Unit = {}
    consumer = { move: Int ->
        if ((board.hmc - start) < depth - 1) {
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

fun perftRunWithReference(
    board: ChessBoard,
    reference: ReferenceBoard,
    depth: Int,
): Long {
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
            my board:  ${Lichess.analysisUrl(Fen.format(board))}
            reference: ${Lichess.analysisUrl(reference.getFen())}
            my moves: $moves
            reference moves: $referenceMoves
            moves made so far: ${
            board.moves.slice(0..board.hmc).reversed().map { Move(it).toString() }
        }
            moves bits: ${board.moves.slice(0..board.hmc).reversed()}
        """.trimIndent()
    }) {
        board.castleRights() shouldContainExactly reference.castleRights()
        if (moves != referenceMoves) {
            println("error")
        }
        moves shouldContainExactly referenceMoves
    }

    for (move in humanMoves) {
        board.makeMove(move.bits)
        reference.makeMove(move)
        nodes += perftRunWithReference(board, reference, depth - 1)
        reference.undoMove()
        board.undoMove()
    }
    return nodes
}
