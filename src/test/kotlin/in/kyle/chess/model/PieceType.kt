package `in`.kyle.chess.model

enum class PieceType {
    PAWN,
    KNIGHT,
    BISHOP,
    ROOK,
    QUEEN,
    KING;

    fun getPiece(color: Color) = Piece.values().first { it.pieceType == this && it.color == color }
}
