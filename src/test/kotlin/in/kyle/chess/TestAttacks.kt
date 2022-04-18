package `in`.kyle.chess

import `in`.kyle.chess.bitboard.bitboard
import `in`.kyle.chess.bitboard.shouldBeBitboard
import `in`.kyle.chess.util.PrettyBoard
import `in`.kyle.chess.extensions.getPieceAttacks
import `in`.kyle.chess.extensions.set
import `in`.kyle.chess.model.Color
import `in`.kyle.chess.model.PieceType
import `in`.kyle.chess.model.Square
import `in`.kyle.chess.model.Square.Direction.*
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.data.row

@Suppress("UNUSED_PARAMETER")
class TestAttacks : FreeSpec({

    fun pawnAttacks(square: Square, color: Color) = bitboard {
        val direction = if (color == Color.WHITE) {
            listOf(NORTH_EAST, NORTH_WEST)
        } else {
            listOf(SOUTH_EAST, SOUTH_WEST)
        }
        direction
            .mapNotNull { square.relative(it) }
            .forEach { add(it) }
    }

    fun knightAttacks(square: Square, color: Color) = bitboard {
        listOf(1 to 2, 2 to 1, -1 to 2, -2 to 1, 1 to -2, 2 to -1, -1 to -2, -2 to -1)
            .mapNotNull { (dx, dy) -> square.relative(dx, dy) }
            .forEach { add(it) }
    }

    fun bishopAttacks(square: Square, color: Color) = bitboard {
        listOf(NORTH_EAST, SOUTH_EAST, NORTH_WEST, SOUTH_WEST)
            .flatMap { square.getSquaresInDirection(it) }
            .forEach { add(it) }
    }

    fun rookAttacks(square: Square, color: Color) = bitboard {
        listOf(NORTH, EAST, SOUTH, WEST)
            .flatMap { square.getSquaresInDirection(it) }
            .forEach { add(it) }
    }

    fun queenAttacks(square: Square, color: Color) = bishopAttacks(square, color) or
            rookAttacks(square, color)

    fun kingAttacks(square: Square, color: Color) = bitboard {
        Square.Direction.values()
            .mapNotNull { square.relative(it) }
            .forEach { add(it) }
    }

    Color.values().map { color ->
        "all attacks work for $color" - {
            val pawnSquares =
                Square.values().toList() - Square.A1.rankSquares - Square.A8.rankSquares
            val everySquare = Square.values().toList()
            listOf(
                row(PieceType.PAWN, ::pawnAttacks, pawnSquares),
                row(PieceType.KNIGHT, ::knightAttacks, everySquare),
                row(PieceType.BISHOP, ::bishopAttacks, everySquare),
                row(PieceType.ROOK, ::rookAttacks, everySquare),
                row(PieceType.QUEEN, ::queenAttacks, everySquare),
                row(PieceType.KING, ::kingAttacks, everySquare)
            ).map { (pieceType, attacksFunction, squares) ->
                val wording = if (squares == Square.values().toList()) {
                    "every square"
                } else {
                    "${squares.size} squares"
                }
                "$pieceType has correct attacks on $wording" - {
                    squares.map { square ->
                        "should have correct attacks on $square" {
                            val board = ChessBoard()
                            board[square] = pieceType.getPiece(color)
                            withClue(lazy {
                                """
                                    Color: $color
                                    Origin square: $square
                                    Board: 
                                    {board}
                                """.trimIndent().replace("{board}", PrettyBoard.print(board))
                            }) {
                                val reference = attacksFunction(square, color)
                                board.getPieceAttacks(square) shouldBeBitboard reference
                                board.getAttackedSquares(color.bits) shouldBeBitboard reference
                            }
                        }
                    }
                }
            }
        }
    }
})
