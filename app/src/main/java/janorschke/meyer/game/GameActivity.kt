package janorschke.meyer.game

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import janorschke.meyer.databinding.ActivityGameBinding
import janorschke.meyer.global.TransferKeys
import janorschke.meyer.home.MainActivity

const val LOG_TAG = "GameActivity"

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gameModeStr = intent.extras?.getString(TransferKeys.GAME_MODE.value)
        if (gameModeStr == null) {
            startActivity(Intent(this, MainActivity::class.java))
            Log.e(LOG_TAG, "Invalid game mode")
        } else {
            val gameMode = GameMode.valueOf(gameModeStr)
            // TODO get game mode specific things, aiLevel ....
        }
    }
}