package janorschke.meyer.view.listener

import android.app.Activity
import android.app.AlertDialog
import android.view.MenuItem
import janorschke.meyer.R

class GameSurrenderOnClickListener(private val activity: Activity) : MenuItem.OnMenuItemClickListener {
    override fun onMenuItemClick(item: MenuItem): Boolean {
        showConfirmationDialog()
        return true
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(activity)
                .setTitle(R.string.dialog_surrender_title)
                .setMessage(R.string.dialog_surrender_message)
                .setPositiveButton(R.string.yes) { dialog, _ ->
                    dialog.dismiss()
                    // TODO: Klick auf "Ja"
                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }
}
