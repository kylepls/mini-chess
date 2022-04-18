package `in`.kyle.chess.chessboard

import `in`.kyle.chess.bitboard.bitboard
import `in`.kyle.chess.util.Fen
import `in`.kyle.chess.extensions.getHumanCastleMoves
import `in`.kyle.chess.model.File
import `in`.kyle.chess.model.Move
import io.kotest.core.spec.style.FreeSpec
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class TestGetCastleMoves : FreeSpec({
    val emptyBitboard = 0L
    listOf(
        // Kingside white
        row(
            "Can castle kingside for white",
            "8/8/8/8/8/8/8/4K2R w K - 0 1",
            emptyBitboard,
            listOf(Move.KINGSIDE_CASTLE_WHITE)
        ),
        row(
            "Cannot castle kingside for white - no castle privileges",
            "8/8/8/8/8/8/8/4K2R w - - 0 1",
            emptyBitboard,
            listOf()
        ),
        row(
            "Cannot castle kingside for white - obstruction F1",
            "8/8/8/8/8/8/8/4K1NR w K - 0 1",
            emptyBitboard,
            listOf()
        ),
        row(
            "Cannot castle kingside for white - obstruction G1",
            "8/8/8/8/8/8/8/4KN1R w K - 0 1",
            emptyBitboard,
            listOf()
        ),
        row(
            "Cannot castle kingside for white - blocked by check E1",
            "4r3/8/8/8/8/8/8/4K2R w K - 0 1",
            bitboard {
                addFile(File.F)
            },
            listOf()
        ),
        row(
            "Cannot castle kingside for white - blocked by check F1",
            "5r2/8/8/8/8/8/8/4K2R w K - 0 1",
            bitboard {
                addFile(File.F)
            },
            listOf()
        ),
        row(
            "Cannot castle kingside for white - blocked by check G1",
            "6r1/8/8/8/8/8/8/4K2R w K - 0 1",
            bitboard {
                addFile(File.G)
            },
            listOf()
        ),

        // Queenside white
        row(
            "Can castle queenside for white",
            "8/8/8/8/8/8/8/R3K3 w Q - 0 1",
            emptyBitboard,
            listOf(Move.QUEENSIDE_CASTLE_WHITE)
        ),
        row(
            "Cannot castle queenside for white - no castle privileges",
            "8/8/8/8/8/8/8/R3K3 w - - 0 1",
            emptyBitboard,
            listOf()
        ),
        row(
            "Cannot castle queenside for white - obstruction B1",
            "8/8/8/8/8/8/8/RN2K3 w Q - 0 1",
            emptyBitboard,
            listOf()
        ),
        row(
            "Cannot castle queenside for white - obstruction C1",
            "8/8/8/8/8/8/8/R1N1K3 w Q - 0 1",
            emptyBitboard,
            listOf()
        ),
        row(
            "Cannot castle queenside for white - obstruction D1",
            "8/8/8/8/8/8/8/R2NK3 w Q - 0 1",
            emptyBitboard,
            listOf()
        ),
        row(
            "Cannot castle queenside for white - blocked by check C1",
            "2r5/8/8/8/8/8/8/R3K3 w Q - 0 1",
            bitboard {
                addFile(File.C)
            },
            listOf()
        ),
        row(
            "Cannot castle queenside for white - blocked by check D1",
            "3r4/8/8/8/8/8/8/R3K3 w Q - 0 1",
            bitboard {
                addFile(File.D)
            },
            listOf()
        ),
        row(
            "Cannot castle queenside for white - blocked by check E1",
            "4r3/8/8/8/8/8/8/R3K3 w Q - 0 1",
            bitboard {
                addFile(File.E)
            },
            listOf()
        ),

        // Kingside black
        row(
            "Can castle kingside for black",
            "4k2r/8/8/8/8/8/8/8 b k - 0 1",
            emptyBitboard,
            listOf(Move.KINGSIDE_CASTLE_BLACK)
        ),
        row(
            "Cannot castle kingside for black - no castle privileges",
            "4k2r/8/8/8/8/8/8/8 b - - 0 1",
            emptyBitboard,
            listOf()
        ),
        row(
            "Cannot castle kingside for black - obstruction F8",
            "4kn1r/8/8/8/8/8/8/8 b k - 0 1",
            emptyBitboard,
            listOf()
        ),
        row(
            "Cannot castle kingside for black - obstruction G8",
            "4k1nr/8/8/8/8/8/8/8 b k - 0 1",
            emptyBitboard,
            listOf()
        ),
        row(
            "Cannot castle kingside for black - blocked by check E8",
            "4k2r/8/8/8/8/8/8/4R3 b k - 0 1",
            bitboard {
                addFile(File.E)
            },
            listOf()
        ),
        row(
            "Cannot castle kingside for black - blocked by check F8",
            "4k2r/8/8/8/8/8/8/5R2 b k - 0 1",
            bitboard {
                addFile(File.F)
            },
            listOf()
        ),
        row(
            "Cannot castle kingside for black - blocked by check G8",
            "4k2r/8/8/8/8/8/8/6R1 b k - 0 1",
            bitboard {
                addFile(File.G)
            },
            listOf()
        ),

        // Queenside black
        row(
            "Can castle queenside for black",
            "r3k3/8/8/8/8/8/8/8 b q - 0 1",
            emptyBitboard,
            listOf(Move.QUEENSIDE_CASTLE_BLACK)
        ),
        row(
            "Cannot castle queenside for black - no castle privileges",
            "r3k3/8/8/8/8/8/8/8 b - - 0 1",
            emptyBitboard,
            listOf()
        ),
        row(
            "Cannot castle queenside for black - obstruction B8",
            "rn2k3/8/8/8/8/8/8/8 b q - 0 1",
            emptyBitboard,
            listOf()
        ),
        row(
            "Cannot castle queenside for black - obstruction C8",
            "r1n1k3/8/8/8/8/8/8/8 b q - 0 1",
            emptyBitboard,
            listOf()
        ),
        row(
            "Cannot castle queenside for black - obstruction D8",
            "r2nk3/8/8/8/8/8/8/8 b q - 0 1",
            emptyBitboard,
            listOf()
        ),
        row(
            "Cannot castle queenside for black - blocked by check C8",
            "r3k3/8/8/8/8/8/8/2R5 b q - 0 1",
            bitboard {
                addFile(File.C)
            },
            listOf()
        ),
        row(
            "Cannot castle queenside for black - blocked by check D8",
            "r3k3/8/8/8/8/8/8/3R4 b q - 0 1",
            bitboard {
                addFile(File.D)
            },
            listOf()
        ),
        row(
            "Cannot castle queenside for black - blocked by check E8",
            "r3k3/8/8/8/8/8/8/4R3 b q - 0 1",
            bitboard {
                addFile(File.E)
            },
            listOf()
        ),
    ).map { (description, fen, attackedSquares, expected) ->
        description {
            val board = Fen.toBoard(fen)
            val actual = board.getHumanCastleMoves(attackedSquares)
            actual shouldBe expected
        }
    }
})
