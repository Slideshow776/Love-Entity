package no.sandramoen.loveentity.utils

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import java.util.Date

class GameUtils {
    companion object {
        fun isTouchDownEvent(e: Event): Boolean { // Custom type checker
            return e is InputEvent && e.type == InputEvent.Type.touchDown
        }

        fun saveGameState() {
            putBigNumber("love", BaseGame.love)
            BaseGame.prefs!!.putLong("lastTimePlayed", Date().time)
            BaseGame.prefs!!.flush()
        }

        fun getBigNumber(value: String): BigNumber {
            val bigNumber = BigNumber(0)
            var i = 0
            while (BaseGame.prefs!!.contains("$value-$i")) {
                bigNumber.maxNumber.add(BaseGame.prefs!!.getInteger("$value-$i"))
                i++
            }
            bigNumber.maxNumber.removeIndex(0) // removes the initialized value
            return bigNumber
        }

        private fun putBigNumber(value: String, bigNumber: BigNumber) {
            var i = 0
            while (BaseGame.prefs!!.contains("$value-$i")) { // clear value in case of contamination
                BaseGame.prefs!!.remove("$value-$i")
                i++
            }

            for (i in 0 until bigNumber.maxNumber.size) {
                BaseGame.prefs!!.putInteger("$value-$i", bigNumber.maxNumber[i])
            }
        }
    }
}
