package janorschke.meyer.service.utils

import janorschke.meyer.enums.PieceColor
import janorschke.meyer.enums.PieceType
import janorschke.meyer.service.model.game.ai.AiEvaluationNode
import janorschke.meyer.service.model.game.ai.AiEvaluationNodeFactory
import janorschke.meyer.service.model.game.board.Board
import janorschke.meyer.service.model.game.board.History
import janorschke.meyer.service.model.game.board.Move
import janorschke.meyer.service.model.game.piece.King
import janorschke.meyer.service.model.game.piece.Knight
import janorschke.meyer.service.model.game.piece.Pawn
import janorschke.meyer.service.model.game.piece.lineMoving.Bishop
import janorschke.meyer.service.model.game.piece.lineMoving.Queen
import janorschke.meyer.service.model.game.piece.lineMoving.Rook
import janorschke.meyer.service.utils.piece.PieceSequence
import janorschke.meyer.service.validator.BoardValidator
import janorschke.meyer.service.validator.FieldValidator

object AiEvaluationTreeGenerator {
    /**
     * TODO https://github.com/MadMax2506/android-wahlmodul-project/issues/107
     */
    fun generateChildren(parent: AiEvaluationNode, board: Board, aiColor: PieceColor): MutableList<AiEvaluationNode> {
        val numberOfMove = parent.history.numberOfMoves
        var pieceSequence = PieceSequence.allPiecesByColor(board, aiColor)

        // Do the first move with a pawn
        if (numberOfMove <= 2) pieceSequence = pieceSequence.filter { it.piece is Pawn }

        return pieceSequence
                // Create a flatten list of moves for each possible move
                .map { piece -> generateMovesForPiece(board, piece) }
                .flatten()
                // Create a evaluation for each move
                .map { move -> prioritizePieces(parent, move, aiColor, numberOfMove) }
                .toMutableList()
    }

    /**
     * Prioritize the pieces in the opening and end game
     *
     * @param parent node
     * @param move on the board
     * @param aiColor
     * @param numberOfMove which are currently done
     * @return prioritized evaluation node
     */
    private fun prioritizePieces(
            parent: AiEvaluationNode,
            move: Move,
            aiColor: PieceColor,
            numberOfMove: Int
    ): AiEvaluationNode {
        val history = parent.history
        val factory = AiEvaluationNodeFactory(history, move, aiColor)

        return when {
            // TODO https://de.wikipedia.org/wiki/Endspiel_(Schach)
            // Endgame

            // First move
            numberOfMove <= 2 -> if (FieldValidator.isCenter(move.to)) factory.create(1) else factory.create()

            // Opening
            numberOfMove <= 30 -> openingPiecePrioritization(factory, move, history, aiColor)

            else -> factory.create()
        }
    }

    /**
     * **PRIORITY**
     * - 3: Castling
     * - 2: Pawn in the extended center | pressure on the center
     * - 1: Light piece in the extended center
     * - 0: Default
     * - -1: Move same piece
     * - -2: Heavy piece move
     * - -3: King move
     *
     * @param factory to create a evaluation move
     * @param move of a piece
     * @param history instance
     * @param aiColor
     * @return prioritized evaluation node
     */
    private fun openingPiecePrioritization(
            factory: AiEvaluationNodeFactory,
            move: Move,
            history: History,
            aiColor: PieceColor
    ): AiEvaluationNode {
        return when {
            // TODO Rochade bevorzugen: https://github.com/MadMax2506/android-wahlmodul-project/issues/115
            // TODO priority = 3
            // Castling

            // Move the king
            move.fromPiece is King -> factory.create(-3)

            // Move a heavy piece
            move.fromPiece.pieceInfo.type == PieceType.HEAVY -> factory.create(-2)

            // Move a piece multiple times
            history.getLastMoves(history.numberOfMoves.coerceAtMost(6))
                    .filter { it.fromPiece.color == aiColor }
                    .none { move.from == it.to } -> {
                factory.create(-1)
            }

            // TODO Druck Zentrum priorisieren
            // TODO priority = 2
            // Pressure on the center

            // Place pieces in the extended center
            FieldValidator.isExtendedCenter(move.to) -> if (move.fromPiece is Pawn) factory.create(2) else factory.create(1)

            else -> factory.create()
        }
    }

    /**
     * @param board instance
     * @param indexedPiece which is on the board
     * @return all possible moves of the given piece
     */
    private fun generateMovesForPiece(board: Board, indexedPiece: PieceSequence.IndexedPiece): Sequence<Move> {
        return indexedPiece.piece
                .possibleMoves(board, indexedPiece.position)
                .asSequence()
                .map { possibleMove ->
                    // Create moves
                    if (BoardValidator.isPawnTransformation(indexedPiece.piece, possibleMove)) {
                        // Special case for the pawn transformation
                        val color = indexedPiece.piece.color
                        arrayOf(Knight(color), Bishop(color), Rook(color), Queen(color)).map { piece ->
                            Board(board).createMove(indexedPiece.position, possibleMove, piece)
                        }.toMutableList()
                    } else {
                        // Normal move
                        mutableListOf(Board(board).createMove(indexedPiece.position, possibleMove))
                    }
                }
                .flatten()
    }
}