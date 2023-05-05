package janorschke.meyer.game.piece

import janorschke.meyer.game.BoardViewModel

class Queen(boardViewModel: BoardViewModel, color: PieceColor) : LineMovingPiece(boardViewModel, color, PieceInfo.QUEEN) {
    override fun possibleMoves(position: PiecePosition): MutableList<PiecePosition> {
        TODO("Not yet implemented")
    }
}