package no.sandramoen.loveentity.screens

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import no.sandramoen.loveentity.actors.Heart
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.BaseScreen
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Array
import no.sandramoen.loveentity.actors.ResourceGenerator

class LevelScreen : BaseScreen() {
    private lateinit var heart: Heart
    private lateinit var resourceGenerators: Array<ResourceGenerator>

    private var love = 0

    private lateinit var loveCountLabel: Label

    override fun initialize() {
        heart = Heart(0f, 0f, mainStage)

        val labels = arrayOf("Ally", "Cisgender", "Bisexual", "Gay", "Lesbian", "Queer", "Transgender", "Intersex", "Pansexual", "Asexual")
        resourceGenerators = Array()
        for (label in labels) {
            resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, label))
        }

        loveCountLabel = Label("Love: $love", BaseGame.labelStyle)

        val scrollableTable = Table()

        val table = Table()
        // table.add(heart).padBottom(300f).padTop(300f).row()
        for (generator in resourceGenerators)
            table.add(generator).padBottom(50f).row()

        val scroll = ScrollPane(table)
        scrollableTable.add(scroll)

        uiTable.add(loveCountLabel)
        uiTable.row()
        uiTable.add(scrollableTable)

        /*uiTable.debug = true
        table.debug = true
        scrollableTable.debug = true*/
    }

    override fun update(dt: Float) {
        loveCountLabel.setText("Love: ${love + heart.love}")
    }
}
