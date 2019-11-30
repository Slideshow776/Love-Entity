package no.sandramoen.loveentity.utils

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent

class GameUtils {
    companion object {
        // Custom type checker
        fun isTouchDownEvent(e: Event): Boolean {
            return e is InputEvent && e.type == InputEvent.Type.touchDown
        }
    }
}
