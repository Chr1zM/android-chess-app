package janorschke.meyer.game.board

import janorschke.meyer.game.piece.model.Bishop
import janorschke.meyer.game.piece.model.King
import janorschke.meyer.game.piece.model.Knight
import janorschke.meyer.game.piece.model.Pawn
import janorschke.meyer.game.piece.model.Piece
import janorschke.meyer.game.piece.PieceColor
import janorschke.meyer.game.piece.PiecePosition
import janorschke.meyer.game.piece.model.Queen
import janorschke.meyer.game.piece.model.Rook
import java.lang.IllegalStateException

class Board {
    companion object {
        const val SIZE = 64
        const val LINE_SIZE = 8
    }

    private lateinit var fields: Array<Array<Piece?>>

    init {
        reset()
    }

    /**
     * @return the whole chess board
     */
    fun getFields(): Array<Array<Piece?>> {
        return fields
    }

    /**
     * @param position target
     * @return piece on the target
     */
    fun getField(position: PiecePosition): Piece? {
        return fields[position.row][position.col]
    }

    /**
     * Moves an piece to another position
     *
     * @param from source position
     * @param to target position
     * @return board move
     */
    fun createBoardMove(from: PiecePosition, to: PiecePosition): BoardMove {
        // TODO https://github.com/MadMax2506/android-wahlmodul-project/issues/23
        val fromPiece = getField(from)
        val toPiece = getField(to)

        setField(from, null)
        setField(to, fromPiece)

        return BoardMove(fields.clone(), from, to, fromPiece!!, toPiece)
    }

    private fun setField(position: PiecePosition, piece: Piece?) {
        fields[position.row][position.col] = piece
    }

    /**
     * Resets the board to the initial state
     */
    fun reset() {
        fields = Array(LINE_SIZE) { Array(LINE_SIZE) { null } }
        fields[0] = generateBaseLine(PieceColor.BLACK)
        fields[1] = generatePawnLine(PieceColor.BLACK)
        fields[6] = generatePawnLine(PieceColor.WHITE)
        fields[7] = generateBaseLine(PieceColor.WHITE)
    }

    /**
     * @param color of the piece
     * @return the pawn line
     */
    private fun generatePawnLine(color: PieceColor): Array<Piece?> {
        return Array(LINE_SIZE) { Pawn(this, color) }
    }

    /**
     * @param color of the piece
     * @return the base line of pieces
     */
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

    /**
     * Searches for the King on the board with the given color
     * @param color of the piece
     * @return position of the King
     */
    fun findKingPosition(color: PieceColor): PiecePosition =
            fields.flatMap { it.toList() }.filter { it is King && it.color == color }
                    .withIndex().map { PiecePosition(it.index) }.firstOrNull()
                    ?: throw IllegalStateException("King with color $color could not be found!")

    /**
     * @return true, if King is in check
     */
    fun isKingInCheck(color: PieceColor): Boolean {
        val kingPosition = this.findKingPosition(color)

        return fields.flatten().filterNotNull().withIndex().filter {
            it.value.possibleMoves(PiecePosition(it.index)).contains(kingPosition)
        }.toList().isNotEmpty()
    }

    /**
     * @return true if the King of the given color is in checkmate, false otherwise
     */
    fun isKingCheckmate(color: PieceColor): Boolean {
        if (!isKingInCheck(color)) {
            return false
        }
        // check if any piece can go somewhere, that is not checkmate
        fields.flatten().filterNotNull().withIndex().forEach {piece ->
            if (piece.value.color == color) {
                val moves = piece.value.possibleMoves(PiecePosition(piece.index))
                for (move in moves) {
                    val boardCopy = fields.copyOf()
                    val movePiece = boardCopy[piece.index / LINE_SIZE][piece.index % LINE_SIZE]
                    boardCopy[piece.index / LINE_SIZE][piece.index % LINE_SIZE] = null
                    boardCopy[move.row][move.col] = movePiece

                    if (!isKingInCheck(color)) {
                        // if King is not in check after this move, then it's not checkmate
                        return false
                    }
                }
            }
        }
        return true
    }

}