package `in`.kyle.chess.model

enum class Piece(
    val bits: Int,
    val chessNotation: String,
    val color: Color,
    val pieceType: PieceType,
    val prettyString: String
) {

    WHITE_PAWN(1, "P", Color.WHITE, PieceType.PAWN, "♙"),
    WHITE_KNIGHT(2, "N", Color.WHITE, PieceType.KNIGHT, "♘"),
    WHITE_BISHOP(3, "B", Color.WHITE, PieceType.BISHOP, "♗"),
    WHITE_ROOK(4, "R", Color.WHITE, PieceType.ROOK, "♖"),
    WHITE_QUEEN(5, "Q", Color.WHITE, PieceType.QUEEN, "♕"),
    WHITE_KING(6, "K", Color.WHITE, PieceType.KING, "♔"),
    BLACK_PAWN(7, "p", Color.BLACK, PieceType.PAWN, "♟"),
    BLACK_KNIGHT(8, "n", Color.BLACK, PieceType.KNIGHT, "♞"),
    BLACK_BISHOP(9, "b", Color.BLACK, PieceType.BISHOP, "♝"),
    BLACK_ROOK(10, "r", Color.BLACK, PieceType.ROOK, "♜"),
    BLACK_QUEEN(11, "q", Color.BLACK, PieceType.QUEEN, "♛"),
    BLACK_KING(12, "k", Color.BLACK, PieceType.KING, "♚");

    val colorBit: Int
        get() = bits and 0b1000

    val uncoloredBits: Int
        get() = bits and 0b0111

    fun oppositeColor() = when (this) {
        WHITE_PAWN -> BLACK_PAWN
        WHITE_KNIGHT -> BLACK_KNIGHT
        WHITE_BISHOP -> BLACK_BISHOP
        WHITE_ROOK -> BLACK_ROOK
        WHITE_QUEEN -> BLACK_QUEEN
        WHITE_KING -> BLACK_KING
        BLACK_PAWN -> WHITE_PAWN
        BLACK_KNIGHT -> WHITE_KNIGHT
        BLACK_BISHOP -> WHITE_BISHOP
        BLACK_ROOK -> WHITE_ROOK
        BLACK_QUEEN -> WHITE_QUEEN
        BLACK_KING -> WHITE_KING
    }

    companion object {
        fun fromChessNotation(chessNotation: String): Piece {
            return values().first { it.chessNotation == chessNotation }
        }

        fun fromBits(bits: Int): Piece {
            return values().first { it.bits == bits }
        }
    }
}
