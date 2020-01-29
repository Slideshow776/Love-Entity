package no.sandramoen.loveentity.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import no.sandramoen.loveentity.utils.BaseActor
import no.sandramoen.loveentity.utils.BaseGame

class Veil(x: Float, y: Float, s: Stage) : BaseActor(x, y, s) {
    var time = 0f
    var label: Label

    init {
        loadAnimation(BaseGame.textureAtlas!!.findRegion("whitePixel"))
        width = Gdx.graphics.width.toFloat()
        height = 1f
        isVisible = false
        color = Color(1f, .78f, .81f, .4f)
        touchable = Touchable.childrenOnly
        setOrigin(Align.bottom)

        label = Label("x1", BaseGame.labelStyle)
        label.setOrigin(Align.center)
        label.isVisible = false
        label.touchable = Touchable.childrenOnly
        val table = Table()
        table.setFillParent(true)
        table.add(label)
        addActor(table)
    }

    override fun act(dt: Float) {
        super.act(dt)
        when (BaseGame.heartBonus) {
            5 -> {
                label.setText("x5")
                addAction(Actions.sizeTo(Gdx.graphics.width.toFloat(), Gdx.graphics.height * .35f, 2f))
                label.addAction(Actions.fadeIn(.25f))
                label.addAction(Actions.color(Color.FIREBRICK, 2f))
                if (!label.isVisible) label.isVisible = true
            }
            4 -> {
                label.setText("x4")
                addAction(Actions.sizeTo(Gdx.graphics.width.toFloat(), Gdx.graphics.height * .3f, 2f))
                label.addAction(Actions.fadeIn(.25f))
                label.addAction(Actions.color(Color.RED, 2f))
                if (!label.isVisible) label.isVisible = true
            }
            3 -> {
                label.setText("x3")
                addAction(Actions.sizeTo(Gdx.graphics.width.toFloat(), Gdx.graphics.height * .25f, 2f))
                label.addAction(Actions.fadeIn(.25f))
                label.addAction(Actions.color(Color.ORANGE, 2f))
                if (!label.isVisible) label.isVisible = true
            }
            2 -> {
                label.setText("x2")
                addAction(Actions.sizeTo(Gdx.graphics.width.toFloat(), Gdx.graphics.height * .2f, 2f))
                label.addAction(Actions.fadeIn(.25f))
                label.addAction(Actions.color(Color.YELLOW, 2f))
                if (!label.isVisible) label.isVisible = true
            }
            1 -> {
                label.setText("x1")
                addAction(Actions.sizeTo(Gdx.graphics.width.toFloat(), 0f, 2f))
                if (label.isVisible) {
                    label.addAction(Actions.fadeOut(2f))
                    label.addAction(Actions.color(Color.WHITE, 2f))
                    label.addAction(Actions.sequence(
                            Actions.delay(2.1f),
                            Actions.run { label.isVisible = false }
                    ))
                }
            }

        }
        time += dt
        if (time >= 3f)
            isVisible = true
    }
}
