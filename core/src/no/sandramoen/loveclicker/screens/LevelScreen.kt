package no.sandramoen.loveclicker.screens

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import no.sandramoen.loveclicker.actors.Heart
import no.sandramoen.loveclicker.utils.BaseGame
import no.sandramoen.loveclicker.utils.BaseScreen

class LevelScreen : BaseScreen() {
    private lateinit var heart: Heart
    private var loves = 0

    private lateinit var loveCountLabel: Label

    override fun initialize() {
        heart = Heart(0f, 0f, mainStage)
        loveCountLabel = Label("Loves: $loves", BaseGame.labelStyle)

        uiTable.add(loveCountLabel).expand().top().pad(10f)
        // uiTable.debug = true
    }

    override fun update(dt: Float) {
        loveCountLabel.setText("Loves: $loves")
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (heart.clicked(screenX, screenY)) {
            loves++
        }
        loveCountLabel.setText("Loves: $loves")
        return false
    }
}
