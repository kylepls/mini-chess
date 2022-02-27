package `in`.kyle.chess.model

import `in`.kyle.chess.*
import `in`.kyle.chess.debug.San

data class Move(val bits: Int) {

    constructor(
        from: Square,
        to: Square,
        piece: Piece,
        encoding: Encoding = Encoding.QUIET
    ) : this(newMove(from.index, to.index, piece.bits, encoding.ordinal))

    val from: Square
        get() = Square.get(bits and MASK_FROM)

    val to: Square
        get() = Square.get((bits and MASK_TO) ushr 6)

    val piece: Piece
        get() = Piece.fromBits((bits and MASK_PIECE) ushr 12)

    val encoding: Encoding
        get() = Encoding.fromBits((bits and MASK_ENCODING) ushr 16)

    val color: Color
        get() = piece.color

    val isPromotion: Boolean
        get() = encoding.isPromotion

    fun getPromotionPiece(): Piece {
        require(encoding.isPromotion) { "Move is not a promotion" }
        return when (encoding) {
            Encoding.BISHOP_PROMOTION, Encoding.BISHOP_PROMOTION_CAPTURE -> PieceType.BISHOP.getPiece(
                color
            )
            Encoding.KNIGHT_PROMOTION, Encoding.KNIGHT_PROMOTION_CAPTURE -> PieceType.KNIGHT.getPiece(
                color
            )
            Encoding.ROOK_PROMOTION, Encoding.ROOK_PROMOTION_CAPTURE -> PieceType.ROOK.getPiece(
                color
            )
            Encoding.QUEEN_PROMOTION, Encoding.QUEEN_PROMOTION_CAPTURE -> PieceType.QUEEN.getPiece(
                color
            )
            else -> throw IllegalStateException("Invalid encoding: $encoding")
        }
    }

    override fun toString(): String {
        val promotion = if (isPromotion) getPromotionPiece().chessNotation.lowercase() else ""
        return "$from$to$promotion"
    }

    fun toString(board: ChessBoard): String {
        return San.format(this.bits, board)
    }

    fun toString(binary: Boolean = false): String {
        return if (binary) {
            "0b${this.bits.toString(2).padStart(16, '0')}"
        } else {
            this.toString()
        }
    }

    companion object {

        val KINGSIDE_CASTLE_WHITE =
            Move(Square.E1, Square.G1, Piece.WHITE_KING, Encoding.KING_CASTLE)
        val QUEENSIDE_CASTLE_WHITE =
            Move(Square.E1, Square.C1, Piece.WHITE_KING, Encoding.QUEEN_CASTLE)
        val KINGSIDE_CASTLE_BLACK =
            Move(Square.E8, Square.G8, Piece.BLACK_KING, Encoding.KING_CASTLE)
        val QUEENSIDE_CASTLE_BLACK =
            Move(Square.E8, Square.C8, Piece.BLACK_KING, Encoding.QUEEN_CASTLE)

    }
}

fun ChessBoard.getHumanMoves(): List<Move> {
    val moves = mutableListOf<Move>()
    this.getMoves{ moveBits: Int -> moves.add(Move(moveBits)) }
    return moves
}
