package janorschke.meyer.service.repository.ai

import android.util.Log
import janorschke.meyer.enums.AiLevel
import janorschke.meyer.enums.PieceColor
import janorschke.meyer.service.model.game.board.Board
import janorschke.meyer.service.model.game.board.History
import janorschke.meyer.service.model.game.board.Move
import janorschke.meyer.service.utils.piece.PieceSequence

private const val LOG_TAG = "AiLevelTwoRepository"

/**
 * Represents a medium ai
 * @see AiLevel.MAX
 */
class AiLevelMediumRepository(color: PieceColor, board: Board, history: History) : AiRepository(color, board, AiLevel.MAX, history) {
    override fun calculateNextMove(): Move {
        Board(board).let { boardCopy ->
            // TODO
            // val boardEvaluation = super.evaluateBoard(this)

            Log.d(LOG_TAG, "Calculate the next move for $aiLevel")

            // TODO temporary code part
            val temp = PieceSequence.allPiecesByColor(boardCopy, color)
                    .filter { it.piece.possibleMoves(boardCopy, it.position, history).isNotEmpty() }
                    .map { Pair(it.position, it.piece.possibleMoves(boardCopy, it.position, history).first()) }
                    .first()
            return boardCopy.createBoardMove(temp.first, temp.second)
        }
    }
}