package no.sandramoen.loveentity.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import no.sandramoen.loveentity.utils.BaseActor
import no.sandramoen.loveentity.utils.BaseGame

class Veil(x: Float, y: Float, s: Stage) : BaseActor(x, y, s) {
    var time = 0f
    var label: Label

    private var waveBackground: WaveBackground

    init {
        width = Gdx.graphics.width.toFloat()
        height = 1f
        isVisible = false
        touchable = Touchable.childrenOnly
        setOrigin(Align.bottom)

        waveBackground = WaveBackground(0f, 0f, "colorGradient", s)
        waveBackground.touchable = Touchable.childrenOnly
        waveBackground.scaleBy(.005f, 0f)
        waveBackground.setPosition(0f, 400f)
        waveBackground.setOrigin(Align.bottom)

        label = Label("x1", BaseGame.labelStyle)
        label.setOrigin(Align.center)
        label.isVisible = false
        label.touchable = Touchable.childrenOnly
        label.moveBy(0f, 400f)

        val labelTable = Table()
        labelTable.add(label).top().padRight(Gdx.graphics.width * .1f)
        labelTable.isTransform = true
        labelTable.setPosition(0f, 400f)
        // labelTable.debug = true

        val stack = Stack()
        stack.add(waveBackground)
        stack.add(labelTable)
        stack.width = Gdx.graphics.width * 1.1f
        addActor(stack)

        waveBackground.isVisible = false
    }

    override fun act(dt: Float) {
        super.act(dt)
        waveBackground.act(dt)
        when (BaseGame.heartBonus) {
            5 -> {
                label.setText("x5")
                addAction(Actions.sizeTo(Gdx.graphics.width.toFloat(), Gdx.graphics.height * .4f, 2f))
                waveBackground.addAction(Actions.sizeTo(Gdx.graphics.width.toFloat(), Gdx.graphics.height * .34f, 2f))
                    label.addAction(Actions.fadeIn(.25f))
                    label.addAction(Actions.color(Color.FIREBRICK, 2f))
                    label.addAction(Actions.moveTo(Gdx.graphics.width / 2 - label.width / 2, waveBackground.height / 2 - label.height / 2, .1f))

                if (!label.isVisible) label.isVisible = true
                if (!waveBackground.isVisible) waveBackground.isVisible = true
            }
            4 -> {
                label.setText("x4")
                addAction(Actions.sizeTo(Gdx.graphics.width.toFloat(), Gdx.graphics.height * .35f, 2f))
                waveBackground.addAction(Actions.sizeTo(Gdx.graphics.width.toFloat(), Gdx.graphics.height * .29f, 2f))
                    label.addAction(Actions.fadeIn(.25f))
                    label.addAction(Actions.color(Color.RED, 2f))
                    label.addAction(Actions.moveTo(Gdx.graphics.width / 2 - label.width / 2, waveBackground.height / 2 - label.height / 2, .1f))

                if (!label.isVisible) label.isVisible = true
                if (!waveBackground.isVisible) waveBackground.isVisible = true
            }
            3 -> {
                label.setText("x3")
                addAction(Actions.sizeTo(Gdx.graphics.width.toFloat(), Gdx.graphics.height * .3f, 2f))
                waveBackground.addAction(Actions.sizeTo(Gdx.graphics.width.toFloat(), Gdx.graphics.height * .24f, 2f))
                    label.addAction(Actions.fadeIn(.25f))
                    label.addAction(Actions.color(Color.ORANGE, 2f))
                    label.addAction(Actions.moveTo(Gdx.graphics.width / 2 - label.width / 2, waveBackground.height / 2 - label.height / 2, .1f))

                if (!label.isVisible) label.isVisible = true
                if (!waveBackground.isVisible) waveBackground.isVisible = true
            }
            2 -> {
                label.setText("x2")
                addAction(Actions.sizeTo(Gdx.graphics.width.toFloat(), Gdx.graphics.height * .25f, 2f))
                waveBackground.addAction(Actions.sizeTo(Gdx.graphics.width.toFloat(), Gdx.graphics.height * .19f, 2f))
                    label.addAction(Actions.fadeIn(.25f))
                    label.addAction(Actions.color(Color.YELLOW, 2f))
                    label.addAction(Actions.moveTo(Gdx.graphics.width / 2 - label.width / 2, waveBackground.height / 2 - label.height / 2, .1f))

                if (!label.isVisible) label.isVisible = true
                if (!waveBackground.isVisible) waveBackground.isVisible = true
            }
            1 -> {
                label.setText("x1")
                addAction(Actions.sizeTo(Gdx.graphics.width.toFloat(), 0f, 2f))
                waveBackground.addAction(Actions.sizeTo(Gdx.graphics.width.toFloat(), 0f, 2f))
                if (label.isVisible) {
                    label.addAction(Actions.fadeOut(2f))
                    label.addAction(Actions.color(Color.WHITE, 2f))
                    label.addAction(Actions.moveTo(Gdx.graphics.width / 2 - label.width / 2, 0f, .1f))
                    label.addAction(Actions.sequence(
                            Actions.delay(2.1f),
                            Actions.run {
                                label.isVisible = false
                                waveBackground.isVisible = true
                            }
                    ))
                }
            }

        }
        time += dt
        if (time >= 3f)
            isVisible = true
    }
}
