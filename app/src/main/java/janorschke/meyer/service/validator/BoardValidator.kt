package janorschke.meyer.service.validator

import janorschke.meyer.enums.PieceColor
import janorschke.meyer.service.model.game.board.Board
import janorschke.meyer.service.model.game.board.History
import janorschke.meyer.service.model.game.board.Move
import janorschke.meyer.service.model.game.board.PiecePosition
import janorschke.meyer.service.model.game.piece.King
import janorschke.meyer.service.model.game.piece.Pawn
import janorschke.meyer.service.model.game.piece.Piece
import janorschke.meyer.service.utils.piece.PieceSequence

object BoardValidator {
    private const val N_MOVE_REPETITIONS_FOR_STALEMATE = 10
    private const val N_MOVES_FOR_THE_FIRST_MOVE = 2
    private const val N_MOVES_FOR_THE_OPENING = 30

    /**
     * @param history instance
     * @return `true`, if the first move of both players is done
     */
    fun isFirstMove(history: History) = history.numberOfMoves <= N_MOVES_FOR_THE_FIRST_MOVE

    /**
     * @param history instance
     * @return `true`, if the current game is in the opening
     */
    fun isOpening(history: History) = history.numberOfMoves <= N_MOVES_FOR_THE_OPENING

    /**
     * @param piece which is moving
     * @param to [PiecePosition] of the [Piece]
     * @return `true`, if the moved piece represents a pawn transformation
     */
    fun isPawnTransformation(piece: Piece, to: PiecePosition) = piece is Pawn && to.row == piece.color.opponent().borderlineIndex

    /**
     * @param board instance
     * @param color of the [King] that can be in check
     * @return `true`, if [King] is in check
     */
    fun isKingInCheck(board: Board, color: PieceColor): Boolean {
        val kingPosition = board.findKingPosition(color) ?: return true

        return PieceSequence.allPiecesByColor(board, color.opponent())
                .any { it.piece.givesOpponentKingCheck(board, it.position, kingPosition) }
    }

    /**
     * @param board instance
     * @param color of the [King] that can be in checkmate
     * @return `true`, if the [King] of the given color is in checkmate
     */
    fun isKingCheckmate(board: Board, color: PieceColor): Boolean {
        if (!isKingInCheck(board, color)) return false

        // check if any piece can go somewhere, that is not checkmate
        for (indexedPiece in PieceSequence.allPiecesByColor(board, color)) {
            for (move in indexedPiece.piece.possibleMoves(board, indexedPiece.position)) {
                Board(board).let { boardCopy ->
                    boardCopy.createMove(indexedPiece.position, move)
                    // if King is not in check after this move, then it's not checkmate
                    if (!isKingInCheck(boardCopy, color)) return false
                }
            }
        }
        return true
    }

    /**
     * @param board instance
     * @param history to check the move-repetition
     * @param color of the player who has the next turn
     * @return `true`, if the current [Board] with the remaining [Piece]s is stalemate
     */
    fun isStalemate(board: Board, history: History, color: PieceColor): Boolean {
        if (isKingInCheck(board, color)) return false

        val pieceSequence = PieceSequence.allPiecesByColor(board, color)
        val pieceSequenceOpponent = PieceSequence.allPiecesByColor(board, color.opponent())

        // check if no possible move for the given color is left
        pieceSequence.map { it.piece.possibleMoves(board, it.position) }
                .flatten()
                .toList()
                .isEmpty()
                .let { isEmpty -> if (isEmpty) return true }

        // check if both player have enough pieces
        if (!checkIfPlayerCanWin(pieceSequence) && !checkIfPlayerCanWin(pieceSequenceOpponent)) return true

        // check move-repetition
        if (N_MOVE_REPETITIONS_FOR_STALEMATE >= history.numberOfMoves) return false
        history.getLastMoves(N_MOVE_REPETITIONS_FOR_STALEMATE).let { moves ->
            return hasColorRepeatedMoves(moves, PieceColor.WHITE) && hasColorRepeatedMoves(moves, PieceColor.BLACK)
        }
    }

    /**
     * Checks if the remaining pieces of a colors can win the game
     *
     * @param pieceSequence contains all pieces of an color
     */
    private fun checkIfPlayerCanWin(pieceSequence: Sequence<PieceSequence.IndexedPiece>): Boolean {
        pieceSequence.map { it.piece }
                .filterNot { it is King }
                .toList()
                .let { pieces ->
                    if (pieces.isEmpty()) return false
                    if (pieces.size == 1 && pieces[0].pieceInfo.valence == 3) return false
                }
        return true
    }

    /**
     * @param moveHistory to check the move-repetition for the given color
     * @param color of the pieces that have possibly the repeated moves
     */
    private fun hasColorRepeatedMoves(moveHistory: List<Move>, color: PieceColor): Boolean {
        val filteredHistory = moveHistory.filter { it.fromPiece.color == color }
        for (indexedMove in filteredHistory.withIndex()) {
            if (indexedMove.index % 2 == 0 && filteredHistory[0] != indexedMove.value) return false
            else if (indexedMove.index % 2 == 1 && filteredHistory[1] != indexedMove.value) return false
        }
        return true
    }
}