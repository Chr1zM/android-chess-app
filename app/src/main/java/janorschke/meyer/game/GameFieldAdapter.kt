package janorschke.meyer.game

import android.content.Context
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import janorschke.meyer.R
import janorschke.meyer.databinding.GameFieldBinding
import janorschke.meyer.game.piece.Piece
import janorschke.meyer.game.piece.PieceColor
import janorschke.meyer.game.piece.PiecePosition


class GameFieldAdapter(
        private val context: Context,
        private val boardViewModel: BoardViewModel
) : BaseAdapter() {
    private class ViewHolder(val binding: GameFieldBinding, val view: View)

    override fun getCount(): Int {
        return BoardViewModel.BOARD_SIZE
    }

    override fun getItem(index: Int): Piece? {
        return boardViewModel.getField(PiecePosition(index))
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(index: Int, convertView: View?, parent: ViewGroup?): View {
        lateinit var holder: ViewHolder
        if (convertView == null) {
            val binding = GameFieldBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
            holder = ViewHolder(binding, binding.root)
            holder.view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val position = PiecePosition(index)
        holder.view.setBackgroundResource(getViewBackgroundColor(position))

        val piece = boardViewModel.getField(position)
        if (piece != null) {
            val drawable = ContextCompat.getDrawable(context, piece.pieceInfo.imageId)!!.mutate()

            if (piece.color == PieceColor.BLACK) {
                val NEGATIVE = floatArrayOf(
                        -1.0f, 0f, 0f, 0f, 255f,  // red
                        0f, -1.0f, 0f, 0f, 255f,  // green
                        0f, 0f, -1.0f, 0f, 255f,  // blue
                        0f, 0f, 0f, 1.0f, 0f // alpha
                )
                drawable.colorFilter = ColorMatrixColorFilter(NEGATIVE)
            }
            holder.binding.btn.background = drawable
        }
        holder.binding.btn.setOnClickListener { boardViewModel.onFieldClicked(position) }

        return holder.view
    }

    private fun getViewBackgroundColor(position: PiecePosition): Int {
        return if (position.row % 2 != position.col % 2) R.color.brown else R.color.beige
    }
}
