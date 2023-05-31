package janorschke.meyer.service.model.game

import android.util.Log
import janorschke.meyer.enums.PieceColor
import janorschke.meyer.service.model.game.board.Board
import janorschke.meyer.service.model.game.board.History
import janorschke.meyer.service.model.game.board.Move
import janorschke.meyer.service.model.game.piece.King
import janorschke.meyer.service.utils.piece.PieceSequence
import janorschke.meyer.service.validator.BoardValidator
import kotlin.system.measureTimeMillis

const val LOG_TAG = "AiEvaluationNode"

/**
 * TODO https://github.com/MadMax2506/android-wahlmodul-project/issues/107
 */
class AiEvaluationNode(val history: History, val move: Move?, private val aiColor: PieceColor) {
    val valency: Int

    val requiredMove get() = move!!
    val color get() = requiredMove.fromPiece.color
    private val valencyFactor get() = if (aiColor == PieceColor.WHITE) 1 else -1

    init {
        val time = measureTimeMillis {
            this.valency = if (move == null) {
                // Neutral starting position on the board
                0
            } else {
                // Calculates valency of the current position
                valencyFactor * Board(move.fieldsAfterMoving).let { boardCopy ->
                    history.push(move)

                    if (BoardValidator.isKingCheckmate(boardCopy, color.opponent())) Int.MAX_VALUE
                    if (BoardValidator.isStalemate(boardCopy, history, color.opponent())) Int.MIN_VALUE
                    getPieceValue(boardCopy, PieceColor.WHITE) - getPieceValue(boardCopy, PieceColor.BLACK)
                }
            }
        }
        Log.d(LOG_TAG, "Calculate valency in ${time}ms of")
    }

    /**
     * @param board instance
     * @param color of the related pieces
     * @return the valence of all pieces by the given color
     */
    private fun getPieceValue(board: Board, color: PieceColor): Int {
        return PieceSequence.allPiecesByColor(board, color)
                .filter { indexedPiece -> indexedPiece.piece !is King }
                .sumOf { indexedPiece -> indexedPiece.piece.pieceInfo.valence }
    }
}