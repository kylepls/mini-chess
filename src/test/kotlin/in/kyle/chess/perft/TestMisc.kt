package `in`.kyle.chess.perft

import `in`.kyle.chess.debug.Fen
import `in`.kyle.chess.model.Encoding.DOUBLE_PAWN_PUSH
import `in`.kyle.chess.model.Encoding.EN_PASSANT_CAPTURE
import `in`.kyle.chess.model.Move
import `in`.kyle.chess.model.Piece.*
import `in`.kyle.chess.model.Square.*
import `in`.kyle.chess.model.getHumanMoves
import com.github.ajalt.colormath.RGB.Companion.fromInt
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class TestMisc : FreeSpec({
    "rnbqkbnr/p1pppppp/8/1N6/8/8/PPPPPPPP/R1BQKBNR b KQkq - 3 3" {
        val board = Fen.toBoard("rnbqkbnr/p1pppppp/8/1N6/8/8/PPPPPPPP/R1BQKBNR b KQkq - 3 3")
        board.getMoves().size shouldBe 20
    }

    "idk" {
        val board = Fen.toBoard(Fen.STARTING_POSITION)
        val sent = listOf(71176, 35385, 6168, 96369)//, 268896)
        sent.forEach { board.makeMove(it) }

        val humanSent = sent.map { Move(it) }
        println(humanSent)

        val moves = board.getHumanMoves().sortedBy { it.toString() }
        println(moves)
    }
})
