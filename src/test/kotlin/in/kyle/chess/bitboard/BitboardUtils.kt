package `in`.kyle.chess.bitboard

import `in`.kyle.chess.model.File
import `in`.kyle.chess.model.Rank
import `in`.kyle.chess.model.Square
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe

object BitboardUtils {
    fun printBitboard(long: ULong) {
        println(long.toBitboardString())
    }
}

fun ULong.toBitboardString(): String {
    var string = ""
    for (rank in 7 downTo 0) {
        string += "${rank + 1} "
        for (file in 0..7) {
            string += if (this and (1UL shl (rank * 8 + file)) != 0UL) {
                "| X "
            } else {
                "| . "
            }
        }
        string += "|\n"
    }
    string += "X "
    for (file in 0..7) {
        string += "| ${'A' + file} "
    }
    string += "|"
    return string
}

infix fun ULong.shouldBeBitboard(other: ULong) {
    withClue(lazy {
        "Bitboard should be\n${other.toBitboardString()}\nbut was\n${this.toBitboardString()}"
    }) {
        this shouldBe other
    }
}

data class BitboardConfiguration(val occupancies: MutableSet<Square>) {

    fun add(vararg square: Square) {
        occupancies.addAll(square)
    }

    fun remove(vararg square: Square) {
        occupancies.removeAll(square)
    }

    fun addSquares(squares: Iterable<Square>) {
        occupancies.addAll(squares)
    }

    fun removeSquares(squares: Iterable<Square>) {
        occupancies.removeAll(squares)
    }

    fun addRank(rank: Rank) {
        addSquares(rank.squares)
    }

    fun addDiagonal(start: Square, end: Square) {
        addSquares(start.diagonalTo(end))
    }

    fun addFile(file: File) {
        addSquares(file.squares)
    }

    fun allSquares() {
        addSquares(Square.values().toList())
    }

    fun toBitboard(): ULong {
        var bitboard = 0UL
        for (square in occupancies) {
            bitboard = bitboard or (1UL shl square.index)
        }
        return bitboard
    }
}

fun bitboard(initializer: BitboardConfiguration.() -> Unit): ULong {
    val configuration = BitboardConfiguration(mutableSetOf())
    configuration.initializer()
    return configuration.toBitboard()
}

fun ULong.shiftUpRanks(ranks: Int): ULong {
    return this shl (ranks * 8)
}
