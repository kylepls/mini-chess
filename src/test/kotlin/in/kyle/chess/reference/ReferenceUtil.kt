package `in`.kyle.chess.reference

import `in`.kyle.chess.ChessBoard
import `in`.kyle.chess.Move
import `in`.kyle.chess.util.Fen
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Square
import io.kotest.assertions.withClue

fun Move.toReference(): com.github.bhlangonijr.chesslib.move.Move {
    val fromNotation = Move.getChessNotation(this.from).uppercase()
    val toNotation = Move.getChessNotation(this.to).uppercase()
    val fromSquare = Square.valueOf(fromNotation)
    val toSquare = Square.valueOf(toNotation)

    if ("PROMOTION" in this.encoding.name) {
        val pieceName = this.encoding.name.split("_")[1]
        val color = if (this.piece.isUpperCase()) "WHITE" else "BLACK"
        val promotionPiece = Piece.valueOf("${color}_$pieceName")
        return com.github.bhlangonijr.chesslib.move.Move(fromSquare, toSquare, promotionPiece)
    }

    return com.github.bhlangonijr.chesslib.move.Move(fromSquare, toSquare)
}

inline fun <R> withMovesComparisonClue(
    board: ChessBoard,
    referenceBoard: ReferenceBoard,
    thunk: () -> R
) = withClue(lazy {
    val moves = board.getValidMoves().sortedBy { it.toString() }
    val referenceMoves = referenceBoard.getLegalMoves().sorted()
    """
        My Moves        (${moves.size}): ${moves.joinToString(", ")}
        Reference Moves (${referenceMoves.size}): ${referenceMoves.joinToString(", ")}
        {board}
        Fen: ${Fen.toFen(board)}
    """.trimIndent().replace("{board}", board.toString())
}, thunk)

fun testSameMovesAsReference(fen: String) {
    val board = Fen.toBoard(fen)
    val referenceBoard = ReferenceBoard().apply { setPosition(fen) }

    board shouldHaveSameMovesAs referenceBoard
}
