package janorschke.meyer.game.piece

import janorschke.meyer.game.BoardViewModel

/**
 * Utils class to validate a field on the board
 */
class FieldValidation(private val piece: Piece, private val boardViewModel: BoardViewModel) {
    /**
     * @param position being checked
     * @return true, if position contains an piece of the opponent team
     * @throws IndexOutOfBoundsException if position is invalid
     */
    fun isOpponent(position: PiecePosition): Boolean {
        return !isEmpty(position) && !isTeammate(position)
    }

    /**
     * @param position being checked
     * @return true, if position contains an piece of the own team.
     * @throws IndexOutOfBoundsException if position is invalid
     */
    fun isTeammate(position: PiecePosition): Boolean {
        if (!isInBound(position)) throw IndexOutOfBoundsException("Position is not on the board")

        return boardViewModel.getBoard()[position.row][position.col]?.color == piece.color
    }

    /**
     * @param position being checked
     * @return true, if no piece is on the given position
     * @throws IndexOutOfBoundsException if position is invalid
     */
    fun isEmpty(position: PiecePosition): Boolean {
        if (!isInBound(position)) throw IndexOutOfBoundsException("Position is not on the board")

        return boardViewModel.getBoard()[position.row][position.col] == null
    }

    /**
     * @param position being checked
     * @return true, if position is on the board
     */
    fun isInBound(position: PiecePosition): Boolean {
        val indices = boardViewModel.getBoard().indices
        return position.row in indices && position.col in indices
    }
}