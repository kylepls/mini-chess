package `in`.kyle.chess.generator

import `in`.kyle.chess.bitboard.BitboardUtils

private fun makeNotFile(file: Int): Long {
    var mask = Long.MAX_VALUE

    for (i in 0..7) {
        val remove = 1UL.toLong() shl (file + i * 8)
        mask = mask and remove.inv()
    }

    return mask
}

private fun makeRank(rank: Int): Long {
    var mask = 0UL.toLong()
    for (i in 0..7) {
        mask = mask or (1UL.toLong() shl (i + rank * 8))
    }
    return mask
}

fun main() {
    val notAFile = makeNotFile(0)
    val notHFile = makeNotFile(7)

    println("Not A File: 0x${notAFile.toString(16)}UL.toLong()")
    println("Not H File: 0x${notHFile.toString(16)}UL.toLong()")

    val promotionMask = makeRank(0) or makeRank(7)
    println("Promotion Mask: 0x${promotionMask.toString(16)}UL.toLong()")
    BitboardUtils.printBitboard(promotionMask)
}
