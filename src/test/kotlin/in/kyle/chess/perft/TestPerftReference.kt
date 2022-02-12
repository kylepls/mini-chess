package `in`.kyle.chess.perft

import `in`.kyle.chess.ChessBoard
import `in`.kyle.chess.util.Fen
import `in`.kyle.chess.reference.toReference
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import com.github.bhlangonijr.chesslib.Board as ReferenceBoard

class TestPerftReference : StringSpec({

    val inputs = (1..6).associateWith { Perft.PERFT_RESULTS[it] }

    fun perftRun(board: ChessBoard, reference: ReferenceBoard, depth: Int, ply: Int): Long {
        if (depth == 0) {
            return 1
        }
        var tmp = board
        var nodes: Long = 0

        val moves = tmp.getValidMoves().sortedBy { it.toString() }
        val referenceMoves = reference.legalMoves().sortedBy { it.toString() }

        withClue(lazy {
            val sortedMoves = moves.map { it.toString().replace("-", "") }.sorted()
            val sortedReferenceMoves = referenceMoves.map { it.toString() }
            """
            $board
            board: ${Fen.toFen(tmp)}
            my moves: $sortedMoves
            reference moves: $sortedReferenceMoves
            """.trimIndent()
        }) {
            moves.size shouldBe referenceMoves.size
        }

        for (move in moves) {
            val copy = tmp.copy()
            tmp.makeMove(move)
            reference.doMove(move.toReference())
            nodes += perftRun(tmp, reference, depth - 1, ply + 1)
            reference.undoMove()
            tmp = copy
        }
        return nodes
    }

    withData(
        nameFn = { "perft(${it.key}) should return ${it.value.nodes}" },
        inputs.entries
    ) { (depth, expected) ->
        val board = Fen.toBoard(Fen.STARTING_POSITION)
        val reference = ReferenceBoard().apply { loadFromFen(Fen.STARTING_POSITION) }
        val result = perftRun(board, reference, depth, 0)
        result shouldBe expected.nodes
    }
})
