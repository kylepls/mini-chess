package `in`.kyle.chess.reference

import `in`.kyle.chess.ChessBoard
import `in`.kyle.chess.Move
import `in`.kyle.chess.util.Fen
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.move.Move as ReferenceMove
import com.github.bhlangonijr.chesslib.File
import com.github.bhlangonijr.chesslib.Square
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly

class ReferenceBoard {

    private val board: Board = Board().apply { loadFromFen(Fen.STARTING_POSITION) }

    fun getLegalMoves(): List<String> {
        return board.legalMoves().map { it.toString() }
    }

    fun setPosition(fen: String) {
        board.loadFromFen(fen)
    }

    fun undoMove() {
        board.undoMove()
    }

    fun makeMove(move: Move) {
        board.doMove(move.toReference(), true)
    }

    fun sanityCheckEquals(myBoard: ChessBoard) {
        require(Fen.toFen(myBoard) == board.fen) {
            """
            |FENs do not match
            |My FEN: ${Fen.toFen(myBoard)}
            |Reference FEN: ${board.fen}
            """.trimIndent()
        }
    }
}

infix fun ChessBoard.shouldHaveSameMovesAs(referenceBoard: ReferenceBoard) {
    referenceBoard.sanityCheckEquals(this)

    val moves = this.getValidMoves()
    val referenceMoves = referenceBoard.getLegalMoves()

    val sortedMoves = moves.map { it.toString().replace("-", "") }.sorted()
    val sortedReferenceMoves = referenceMoves.sorted()

    withClue(lazy {
        """
        My Moves        (${moves.size}): ${sortedMoves.joinToString(", ")}
        Reference Moves (${referenceMoves.size}): ${sortedReferenceMoves.joinToString(", ")}
        {board}
        Fen: ${Fen.toFen(this)}
        """.trimIndent().replace("{board}", this.toString())
    }) {
        sortedMoves shouldContainExactly sortedReferenceMoves
    }
}
