package `in`.kyle.chess.epd

import `in`.kyle.chess.model.getHumanMoves
import `in`.kyle.chess.reference.ReferenceBoard
import `in`.kyle.chess.debug.Fen
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContainExactly


class EpdTests : FreeSpec({

    listOf("bratko-kopec-tests.epd").map { fileName ->
        fileName - {
            Epd.readEpd(fileName).map { epd ->
                epd[Epd.OperationKey.ID] {
                    val fen = epd.fen
                    val referenceBoard = ReferenceBoard().apply { setPosition(fen) }

                    val board = Fen.toBoard(fen)

                    val referenceMoves = referenceBoard.getLegalMoves().sorted()
                    val myMovesSan = board.getHumanMoves().map { it.toString() }.sorted()

                    // withMovesComparisonClue(board, referenceBoard) {
                    myMovesSan shouldContainExactly referenceMoves
                    // }
                }
            }
        }
    }
})
