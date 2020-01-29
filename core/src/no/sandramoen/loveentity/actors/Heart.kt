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
    val income = 1L
    var bonusTime = 0f
    var cpsTime = 0f
    var clicks = 0

    init {
        loadAnimation(BaseGame.textureAtlas!!.findRegion("heart"))
        width *= Gdx.graphics.width * .0065f
        height *= Gdx.graphics.width * .0065f
        setOrigin(Align.center)

        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                clicks++

                if (BaseGame.currentAscensionPoints > 0) {
                    BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(income * (BaseGame.currentAscensionPoints * BaseGame.ascensionBonus) * BaseGame.heartBonus))
                    BaseGame.lifeTimeLove = BaseGame.lifeTimeLove.add(BaseGame.lifeTimeLove, BigNumber(income * (BaseGame.currentAscensionPoints * BaseGame.ascensionBonus) * BaseGame.heartBonus))
                } else {
                    BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(income * BaseGame.heartBonus))
                    BaseGame.lifeTimeLove = BaseGame.lifeTimeLove.add(BaseGame.lifeTimeLove, BigNumber(income * BaseGame.heartBonus))
                }

                addAction(Actions.scaleTo(1.4f, 1.4f, .25f, Interpolation.pow2Out))
                addAction(Actions.delay(.25f))
                addAction(Actions.scaleTo(1.0f, 1.0f, .5f, Interpolation.fade))
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

        /*if (time >= 2f) {
            touched = false
            bonusTime -= dt
        } else {
            bonusTime += dt
        }

        if (bonusTime < 0f)
            bonusTime = 0f
        if (bonusTime > 5f)
            bonusTime = 5f*/
    }
}
