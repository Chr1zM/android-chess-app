package janorschke.meyer.service.model.game.player

import janorschke.meyer.enums.PieceColor
import java.io.Serializable

open class Player(val color: PieceColor, val name: String, var remainingTime: Long?) : Serializable {
    var requiredRemainingTime: Long
        get() = remainingTime!!
        set(time) {
            this.remainingTime = time
        }
}