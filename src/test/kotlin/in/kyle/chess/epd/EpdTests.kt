package `in`.kyle.chess.epd

import `in`.kyle.chess.reference.ReferenceBoard
import `in`.kyle.chess.util.Fen
import `in`.kyle.chess.reference.withMovesComparisonClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.datatest.withData
import io.kotest.matchers.collections.shouldContainExactly


class EpdTests : StringSpec({

    withData(listOf("bratko-kopec-tests.epd")) { file ->
        withData(nameFn = { it[Epd.OperationKey.ID] }, Epd.readEpd(file) as Iterable<Epd>) { epd ->
            val fen = epd.fen
            val referenceBoard = ReferenceBoard().apply { setPosition(fen) }

            val board = Fen.toBoard(fen)

            val referenceMoves = referenceBoard.getLegalMoves().sorted()
            val myMovesSan = board.getValidMoves().map { it.toString() }.sorted()

            withMovesComparisonClue(board, referenceBoard) {
                myMovesSan shouldContainExactly referenceMoves
            }
        }
    }
})
