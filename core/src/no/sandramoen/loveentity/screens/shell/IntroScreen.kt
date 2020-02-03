package no.sandramoen.loveentity.screens.shell

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import no.sandramoen.loveentity.screens.gameplay.LevelScreen
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.BaseScreen

class IntroScreen : BaseScreen() {
    override fun initialize() {
        val label0 = Label("You are a ", BaseGame.labelStyle)
        if (!BaseGame.english) label0.setText("Du er en ")
        label0.setFontScale(.7f)
        val label1 = Label("love entity", BaseGame.labelStyle)
        if (!BaseGame.english) label1.setText("kjærlighets-entitet")
        label1.setFontScale(.7f)
        val label2 = Label("...", BaseGame.labelStyle)
        label2.setFontScale(.7f)

        label1.addAction(Actions.color(Color.PINK, 2f))
        label1.addAction(Actions.after(Actions.run {
            label0.addAction(Actions.fadeOut(2f))
            label1.addAction(Actions.fadeOut(2f))
            label2.addAction(Actions.fadeOut(2f))
        }))
        label0.addAction(Actions.sequence(
                Actions.delay(4f),
                Actions.run {
                    label0.setText("How much can you love?")
                    if (!BaseGame.english) label0.setText("Hvor mye kan du elske?")
                    label1.setText("")
                    label2.setText("")
                },
                Actions.fadeIn(2f),
                Actions.fadeOut(2f),
                Actions.run {
                    BaseGame.prefs!!.putBoolean("skipIntro", true)
                    BaseGame.setActiveScreen(LevelScreen())
                }
        ))

        val table = Table()
        table.add(label0)
        table.add(label1)
        table.add(label2)
        table.setFillParent(true)
        mainStage.addActor(table)
    }

    override fun update(dt: Float) {

    }
}
