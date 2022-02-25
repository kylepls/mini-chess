package `in`.kyle.chess.debug

import `in`.kyle.chess.MASK_ENCODING
import `in`.kyle.chess.MASK_FROM
import `in`.kyle.chess.MASK_PIECE
import `in`.kyle.chess.MASK_TO

object Lan {

    private val pieceMapping = mapOf(
        1 to 'P',
        2 to 'N',
        3 to 'B',
        4 to 'R',
        5 to 'Q',
        6 to 'K',
        9 to 'P',
        10 to 'N',
        11 to 'B',
        12 to 'R',
        13 to 'Q',
        14 to 'K'
    )

    private val promotionMapping = mapOf(
        8 to 'N',
        9 to 'B',
        10 to 'R',
        11 to 'Q',
        12 to 'N',
        13 to 'B',
        14 to 'R',
        15 to 'Q'
    )

    fun format(move: Int): String {
        val fromId = (MASK_FROM and move)
        val toId = (MASK_TO and move) ushr 6

        val fromRank = fromId / 8
        val fromFile = fromId % 8

        val toRank = toId / 8
        val toFile = toId % 8

        val pieceId = (MASK_PIECE and move) ushr 12
        val encoding = (MASK_ENCODING and move) ushr 16

        val piece = if (pieceId in arrayOf(1, 6)) pieceMapping[pieceId] else ""
        val capture = if (0b0100 and encoding != 0) "x" else ""
        val from = "${fromFile.toChar() + 97}${fromRank + 1}"
        val to = "${toFile.toChar() + 97}${toRank + 1}"
        return "$piece$from$capture$to${promotionMapping[encoding] ?: ""}"
    }
}
