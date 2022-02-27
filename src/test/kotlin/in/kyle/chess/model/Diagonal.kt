package `in`.kyle.chess.model

import `in`.kyle.chess.model.Square.*

@OptIn(ExperimentalUnsignedTypes::class)
object Diagonal {

    val diagonals = mapOf(
        0 to (A1 diagonalTo H8),
        1 to (A2 diagonalTo G8),
        2 to (A3 diagonalTo F8),
        3 to (A4 diagonalTo E8),
        4 to (A5 diagonalTo D8),
        5 to (A6 diagonalTo C8),
        6 to (A7 diagonalTo B8),
        7 to (A8 diagonalTo A8),

        9 to (H1 diagonalTo H1),
        10 to (G1 diagonalTo H2),
        11 to (F1 diagonalTo H3),
        12 to (E1 diagonalTo H4),
        13 to (D1 diagonalTo H5),
        14 to (C1 diagonalTo H6),
        15 to (B1 diagonalTo H7),
    )

    var diagonalMasks8 = longArrayOf(
        0x8040201008040201UL.toLong(),
        0x4020100804020100UL.toLong(),
        0x2010080402010000UL.toLong(),
        0x1008040201000000UL.toLong(),
        0x804020100000000UL.toLong(),
        0x402010000000000UL.toLong(),
        0x201000000000000UL.toLong(),
        0x100000000000000UL.toLong(),
        0x80402010080402UL.toLong(),
        0x804020100804UL.toLong(),
        0x8040201008UL.toLong(),
        0x80402010UL.toLong(),
        0x804020UL.toLong(),
        0x8040UL.toLong(),
        0x80UL.toLong(),
        0x0UL.toLong()
    )

    var antiDiagonalMasks8 = longArrayOf(
        0x102040810204080UL.toLong(),
        0x1020408102040UL.toLong(),
        0x10204081020UL.toLong(),
        0x102040810UL.toLong(),
        0x1020408UL.toLong(),
        0x10204UL.toLong(),
        0x102UL.toLong(),
        0x1UL.toLong(),
        0x0UL.toLong(),
        0x8000000000000000UL.toLong(),
        0x4080000000000000UL.toLong(),
        0x2040800000000000UL.toLong(),
        0x1020408000000000UL.toLong(),
        0x810204080000000UL.toLong(),
        0x408102040800000UL.toLong(),
        0x204081020408000UL.toLong()
    )
}
