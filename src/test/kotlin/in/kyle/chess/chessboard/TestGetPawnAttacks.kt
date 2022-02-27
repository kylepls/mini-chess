package `in`.kyle.chess.chessboard

import `in`.kyle.chess.bitboard.bitboard
import `in`.kyle.chess.bitboard.shouldBeBitboard
import `in`.kyle.chess.debug.Fen
import `in`.kyle.chess.model.Square.*
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.data.row

class TestGetPawnAttacks : FreeSpec({

    listOf(
        row("Gets all pawn attacks for white - none", "8/8/8/8/8/8/8/8 w - - 0 1", bitboard {}),
        row("Gets all pawn attacks for white - simple", "8/8/8/8/8/8/1P6/8 w - - 0 1", bitboard {
            add(A3, C3)
        }),
        row("Gets all pawn attacks for white - A-file", "8/8/8/8/8/8/P7/8 w - - 0 1", bitboard {
            add(B3)
        }),
        row("Gets all pawn attacks for white - H-file", "8/8/8/8/8/8/7P/8 w - - 0 1", bitboard {
            add(G3)
        }),
        row("Gets all pawn attacks for black - none", "8/8/8/8/8/8/8/8 b - - 0 1", bitboard {}),
        row("Gets all pawn attacks for black - simple", "8/1p6/8/8/8/8/8/8 b - - 0 1", bitboard {
            add(A6, C6)
        }),
        row("Gets all pawn attacks for black - A-file", "8/p7/8/8/8/8/8/8 b - - 0 1", bitboard {
            add(B6)
        }),
        row("Gets all pawn attacks for black - H-file", "8/7p/8/8/8/8/8/8 b - - 0 1", bitboard {
            add(G6)
        }),
    ).map { (description, fen, expected) ->
        description {
            withClue(lazy {
                "FEN: $fen"
            }) {
                val board = Fen.toBoard(fen)
                val attacks = board.getPawnAttacks(board.color)
                attacks shouldBeBitboard expected
            }
        }
    }
})
