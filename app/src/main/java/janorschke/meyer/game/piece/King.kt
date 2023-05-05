package janorschke.meyer.game.piece

import janorschke.meyer.R
import janorschke.meyer.game.board.Board

class King(board: Board, color: PieceColor) : Piece(board, color) {
    override fun getImageId(): Int {
        return R.drawable.chess_king
    }

    override fun possibleMoves(position: PiecePosition): MutableCollection<PiecePosition> {
        val possibleMoves = mutableListOf<PiecePosition>()
        for (i in -1..1) {
            for (j in -1..1) {
                val currentPosition = PiecePosition(position.row + i, position.col + j)
                if (isFieldUnavailable(currentPosition)) continue
                // TODO Steht der König im Schach oder ist die Figur gefesselt
                possibleMoves.add(currentPosition)
            }
        }
        return possibleMoves
    }

    override fun isFieldUnavailable(position: PiecePosition): Boolean {
        return !fieldValidation.isInBound(position) || fieldValidation.isTeammate(position)
    }
}