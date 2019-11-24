package no.sandramoen.loveentity.screens

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import no.sandramoen.loveentity.actors.Heart
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.BaseScreen
import com.badlogic.gdx.scenes.scene2d.ui.TextButton

class LevelScreen : BaseScreen() {
    private lateinit var heart: Heart
    private var love = 0

    private lateinit var loveCountLabel: Label

    override fun initialize() {
        heart = Heart(0f, 0f, mainStage)
        loveCountLabel = Label("Love: $love", BaseGame.labelStyle)

        val scrollableTable = Table()

        val table = Table()
        table.setFillParent(true)
        table.add(heart).padBottom(50f).row()
        table.add<TextButton>(TextButton("WELCOME", BaseGame.textButtonStyle)).row()
        table.add<TextButton>(TextButton("WELCOME", BaseGame.textButtonStyle)).row()
        table.add<TextButton>(TextButton("WELCOME", BaseGame.textButtonStyle)).row()
        table.add<TextButton>(TextButton("WELCOME", BaseGame.textButtonStyle)).row()
        table.add<TextButton>(TextButton("WELCOME", BaseGame.textButtonStyle)).row()
        table.add<TextButton>(TextButton("WELCOME", BaseGame.textButtonStyle)).row()
        table.add<TextButton>(TextButton("WELCOME", BaseGame.textButtonStyle)).row()
        table.add<TextButton>(TextButton("WELCOME", BaseGame.textButtonStyle)).row()
        table.add<TextButton>(TextButton("WELCOME", BaseGame.textButtonStyle)).row()
        table.add<TextButton>(TextButton("WELCOME", BaseGame.textButtonStyle)).row()

        val scroll = ScrollPane(table)
        scrollableTable.add(scroll).growX()

        uiTable.add(loveCountLabel)
        uiTable.row()
        uiTable.add(scrollableTable).growX()

        /*uiTable.debug = true
        table.debug = true
        scrollableTable.debug = true*/
    }

    override fun update(dt: Float) {
        loveCountLabel.setText("Love: ${love + heart.love}")
    }
}
