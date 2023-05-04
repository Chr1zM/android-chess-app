package janorschke.meyer.game.piece

import janorschke.meyer.game.BoardViewModel

abstract class Piece(val boardViewModel: BoardViewModel, val color: PieceColor) {
    /**
     * @return the image id for the piece
     */
    abstract fun getImageId(): Int

    /**
     * @param position current position
     * @return possible moves
     */
    abstract fun possibleMoves(position: PiecePosition): MutableCollection<PiecePosition>
}