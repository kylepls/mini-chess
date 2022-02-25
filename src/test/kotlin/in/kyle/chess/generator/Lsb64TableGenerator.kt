package `in`.kyle.chess.generator

import `in`.kyle.chess.bitboard.Lsb64Table

fun main() {
    val table = Lsb64Table.base64Encode()
    println(table)
}
