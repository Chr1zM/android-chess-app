package janorschke.meyer.service.repository

import android.util.Log
import janorschke.meyer.service.model.game.Game
import janorschke.meyer.service.model.game.board.Board
import janorschke.meyer.service.model.game.board.History
import janorschke.meyer.service.model.game.board.Position
import janorschke.meyer.service.model.game.board.move.Move
import janorschke.meyer.service.model.game.board.move.PossibleMove
import janorschke.meyer.service.model.game.piece.Piece
import janorschke.meyer.service.repository.game.GameRepository
import janorschke.meyer.service.repository.player.PlayerRepository
import janorschke.meyer.service.validator.BoardValidator.isPawnTransformation
import janorschke.meyer.view.callback.BoardRepositoryCallback
import janorschke.meyer.viewModel.GameViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val LOG_TAG = "BoardRepository"

class BoardRepository(
        private val board: Board,
        private val history: History,
        private val game: Game,
        private val gameRepository: GameRepository,
        private val playerRepository: PlayerRepository
) {
    private lateinit var callback: BoardRepositoryCallback

    fun tryToMovePiece(position: Position) {
        val possibleMove = game.getPossibleMoves().firstOrNull { it.to.position == position }
        tryToMovePiece(possibleMove, false)
    }

    /**
     * Moves a chess piece from the source position to the target position, if the target position is valid.
     *
     * @param possibleMove of the piece
     * @param isExternalMove if `true`, the [Move] was produced external - ai or network
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun tryToMovePiece(possibleMove: PossibleMove?, isExternalMove: Boolean) {
        // Check if requested position is a possible move of the piece
        if (possibleMove == null) {
            game.setSelectedPiece()
            return
        }

        val piece = possibleMove.from.requiredPiece

        // Move piece and reset selection
        // Open the PromotionDialog if Pawn goes to the last field
        if (isPawnTransformation(piece, possibleMove.to.position)) {
            callback.openPromotionDialog(
                    piece.color,
                    possibleMove
            )
        } else {
            movePiece(possibleMove)
        }
        game.setSelectedPiece()

        // Check if game is finished or
        if (gameRepository.checkEndOfGame(piece)) return

        gameRepository.handleMove()

        // move was done by the ai
        if (isExternalMove) return

        GlobalScope.launch {
            val aiMove = playerRepository.nextMove(board, history)

            withContext(Dispatchers.Main) {
                tryToMovePiece(aiMove, true)
                GameViewModel.getInstance().aiMoved()
            }
        }
    }

    /**
     * Moves a piece to the target position
     *
     * @param possibleMove from which the move is created to move the piece
     */
    fun movePiece(possibleMove: PossibleMove) {
        val move = createMove(possibleMove)
        history.push(move)

        if (move.beaten.piece != null) Log.d(LOG_TAG, "${possibleMove.from.position.getNotation()} beat piece on ${possibleMove.to.position.getNotation()}")
        else Log.d(LOG_TAG, "Move piece from ${possibleMove.from.position.getNotation()} to ${possibleMove.to.position.getNotation()}")
    }


    /**
     * Moves an piece to another position
     *
     * @param possibleMove from which the move is created
     * @return board move
     *
     * @see Board.createMove
     */
    private fun createMove(possibleMove: PossibleMove): Move = board.createMove(possibleMove)


    fun setCallback(callback: BoardRepositoryCallback) {
        this.callback = callback
    }
}