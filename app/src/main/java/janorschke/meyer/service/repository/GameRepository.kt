package janorschke.meyer.service.repository

import android.util.Log
import janorschke.meyer.enums.GameStatus
import janorschke.meyer.service.model.game.Game
import janorschke.meyer.service.model.game.board.Board
import janorschke.meyer.service.model.game.board.History
import janorschke.meyer.service.model.game.piece.Piece
import janorschke.meyer.service.validator.BoardValidator

private const val LOG_TAG = "GameRepository"

class GameRepository(private val board: Board, private val history: History, private val game: Game) {
    /**
     * Check if the game is finished
     *
     * @param piece which has moved
     * @return true if the game is finished
     */
    fun checkEndOfGame(piece: Piece): Boolean {
        if (BoardValidator.isKingCheckmate(board, history, piece.color.opponent())) {
            Log.d(LOG_TAG, "setStatus CHECKMATE")
            game.setStatus(GameStatus.CHECKMATE)
            return true
        } else if (BoardValidator.isStalemate(board, history, piece.color.opponent())) {
            Log.d(LOG_TAG, "setStatus STALEMATE")
            game.setStatus(GameStatus.STALEMATE)
            return true
        }
        return false
    }
}