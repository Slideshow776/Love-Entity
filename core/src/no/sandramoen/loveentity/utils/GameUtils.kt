package no.sandramoen.loveentity.utils

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import java.util.*
import java.util.concurrent.TimeUnit


class GameUtils {
    companion object {
        fun isTouchDownEvent(e: Event): Boolean { // Custom type checker
            return e is InputEvent && e.type == InputEvent.Type.touchDown
        }

        fun saveGameState() {
            BaseGame.prefs!!.putFloat("love", BaseGame.love)
            BaseGame.prefs!!.putLong("lastTimePlayed", Date().time)
            BaseGame.prefs!!.flush()
        }
    }
}
