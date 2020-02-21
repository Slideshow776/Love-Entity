package no.sandramoen.loveentity.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import no.sandramoen.loveentity.utils.BaseActor
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.GameUtils
import java.math.BigInteger

class Heart(x: Float, y: Float, s: Stage) : BaseActor(x, y, s) {
    val income = 1L
    private var bonusTime = 0f
    private var cpsTime = 0f
    var clicks = 0
    var disabled = false
    var time = 0f

    init {
        loadAnimation(BaseGame.textureAtlas!!.findRegion("heart"))
        width *= Gdx.graphics.width * .0023f
        height *= Gdx.graphics.width * .0025f
        setOrigin(Align.center)

        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                if (!disabled) {
                    clicks++
                    BaseGame.heartTouched = true

                    var loveEarned = BigInteger.ONE
                    if (BaseGame.currentAscensionPoints > 0) {
                        loveEarned = BigInteger((income * (BaseGame.currentAscensionPoints * BaseGame.ascensionBonus) * BaseGame.heartBonus).toString())
                        BaseGame.love = BaseGame.love.add(loveEarned)
                        BaseGame.lifeTimeLove = BaseGame.lifeTimeLove.add(loveEarned)
                    } else {
                        loveEarned = BigInteger((income * BaseGame.heartBonus).toString())
                        BaseGame.love = BaseGame.love.add(loveEarned)
                        BaseGame.lifeTimeLove = BaseGame.lifeTimeLove.add(loveEarned)
                    }

                    addAction(Actions.scaleTo(1.4f, 1.4f, .25f, Interpolation.pow2Out))
                    addAction(Actions.delay(.25f))
                    addAction(Actions.scaleTo(1.0f, 1.0f, .5f, Interpolation.fade))

                    createFloatingLabelAndRemove(x, y, loveEarned)
                    createAndStartEffect(x, y)
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

        time += dt
        if (time > .25f) {
            time = 0f
            BaseGame.heartTouched = false
        }
    }

    private fun createAndStartEffect(x: Float, y: Float) {
        val heartEffect = HeartEffect()
        heartEffect.setPosition(x, y)
        heartEffect.scaleBy(2f)
        addActor(heartEffect)
        heartEffect.start()
    }

    private fun createFloatingLabelAndRemove(x: Float, y: Float, loveEarned: BigInteger) {
        val temp = Label("", BaseGame.labelStyle)
        if (BaseGame.longScale)
            temp.setText("+${GameUtils.presentLongScale(loveEarned)}")
        else
            temp.setText("+${GameUtils.presentShortScale(loveEarned)}")
        temp.setFontScale(.6f)
        temp.x = MathUtils.random(x - Gdx.graphics.width * .01f, x + Gdx.graphics.width * .01f)
        temp.y = y
        addActor(temp)
        temp.addAction(Actions.moveBy(0f, Gdx.graphics.height * .2f, 5f))
        temp.addAction(Actions.fadeOut(2f))
        temp.addAction(Actions.after(Actions.run { temp.remove() }))
    }
}
