package `in`.kyle.chess.generator

import `in`.kyle.chess.bitboard.BitboardUtils
import `in`.kyle.chess.bitboard.bitboard
import `in`.kyle.chess.bitboard.toBitboardString
import `in`.kyle.chess.model.Diagonal
import io.kotest.mpp.file

private fun makeDiagonal(startRank: Int, startFile: Int): Long {
    var rank = startRank
    var file = startFile

    var mask = 0UL.toLong()

    while (rank <= 7 && file <= 7) {
        val index = rank * 8 + file
        val bit = 1UL.toLong() shl index
        mask = mask or bit
        rank++
        file++
    }

    return mask
}

private fun makeAntiDiagonal(startRank: Int, startFile: Int): Long {
    var rank = startRank
    var file = startFile

    var mask = 0UL.toLong()

    while (rank >= 0 && file <= 7) {
        val index = rank * 8 + file
        val bit = 1UL.toLong() shl index
        mask = mask or bit
        rank--
        file++
    }

    return mask
}

fun main() {
    val diagonalMasks = LongArray(16) { 0UL.toLong() }
    Diagonal.diagonals.forEach { (index, mask) ->
        diagonalMasks[index] = bitboard { mask.forEach { add(it) } }
    }
    println("Diagonal Masks")
    println(diagonalMasks.map { "0x${it.toString(16)}" }.toList())

    diagonalMasks.forEachIndexed { index, mask ->
        println("Diagonal $index:")
        println(mask.toBitboardString())
    }

    val antiDiagonalMasks = LongArray(16) { 0UL.toLong() }
    for (rank in 7 downTo 0) {
        val mask = makeAntiDiagonal(rank, 0)
        antiDiagonalMasks[7 - rank] = mask
    }
    for ((i, file) in (7 downTo 1).withIndex()) {
        val mask = makeAntiDiagonal(7, file)
        antiDiagonalMasks[i + 9] = mask
    }
    println("Anti-Diagonal Masks")
    println(antiDiagonalMasks.map { "0x${it.toULong().toString(16)}" }.toList())
    BitboardUtils.printBitboard(antiDiagonalMasks[10])

    antiDiagonalMasks.forEachIndexed { index, mask ->
        println("Anti Diagonal $index:")
        println(mask.toBitboardString())
    }
}
