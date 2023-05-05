package janorschke.meyer.game.piece

import janorschke.meyer.game.board.Board

class Queen(board: Board, color: PieceColor) : LineMovingPiece(board, color, PieceInfo.QUEEN) {
    override fun possibleMoves(position: PiecePosition): MutableList<PiecePosition> {
        return possibleMovesOnDiagonalLine(position).plus(possibleMovesOnStraightLine(position)).toMutableList()
    }
}