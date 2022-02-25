package `in`.kyle.chess.model

object StandardMoves {

    val kingsideCastleMoveWhite = Move(Square.E1, Square.G1, Piece.WHITE_KING, Encoding.KING_CASTLE)
    val queensideCastleMoveWhite = Move(Square.E1, Square.C1, Piece.WHITE_KING, Encoding.QUEEN_CASTLE)
    val kingsideCastleMoveBlack = Move(Square.E8, Square.G8, Piece.BLACK_KING, Encoding.KING_CASTLE)
    val queensideCastleMoveBlack = Move(Square.E8, Square.C8, Piece.BLACK_KING, Encoding.QUEEN_CASTLE)

}
