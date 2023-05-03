package janorschke.meyer.game

import androidx.lifecycle.ViewModel
import janorschke.meyer.game.piece.Bishop
import janorschke.meyer.game.piece.King
import janorschke.meyer.game.piece.Knight
import janorschke.meyer.game.piece.Pawn
import janorschke.meyer.game.piece.Piece
import janorschke.meyer.game.piece.PieceColor
import janorschke.meyer.game.piece.PiecePosition
import janorschke.meyer.game.piece.Queen
import janorschke.meyer.game.piece.Rook

class BoardViewModel : ViewModel() {
    private lateinit var board: Array<Array<Piece?>>

    companion object {
        const val BOARD_SIZE = 64
        const val ROW_SIZE = 8
    }

    init {
        resetBoard()
    }

    fun getBoard(): Array<Array<Piece?>> {
        return this.board
    }

    fun movePiece(from: PiecePosition, to: PiecePosition) {
        TODO("Move piece")
    }

    fun getField(position: PiecePosition): Piece? {
        return board[position.row][position.col]
    }

    fun onFieldClicked(position: PiecePosition) {
        TODO("Validierung, ob ein eigenes Piece angeklickt => in einen State behalten und beim nächsten klick schauen, ob der Move valide ist")
    }

    private fun resetBoard() {
        board = Array(8) { Array(8) { null } }
        board[0] = generateBaseLine(PieceColor.BLACK)
        board[1] = generatePawnLine(PieceColor.BLACK)
        board[6] = generatePawnLine(PieceColor.WHITE)
        board[7] = generateBaseLine(PieceColor.WHITE)
    }

    private fun generatePawnLine(color: PieceColor): Array<Piece?> {
        return Array(ROW_SIZE) { Pawn(this, color) }
    }

    private fun generateBaseLine(color: PieceColor): Array<Piece?> {
        return arrayOf(
            Rook(this, color),
            Knight(this, color),
            Bishop(this, color),
            Queen(this, color),
            King(this, color),
            Bishop(this, color),
            Knight(this, color),
            Rook(this, color)
        )
    }
}
