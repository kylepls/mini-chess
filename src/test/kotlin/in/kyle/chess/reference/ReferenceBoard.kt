package `in`.kyle.chess.reference

import `in`.kyle.chess.ChessBoard
import `in`.kyle.chess.model.Piece
import `in`.kyle.chess.model.Color
import `in`.kyle.chess.model.Move
import `in`.kyle.chess.debug.Fen
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.PieceType
import com.github.bhlangonijr.chesslib.Side
import com.github.bhlangonijr.chesslib.Square

class ReferenceBoard {

    private val board: Board = Board().apply { loadFromFen(Fen.STARTING_POSITION) }

    private val pieceTypeMapping = mapOf(
        `in`.kyle.chess.model.PieceType.PAWN to PieceType.PAWN,
        `in`.kyle.chess.model.PieceType.KNIGHT to PieceType.KNIGHT,
        `in`.kyle.chess.model.PieceType.BISHOP to PieceType.BISHOP,
        `in`.kyle.chess.model.PieceType.ROOK to PieceType.ROOK,
        `in`.kyle.chess.model.PieceType.QUEEN to PieceType.QUEEN,
        `in`.kyle.chess.model.PieceType.KING to PieceType.KING
    )

    private val pieceTypeMappingInv = pieceTypeMapping.entries.associateBy({ it.value }, { it.key })

    private val sideMapping = mapOf(
        Color.WHITE to Side.WHITE,
        Color.BLACK to Side.BLACK
    )

    private val sideMappingInv = sideMapping.entries.associateBy({ it.value }, { it.key })

    fun getLegalMoves(): List<String> {
        return board.legalMoves().map { it.toString() }
    }

    fun setPosition(fen: String) {
        board.loadFromFen(fen)
    }

    fun getFen(): String {
        return board.fen
    }

    fun undoMove() {
        board.undoMove()
    }

    fun makeMove(move: Move) {
        board.doMove(move.toReference(), true)
    }

    fun getPieceSquare(piece: Piece): `in`.kyle.chess.model.Square {
        val referencePieces = Square.values().filter {
            val referencePiece = board.getPiece(it)
            referencePiece.pieceType == pieceTypeMapping[piece.pieceType] &&
                    referencePiece.pieceSide == sideMapping[piece.color]
        }
        require(referencePieces.size == 1) { "Expected 1 piece, got ${referencePieces.size}" }

        return `in`.kyle.chess.model.Square.get(referencePieces[0].ordinal)
    }

    fun getOccupancies(): Map<`in`.kyle.chess.model.Square, Piece?> {
        return Square.values()
            .filter { it != Square.NONE }
            .filter { board.getPiece(it).pieceType != PieceType.NONE }
            .associate { s ->
                val referencePiece = board.getPiece(s)
                val square = `in`.kyle.chess.model.Square.get(s.ordinal)

                if (referencePiece == com.github.bhlangonijr.chesslib.Piece.NONE) {
                    square to null
                } else {
                    val piece = Piece.values().first {
                        it.pieceType == pieceTypeMappingInv[referencePiece.pieceType]
                                && sideMappingInv[referencePiece.pieceSide] == it.color
                    }
                    square to piece
                }
            }
    }

    fun sanityCheckEquals(myBoard: ChessBoard) {
        require(Fen.format(myBoard) == board.fen) {
            """
            |FENs do not match
            |My FEN: ${Fen.format(myBoard)}
            |Reference FEN: ${board.fen}
            """.trimIndent()
        }
    }
}

