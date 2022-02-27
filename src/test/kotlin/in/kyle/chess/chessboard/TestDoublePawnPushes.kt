package `in`.kyle.chess.chessboard

import `in`.kyle.chess.ChessBoard
import `in`.kyle.chess.bitboard.bitboard
import `in`.kyle.chess.bitboard.shouldBeBitboard
import `in`.kyle.chess.debug.Fen
import `in`.kyle.chess.extensions.set
import `in`.kyle.chess.model.Color
import `in`.kyle.chess.model.PieceType
import `in`.kyle.chess.model.Square.*
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.data.row

class TestDoublePawnPushes : FreeSpec({
    val empty = bitboard {}
    listOf(
        // white
        row("Gets all pawn pushes for white - none", "8/8/8/8/8/8/8/8 w - - 0 1", empty),
        row("Gets all pawn pushes for white - simple", "8/8/8/8/8/8/P7/8 w - - 0 1", bitboard {
            add(A2)
        }),
        row(
            "Gets all pawn pushes for white - blocked single",
            "8/8/8/8/8/N7/P7/8 w - - 0 1",
            empty
        ),
        row(
            "Gets all pawn pushes for white - blocked double",
            "8/8/8/8/N7/8/P7/8 w - - 0 1",
            empty
        ),

        // black
        row("Gets all pawn pushes for black - none", "8/8/8/8/8/8/8/8 b - - 0 1", empty),
        row("Gets all pawn pushes for black - simple", "8/1p6/8/8/8/8/8/8 b - - 0 1", bitboard {
            add(B7)
        }),
        row(
            "Gets all pawn pushes for black - blocked single",
            "8/1p6/1n6/8/8/8/8/8 b - - 0 1",
            empty
        ),
        row(
            "Gets all pawn pushes for black - blocked double",
            "8/1p6/8/1n6/8/8/8/8 b - - 0 1",
            empty
        ),
    ).map { (description, fen, expected) ->
        description {
            withClue(lazy {
                "FEN: $fen"
            }) {
                val board = Fen.toBoard(fen)
                val singlePawnPushes = board.singlePawnPushes(board.color)
                val doublePawnPushes = board.doublePawnPushes(board.color, singlePawnPushes)
                doublePawnPushes shouldBeBitboard expected
            }
        }
    }

    listOf(
        row(Color.WHITE, A2.rankSquares, A4.rankSquares),
        row(Color.BLACK, A7.rankSquares, A5.rankSquares)
    ).map { (color, startingSquares, destinationSquares) ->
        "Double pawn pushes should work for $color" - {
            startingSquares.zip(destinationSquares).map { (from, to) ->
                "Can push $from to $to" {
                    val board = ChessBoard()
                    board.color = color.bits
                    board[from] = PieceType.PAWN.getPiece(color)

                    val singlePawnPushes = board.singlePawnPushes(board.color)
                    val doublePawnPushes = board.doublePawnPushes(board.color, singlePawnPushes)
                    doublePawnPushes shouldBeBitboard bitboard {
                        add(from)
                    }
                }
            }
        }
    }

    "simple test" {
        val board = ChessBoard()
        val color = Color.WHITE
        val from = H2
        board[from] = PieceType.PAWN.getPiece(color)

        val singlePawnPushes = board.singlePawnPushes(board.color)
        val doublePawnPushes = board.doublePawnPushes(board.color, singlePawnPushes)
        doublePawnPushes shouldBeBitboard bitboard {
            add(from)
        }
    }
})
