package janorschke.meyer.service.model.game.board

import janorschke.meyer.enums.PieceColor
import janorschke.meyer.service.model.game.piece.King
import janorschke.meyer.service.model.game.piece.Knight
import janorschke.meyer.service.model.game.piece.Pawn
import janorschke.meyer.service.model.game.piece.Piece
import janorschke.meyer.service.model.game.piece.lineMoving.Bishop
import janorschke.meyer.service.model.game.piece.lineMoving.Queen
import janorschke.meyer.service.model.game.piece.lineMoving.Rook
import janorschke.meyer.service.utils.board.PiecePosition
import janorschke.meyer.service.utils.piece.PieceSequence

/**
 * Chess board
 */
class Board {
    companion object {
        const val LINE_SIZE = 8
        const val SIZE = LINE_SIZE * LINE_SIZE

        /**
         * @param color of the piece
         * @return the pawn line
         */
        private fun generatePawnLine(color: PieceColor): Array<Piece?> = Array(LINE_SIZE) { Pawn(color) }

        /**
         * @param color of the piece
         * @return the base line of pieces
         */
        private fun generateBaseLine(color: PieceColor): Array<Piece?> {
            return arrayOf(
                    Rook(color),
                    Knight(color),
                    Bishop(color),
                    Queen(color),
                    King(color),
                    Bishop(color),
                    Knight(color),
                    Rook(color)
            )
        }
    }

    private var fields: Array<Array<Piece?>>

    constructor() {
        fields = Array(LINE_SIZE) { Array(LINE_SIZE) { null } }
        fields[0] = generateBaseLine(PieceColor.BLACK)
        fields[1] = generatePawnLine(PieceColor.BLACK)
        fields[6] = generatePawnLine(PieceColor.WHITE)
        fields[7] = generateBaseLine(PieceColor.WHITE)
    }

    // Copy Constructor
    constructor(board: Board) : this(board.getFields())
    constructor(fields: Array<Array<Piece?>>) {
        this.fields = fields.map { it.copyOf() }.toTypedArray()
    }

    /**
     * @return the whole chess board
     */
    fun getFields(): Array<Array<Piece?>> = fields

    /**
     * @param position target
     * @return piece on the target
     */
    fun getField(position: PiecePosition): Piece? = fields[position.row][position.col]

    fun setField(position: PiecePosition, piece: Piece?) {
        fields[position.row][position.col] = piece
    }

    /**
     * Searches for the King on the board with the given color
     * @param color of the piece
     * @return position of the King
     */
    fun findKingPosition(color: PieceColor): PiecePosition? {
        return PieceSequence.allPiecesByColor(fields, color)
                .filter { it.piece is King }
                .map { it.position }
                .firstOrNull()
    }

    /**
     * @param color of the pieces
     * @return pawn difference of the pieces by the given color
     */
    fun getPawnDifferenceByColor(color: PieceColor): Int {
        val valanceOfOpponentPieces = PieceSequence.allPiecesByColor(fields, color.opponent()).sumOf { it.piece.pieceInfo.valence }
        val valenceOfOwnPieces = PieceSequence.allPiecesByColor(fields, color).sumOf { it.piece.pieceInfo.valence }

        return valenceOfOwnPieces - valanceOfOpponentPieces
    }

    /**
     * Moves an piece to another position
     *
     * @param from source position
     * @param to target position
     * @return board move
     */
    fun createBoardMove(from: PiecePosition, to: PiecePosition): Move {
        val fromPiece = getField(from)!!
        val toPiece = getField(to)

        setField(from, null)
        if (fromPiece is Pawn && to.row == fromPiece.color.opponent().borderlineIndex) {
            setField(to, Queen(fromPiece.color))
        } else {
            setField(to, fromPiece)
        }

        return Move(fields.map { it.copyOf() }.toTypedArray(), from, to, fromPiece, toPiece)
    }
}