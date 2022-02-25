package `in`.kyle.chess.bitboard

import java.util.*

object Lsb64Table {
    val lsb64Table = intArrayOf(
        63,
        30,
        3,
        32,
        59,
        14,
        11,
        33,
        60,
        24,
        50,
        9,
        55,
        19,
        21,
        34,
        61,
        29,
        2,
        53,
        51,
        23,
        41,
        18,
        56,
        28,
        1,
        43,
        46,
        27,
        0,
        35,
        62,
        31,
        58,
        4,
        5,
        49,
        54,
        6,
        15,
        52,
        12,
        40,
        7,
        42,
        45,
        16,
        25,
        57,
        48,
        13,
        10,
        39,
        8,
        44,
        20,
        47,
        38,
        22,
        17,
        37,
        36,
        26
    )

    fun base64Encode(): String {
        val bytes = mutableListOf<Byte>()

        lsb64Table.forEach {
            bytes.add(it.toByte())
        }

        return Base64.getEncoder().encodeToString(bytes.toByteArray())
    }
}
