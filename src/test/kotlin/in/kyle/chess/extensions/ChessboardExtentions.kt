package `in`.kyle.chess.extensions

import `in`.kyle.chess.ChessBoard
import `in`.kyle.chess.bitboard.bitboard
import `in`.kyle.chess.model.Color
import `in`.kyle.chess.model.Piece
import `in`.kyle.chess.model.PieceType
import `in`.kyle.chess.model.Square

operator fun ChessBoard.get(square: Square): Piece? {
    return this.board[square.index].takeIf { it != 0 }?.let { Piece.fromBits(it) }
}

operator fun ChessBoard.set(square: Square, piece: Piece?) {
    this[square.index] = piece?.bits ?: 0
}

fun ChessBoard.getPieceAttacks(square: Square, accountForFriendlyPieces: Boolean = true): ULong {
    val piece = this[square] ?: error("No piece at $square")
    val pieceBitboard = bitboard {
        add(square)
    }

    val friendlyPiecesMaskInv =
        if (accountForFriendlyPieces) colorOccupancy[color].inv() else 0UL.inv()

    return when (piece.pieceType) {
        PieceType.PAWN -> getSinglePawnAttacks(square.index)
        PieceType.KNIGHT -> getAllKnightAttacks(pieceBitboard)
        PieceType.BISHOP -> getSingleBishopAttacks(square.index, friendlyPiecesMaskInv)
        PieceType.ROOK -> getSingleRookAttacks(square.index, friendlyPiecesMaskInv)
        PieceType.QUEEN -> getSingleRookAttacks(square.index, friendlyPiecesMaskInv) or
                getSingleBishopAttacks(square.index, friendlyPiecesMaskInv)
        PieceType.KING -> getKingAttacks(piece.bits)
    }
}

fun ChessBoard.getAttackedSquares(color: Color): ULong {
    return this.getAttackedSquares(color.bits)
}
