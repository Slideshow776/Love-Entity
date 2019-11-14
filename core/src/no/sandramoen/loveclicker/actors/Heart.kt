package no.sandramoen.loveclicker.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Align
import no.sandramoen.loveclicker.utils.BaseActor

class Heart(x: Float, y: Float, s: Stage) : BaseActor(x, y, s) {
    init {
        loadTexture("images/heart.png")
        width *= 3
        height *= 3
        centerAtPosition(Gdx.graphics.width / 2f, Gdx.graphics.height / 2f)
        setOrigin(Align.center)
    }

    fun clicked(mouseX: Int, mouseY: Int): Boolean {
        var mouseY = (Gdx.graphics.height - mouseY) // inverts the y position to start from the bottom of the screen
        val clickedX = mouseX >= x && mouseX <= x + width
        val clickedY = mouseY >= x && mouseY <= y + height

        if (clickedX && clickedY) {
            addAction(Actions.scaleTo(1.4f, 1.4f, .25f, Interpolation.pow2Out))
            addAction(Actions.delay(.25f))
            addAction(Actions.scaleTo(1.0f, 1.0f, .5f, Interpolation.fade))
            return true
        }
        return false
    }
}
