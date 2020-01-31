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
import java.math.BigInteger

class Heart(x: Float, y: Float, s: Stage) : BaseActor(x, y, s) {
    val income = 1L
    private var bonusTime = 0f
    private var cpsTime = 0f
    var clicks = 0
    var disabled = false

    init {
        loadAnimation(BaseGame.textureAtlas!!.findRegion("heart"))
        width *= Gdx.graphics.width * .0065f
        height *= Gdx.graphics.width * .0065f
        setOrigin(Align.center)

        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                if (!disabled) {
                    clicks++

                    if (BaseGame.currentAscensionPoints > 0) {
                        BaseGame.love = BaseGame.love.add(BigInteger((income * (BaseGame.currentAscensionPoints * BaseGame.ascensionBonus) * BaseGame.heartBonus).toString()))
                        BaseGame.lifeTimeLove = BaseGame.lifeTimeLove.add(BigInteger((income * (BaseGame.currentAscensionPoints * BaseGame.ascensionBonus) * BaseGame.heartBonus).toString()))
                    } else {
                        BaseGame.love = BaseGame.love.add(BigInteger((income * BaseGame.heartBonus).toString()))
                        BaseGame.lifeTimeLove = BaseGame.lifeTimeLove.add(BigInteger((income * BaseGame.heartBonus).toString()))
                    }

                    addAction(Actions.scaleTo(1.4f, 1.4f, .25f, Interpolation.pow2Out))
                    addAction(Actions.delay(.25f))
                    addAction(Actions.scaleTo(1.0f, 1.0f, .5f, Interpolation.fade))
                }
                return true
            }
        })
    }

    override fun act(dt: Float) {
        super.act(dt)
        bonusTime += dt
        cpsTime += dt
        if (cpsTime >= 5f) {
            val cps = clicks.toFloat() / cpsTime
            when {
                cps >= 9 && bonusTime >= 35 -> BaseGame.heartBonus = 5
                cps >= 7 && bonusTime >= 25 -> BaseGame.heartBonus = 4
                cps >= 5 && bonusTime >= 15 -> BaseGame.heartBonus = 3
                cps >= 3 -> BaseGame.heartBonus = 2
                else -> {
                    BaseGame.heartBonus = 1
                    bonusTime = 0f
                }
            }
            cpsTime = 0f
            clicks = 0
        }
    }
}
