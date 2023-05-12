package janorschke.meyer.game.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import janorschke.meyer.databinding.MoveHistoryFieldBinding
import janorschke.meyer.game.GameViewModel
import janorschke.meyer.game.board.BoardMove
import janorschke.meyer.game.piece.model.Pawn

/**
 * Adapter for the move history
 */
class MoveHistoryAdapter(private val context: Context, private val gameViewModel: GameViewModel) : BaseAdapter() {
    private data class ViewHolder(val binding: MoveHistoryFieldBinding, val view: View)

    init {
        gameViewModel.setMoveHistoryAdapter(this)
    }

    override fun getCount(): Int = gameViewModel.numberOfMoves()

    override fun getItem(index: Int): BoardMove = gameViewModel.getMove(index)

    override fun getItemId(index: Int): Long = index.toLong()

    override fun getView(index: Int, convertView: View?, parent: ViewGroup?): View {
        lateinit var holder: ViewHolder
        if (convertView == null) {
            val binding = MoveHistoryFieldBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
            holder = ViewHolder(binding, binding.root)
            holder.view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.binding.notation.text = getMoveNotation(getItem(index))

        return holder.view
    }

    /**
     * @param move in the history
     * @return a valid notion for the move
     */
    private fun getMoveNotation(move: BoardMove): String {
        StringBuilder()
                .apply {
                    this.append(context.resources.getString(move.fromPiece.pieceInfo.notationId))
                    if (move.toPiece != null) {
                        if (move.fromPiece is Pawn) this.append(move.from.getColNotation())
                        this.append("x")
                    }
                    this.append(move.to.getNotation())

                    return this.toString()
                }
    }
}