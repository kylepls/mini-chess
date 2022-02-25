package `in`.kyle.chess

import `in`.kyle.chess.bitboard.bitboard
import `in`.kyle.chess.bitboard.shouldBeBitboard
import `in`.kyle.chess.bitboard.toBitboardString
import `in`.kyle.chess.debug.PrettyBoard
import `in`.kyle.chess.extensions.set
import `in`.kyle.chess.model.Color
import `in`.kyle.chess.model.Move
import `in`.kyle.chess.model.Piece
import `in`.kyle.chess.model.Square
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class TestBoard : FreeSpec({
    fun withBoardClue(board: ChessBoard, thunk: () -> Any) = withClue(lazy {
        val colorBoards = Color.values().map { board.colorOccupancy[it.bits].toBitboardString() }
        """
            Board:
            {board}
            
            White Occupancy:
            {whiteOccupancy}
            
            Black Occupancy:
            {blackOccupancy}
            
        """.trimIndent()
            .replace("{board}", PrettyBoard.print(board))
            .replace("{whiteOccupancy}", colorBoards[0])
            .replace("{blackOccupancy}", colorBoards[1])
    }, thunk)

    "should set piece" - {
        Piece.values().map { piece ->
            "should be able to set $piece" - {
                Square.values().map { square ->
                    "at square $square" - {
                        val board = ChessBoard()
                        board[square] = piece
                        val pieceBitboard = bitboard { add(square) }

                        "board should be same" {
                            withBoardClue(board) {
                                board.board shouldBe IntArray(64) { 0 }.apply {
                                    set(square.index, piece.bits)
                                }
                            }
                        }

                        "occupancy should be same" {
                            withBoardClue(board) {
                                board.occupancy shouldBeBitboard pieceBitboard
                            }
                        }

                        Color.values().map { color ->
                            val expected = if (color == piece.color) pieceBitboard else bitboard { }
                            "colorOccupancy should be same for $color" {
                                withBoardClue(board) {
                                    board.colorOccupancy[color.bits] shouldBeBitboard expected
                                }
                            }
                        }

                        "empty should be same" {
                            withBoardClue(board) {
                                board.empty shouldBeBitboard bitboard {
                                    allSquares()
                                    remove(square)
                                }
                            }
                        }

                        Piece.values().map { otherPiece ->
                            val expected = if (piece == otherPiece) pieceBitboard else bitboard {}
                            "pieceOccupancies should be same for $otherPiece" {
                                withBoardClue(board) {
                                    board.pieceOccupancies[otherPiece.bits] shouldBeBitboard expected
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    "should remove piece" - {
        Piece.values().map { piece ->
            "should be able to remove $piece" - {
                Square.values().map { square ->
                    "at square $square" - {
                        val board = ChessBoard()
                        board[square] = piece
                        board[square] = null

                        "board should be same" {
                            withBoardClue(board) {
                                board.board shouldBe IntArray(64) { 0 }
                            }
                        }

                        "occupancy should be same" {
                            withBoardClue(board) {
                                board.occupancy shouldBeBitboard 0UL
                            }
                        }

                        Color.values().map { color ->
                            "colorOccupancy should be same for $color" {
                                withBoardClue(board) {
                                    board.colorOccupancy[color.bits] shouldBeBitboard 0UL
                                }
                            }
                        }

                        "empty should be same" {
                            withBoardClue(board) {
                                board.empty shouldBeBitboard bitboard { allSquares() }
                            }
                        }

                        Piece.values().map { otherPiece ->
                            "pieceOccupancies should be same for $otherPiece" {
                                withBoardClue(board) {
                                    board.pieceOccupancies[otherPiece.bits] shouldBeBitboard 0UL
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    "should remove piece simple" {
        val board = ChessBoard()
        board[Square.A1] = Piece.WHITE_KING

        board[Square.A1] = null

        board.pieceOccupancies[Piece.WHITE_KING.bits] shouldBeBitboard 0UL
    }

    "can make an undo a move" {
//        val board = ChessBoard()
//        board[Square.A1] = Piece.WHITE_KING
//
//        val oldCastling = board.castling
//        val oldEnPassant = board.enPassant
//        val oldHalfMoveClock = board.halfMoveClock
//        val oldFullMoveNumber = board.fullMoveNumber
//        val oldColor = board.color
//
//        val oldBoard = board.board
//        val oldOccupancy = board.occupancy
//        val oldColorOccupancy = board.colorOccupancy.copyOf()
//        val oldPieceOccupancy = board.pieceOccupancies.copyOf()
//        val oldEmpty = board.empty
//
//        board.makeMove(Move(Square.A1, Square.A2, Piece.WHITE_KING).bits)
//        board.undoMove()
//
//        assertSoftly {
//            board.castling shouldBe oldCastling
//            board.enPassant shouldBe oldEnPassant
//            board.halfMoveClock shouldBe oldHalfMoveClock
//            board.fullMoveNumber shouldBe oldFullMoveNumber
//            board.color shouldBe oldColor
//
//            board.board.toList() shouldBe oldBoard.toList()
//            board.occupancy shouldBe oldOccupancy
//            board.colorOccupancy.toList() shouldBe oldColorOccupancy.toList()
//            board.pieceOccupancies.toList() shouldBe oldPieceOccupancy.toList()
//            board.empty shouldBe oldEmpty
//        }
    }
})
