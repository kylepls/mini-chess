package `in`.kyle.chess.generator

import `in`.kyle.chess.bitboard.bitboard
import `in`.kyle.chess.bitboard.shiftUpRanks
import `in`.kyle.chess.model.Square
import `in`.kyle.chess.model.StandardMoves.kingsideCastleMoveBlack
import `in`.kyle.chess.model.StandardMoves.kingsideCastleMoveWhite
import `in`.kyle.chess.model.StandardMoves.queensideCastleMoveBlack
import `in`.kyle.chess.model.StandardMoves.queensideCastleMoveWhite

fun main() {
    val kingsideCastle = bitboard {
        add(Square.E1, Square.F1, Square.G1)
    }
    val queensideCastle = bitboard {
        add(Square.E1, Square.D1, Square.C1)
    }

    val safetyMasks = listOf(
        kingsideCastle,
        queensideCastle,
        kingsideCastle.shiftUpRanks(7),
        queensideCastle.shiftUpRanks(7)
    )

    println("Safety Masks: " + safetyMasks.map { "0x${it.toString(16)}UL" })

    val castleMoves = listOf(kingsideCastleMoveWhite, queensideCastleMoveWhite, kingsideCastleMoveBlack, queensideCastleMoveBlack)
        .map { it.bits }
    println("Castle Moves: $castleMoves")

    val kingsideCastleOccupancy = bitboard {
        add(Square.F1, Square.G1)
    }
    val queensideCastleOccupancy = bitboard {
        add(Square.B1, Square.C1, Square.D1)
    }

    val occupancyMasks = listOf(
        kingsideCastleOccupancy,
        queensideCastleOccupancy,
        kingsideCastleOccupancy.shiftUpRanks(7),
        queensideCastleOccupancy.shiftUpRanks(7)
    )

    println("Occupancy Masks: " + occupancyMasks.map { "0x${it.toString(16)}UL" })
}
