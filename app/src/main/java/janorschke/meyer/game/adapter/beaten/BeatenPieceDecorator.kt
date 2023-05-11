package janorschke.meyer.game.adapter.beaten

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class BeatenPieceDecorator : RecyclerView.ItemDecoration() {
    companion object {
        private const val SPACE = -60
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        if (position > 0 && parent.getChildAdapterPosition(view) != RecyclerView.NO_POSITION) {
            outRect.set(SPACE, 0, 0, 0)
        }
    }
}