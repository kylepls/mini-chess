package `in`.kyle.chess.generator

import `in`.kyle.chess.bitboard.BitboardUtils

private fun makePawnAttacks(file: Int): Long {
    return when (file) {
        0 -> 1UL.toLong() shl 1
        7 -> 1UL.toLong() shl 6
        else -> (1UL.toLong() shl (file + 1)) or (1UL.toLong() shl (file - 1))
    }
}

fun main() {
    val attacks = LongArray(8)
    for (file in 0..7) {
        attacks[file] = makePawnAttacks(file)
    }
    println("Pawn attacks:")
    println(attacks.map { "0x${it.toString(16)}UL.toLong()" }.toList())
    BitboardUtils.printBitboard(attacks[2])
}
