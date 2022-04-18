package `in`.kyle.chess.reference

import `in`.kyle.chess.ChessBoard
import `in`.kyle.chess.model.Move
import `in`.kyle.chess.model.getHumanMoves
import `in`.kyle.chess.util.Fen
import `in`.kyle.chess.util.PrettyBoard
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Square
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly
import com.github.bhlangonijr.chesslib.move.Move as ReferenceMove

fun Move.toReference(): ReferenceMove {
    val fromNotation = this.from.toString().uppercase()
    val toNotation = this.to.toString().uppercase()
    val fromSquare = Square.valueOf(fromNotation)
    val toSquare = Square.valueOf(toNotation)

    if (isPromotion) {
        val pieceName = this.getPromotionPiece().pieceType.name
        val color = this.color.name.uppercase()
        val promotionPiece = Piece.valueOf("${color}_$pieceName")
        return ReferenceMove(fromSquare, toSquare, promotionPiece)
    }

    return ReferenceMove(fromSquare, toSquare)
}

fun testSameMovesAsReference(
    fen: String,
    contextFn: (ReferenceAssertionContext.() -> Unit) = {}
): ChessBoard {
    val board = Fen.toBoard(fen)
    val referenceBoard = ReferenceBoard().apply { setPosition(fen) }
    referenceBoard.sanityCheckEquals(board)

    var moves = board.getHumanMoves()
    var referenceMoves = referenceBoard.getLegalMoves()

    val contextEval = ReferenceAssertionContext(referenceBoard).apply(contextFn)
    contextEval.runBefore.forEach {
        it(
            ReferenceRunnableContext(
                board,
                referenceBoard,
                contextEval.focusSquare
            )
        )
    }

    val focusSquare = contextEval.focusSquare
    if (focusSquare != null) {
        moves = moves.filter { it.from == focusSquare }
        referenceMoves = referenceMoves.filter { it.startsWith(focusSquare.toString()) }
    }
    val sortedMoves = moves.map { it.toString() }.sorted()
    val sortedReferenceMoves = referenceMoves.sorted()

    withClue(lazy {
        """
            My Moves        (${moves.size}): ${sortedMoves.joinToString(", ")}
            Reference Moves (${referenceMoves.size}): ${sortedReferenceMoves.joinToString(", ")}
            {board}
            Fen: ${Fen.format(board)}
            """.trimIndent().replace("{board}", PrettyBoard.print(board))
    }) {
        sortedMoves shouldContainExactly sortedReferenceMoves
    }
    return board
}

class ReferenceAssertionContext(private val referenceBoard: ReferenceBoard) {

    internal val runBefore = mutableListOf<ReferenceRunnableContext.() -> Unit>()
    internal var focusSquare: `in`.kyle.chess.model.Square? = null

    fun focusPiece(piece: `in`.kyle.chess.model.Piece) {
        focusSquare(referenceBoard.getPieceSquare(piece))
    }

    fun focusSquare(square: `in`.kyle.chess.model.Square) {
        focusSquare = square
    }

    fun runBefore(block: ReferenceRunnableContext.() -> Unit) {
        runBefore.add(block)
    }
}

data class ReferenceRunnableContext(
    val board: ChessBoard,
    val referenceBoard: ReferenceBoard,
    private val internal_focusSquare: `in`.kyle.chess.model.Square? = null
) {
    val focusSquare: `in`.kyle.chess.model.Square
        get() = internal_focusSquare ?: throw IllegalStateException("No focus square set")
}
