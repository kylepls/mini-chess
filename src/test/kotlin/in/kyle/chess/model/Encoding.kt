package `in`.kyle.chess.model

enum class Encoding(val index: Int, val isCapture: Boolean, val isPromotion: Boolean) {

    QUIET(0, false, false),
    DOUBLE_PAWN_PUSH(1, false, false),
    KING_CASTLE(2, false, false),
    QUEEN_CASTLE(3, false, false),
    CAPTURE(4, true, false),
    EN_PASSANT_CAPTURE(5, true, false),
    KNIGHT_PROMOTION(6, false, true),
    BISHOP_PROMOTION(7, false, true),
    ROOK_PROMOTION(8, false, true),
    QUEEN_PROMOTION(9, false, true),
    KNIGHT_PROMOTION_CAPTURE(10, true, true),
    BISHOP_PROMOTION_CAPTURE(11, true, true),
    ROOK_PROMOTION_CAPTURE(12, true, true),
    QUEEN_PROMOTION_CAPTURE(13, true, true);

    companion object {
        fun fromBits(encoding: Int): Encoding {
            return values().first { it.ordinal == encoding }
        }
    }
}
