package janorschke.meyer.view.adapter.beatenPieces

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import janorschke.meyer.databinding.BeatenPieceBinding
import janorschke.meyer.service.model.game.piece.Piece
import janorschke.meyer.service.utils.piece.PieceDrawables

/**
 * Display beaten pieces
 *
 * @param context of the application
 */
class BeatenPiecesAdapter(private val context: Context) : RecyclerView.Adapter<BeatenPiecesAdapter.ViewHolder>() {
    data class ViewHolder(val binding: BeatenPieceBinding) : RecyclerView.ViewHolder(binding.root)

    private var beatenPieces: MutableList<Piece> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setBeatenPieces(beatenPieces: MutableList<Piece>) {
        this.beatenPieces = beatenPieces
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        BeatenPieceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                .let { binding -> return ViewHolder(binding) }
    }

    override fun getItemCount(): Int = beatenPieces.size

    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        PieceDrawables.getPiece(context, beatenPieces[index])
                .let { drawable -> holder.binding.beatenPiece.setImageDrawable(drawable) }
    }
}