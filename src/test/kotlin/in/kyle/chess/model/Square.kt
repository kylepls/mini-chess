package `in`.kyle.chess.model

import `in`.kyle.chess.util.MoveUtils
import kotlin.math.abs
import kotlin.math.absoluteValue

enum class Square(val index: Int) {

    A1(0),
    B1(1),
    C1(2),
    D1(3),
    E1(4),
    F1(5),
    G1(6),
    H1(7),
    A2(8),
    B2(9),
    C2(10),
    D2(11),
    E2(12),
    F2(13),
    G2(14),
    H2(15),
    A3(16),
    B3(17),
    C3(18),
    D3(19),
    E3(20),
    F3(21),
    G3(22),
    H3(23),
    A4(24),
    B4(25),
    C4(26),
    D4(27),
    E4(28),
    F4(29),
    G4(30),
    H4(31),
    A5(32),
    B5(33),
    C5(34),
    D5(35),
    E5(36),
    F5(37),
    G5(38),
    H5(39),
    A6(40),
    B6(41),
    C6(42),
    D6(43),
    E6(44),
    F6(45),
    G6(46),
    H6(47),
    A7(48),
    B7(49),
    C7(50),
    D7(51),
    E7(52),
    F7(53),
    G7(54),
    H7(55),
    A8(56),
    B8(57),
    C8(58),
    D8(59),
    E8(60),
    F8(61),
    G8(62),
    H8(63);

    val rank: Int
        get() = index / 8

    val file: Int
        get() = index % 8

    val friendlyFile: File
        get() = File.values()[file]

    val friendlyRank: Rank
        get() = Rank.values()[rank]

    val north: Square
        get() = values()[index + 8]

    val south: Square
        get() = values()[index - 8]

    val east: Square
        get() = values()[index + 1]

    val west: Square
        get() = values()[index - 1]

    val northEast: Square
        get() = values()[index + 9]

    val northWest: Square
        get() = values()[index + 7]

    val southEast: Square
        get() = values()[index - 7]

    val southWest: Square
        get() = values()[index - 9]

    val bitboard: Long
        get() = 1L shl index

    val rankSquares: List<Square>
        get() = values().filter { it.rank == rank }

    val fileSquares: List<Square>
        get() = values().filter { it.file == file }

    fun relative(dx: Int, dy: Int): Square? {
        return if (file + dx !in (0..7) || rank + dy !in (0..7)) {
            null
        } else {
            values()[index + dx + 8 * dy]
        }
    }

    fun relative(direction: Direction): Square? {
        return relative(direction.dx, direction.dy)
    }

    infix fun diagonalTo(destination: Square): List<Square> {
        val dx = (destination.file - file).let { if (it != 0) it / abs(it) else 1 }
        val dy = (destination.rank - rank).let { if (it != 0) it / abs(it) else 1 }
        require(dx.absoluteValue == dy.absoluteValue) { "Square $destination is not on the same diagonal as $this" }

        var temp = this
        return sequence {
            yield(temp)
            while (true) {
                temp = temp.relative(dx, dy) ?: break
                yield(temp)
                if (temp == destination) break
            }
        }.toList()
    }

    fun getSquaresInDirection(direction: Direction) = sequence {
        var temp = this@Square
        while (true) {
            temp = temp.relative(direction) ?: break
            yield(temp)
        }
    }.toList()

    override fun toString(): String {
        return MoveUtils.getChessNotation(index)
    }

    companion object {
        fun get(file: Int, rank: Int): Square {
            return values()[file + rank * 8]
        }

        fun get(chessNotation: String): Square {
            return values()[MoveUtils.getSquare(chessNotation)]
        }

        fun get(index: Int): Square {
            return values()[index]
        }

        fun getRank(rank: Rank): List<Square> {
            return values().filter { it.rank == rank.ordinal }
        }

        fun getFile(file: File): List<Square> {
            return values().filter { it.file == file.ordinal }
        }
    }

    enum class Direction(val dx: Int, val dy: Int) {
        NORTH(0, 1),
        NORTH_EAST(1, 1),
        EAST(1, 0),
        SOUTH_EAST(1, -1),
        SOUTH(0, -1),
        SOUTH_WEST(-1, -1),
        WEST(-1, 0),
        NORTH_WEST(-1, 1);
    }
}
