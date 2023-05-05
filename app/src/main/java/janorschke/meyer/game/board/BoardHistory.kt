package janorschke.meyer.game.board

import janorschke.meyer.game.piece.model.Piece

/**
 * Provides the history of all board moves and the beaten pieces by a move
 */
class BoardHistory {
    private val beatenPieces: ArrayDeque<Piece> = ArrayDeque(listOf())
    private val history: ArrayDeque<BoardMove> = ArrayDeque(listOf())

    /**
     * Add a new move to the history
     *
     * @param move of a piece
     */
    fun push(move: BoardMove) {
        if (move.toPiece != null) beatenPieces.add(move.toPiece)
        history.add(move)
    }

    /**
     * @return the last move and remove it from the history
     */
    fun undo(): BoardMove {
        beatenPieces.removeLast()
        return history.removeLast()
    }

    /**
     * Resets the board to the initial state
     */
    fun reset() {
        beatenPieces.clear()
        history.clear()
    }
}