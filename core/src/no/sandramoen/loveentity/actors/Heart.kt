package no.sandramoen.loveentity.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Align
import no.sandramoen.loveentity.utils.BaseActor
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.BigNumber

class Heart(x: Float, y: Float, s: Stage) : BaseActor(x, y, s) {
    init {
        loadAnimation(BaseGame.textureAtlas!!.findRegion("heart"))
        width *= Gdx.graphics.width * .0065f
        height *= Gdx.graphics.width * .0065f
        setOrigin(Align.center)

        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(1))
                addAction(Actions.scaleTo(1.4f, 1.4f, .25f, Interpolation.pow2Out))
                addAction(Actions.delay(.25f))
                addAction(Actions.scaleTo(1.0f, 1.0f, .5f, Interpolation.fade))
                return true
            }
        })
    }
}
