package no.sandramoen.loveentity.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

import no.sandramoen.loveentity.utils.BaseActor
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.GameUtils

class ResourceGenerator(x: Float, y: Float, s: Stage, name: String) : BaseActor(x, y, s) {
    private var table: Table
    private var nameLabel: Label

    private var selfWidth = 600f
    private var selfHeight = 200f

    init {
        loadTexture("images/whitePixel.png")
        width = selfWidth
        height = selfHeight
        // color = Color(random(0, 255) / 255f, random(0, 255) / 255f, random(0, 255) / 255f, 1f)
        color = Color.DARK_GRAY

        nameLabel = Label(name, BaseGame.labelStyle)
        nameLabel.setFontScale(.75f)

        table = Table()
        table.width = selfWidth
        table.height = selfHeight

        table.add(nameLabel).top().colspan(3).row()
        table.add(leftTable(s)).pad(10f)
        table.add(rightTable(s))

        addActor(table)

        /*table.debug()
        this.debug*/
    }

    private fun leftTable(s: Stage): Table {
        val buttonStyle = Button.ButtonStyle()
        val buttonTex = Texture(Gdx.files.internal("images/whitePixel.png"))
        val buttonRegion = TextureRegion(buttonTex)
        buttonRegion.regionWidth = 150
        buttonRegion.regionHeight = 150
        buttonStyle.up = TextureRegionDrawable(buttonRegion)

        val button = Button(buttonStyle)
        button.color = Color.PINK

        val label = Label("23", BaseGame.labelStyle)
        label.setFontScale(.5f)
        label.color = Color.YELLOW

        val progress = BaseActor(0f, 0f, s)
        progress.loadTexture("images/whitePixel.png")
        progress.width = 150f
        progress.height = 35f
        progress.color = Color.FIREBRICK

        label.setPosition((progress.width / 2) - label.width / 3, -progress.height / 2) // weird offsets that just works...
        progress.addActor(label)

        button.addActor(progress)
        button.addListener { e: Event ->
            if(GameUtils.isTouchDownEvent(e)) {
                println("resource activated!")
            }
            false
        }
        return button
    }

    private fun rightTable(s: Stage): Table {
        val progress = BaseActor(0f, 0f, s)
        progress.loadTexture("images/whitePixel.png")
        progress.width = 400f
        progress.height = 75f
        progress.color = Color.GREEN

        val buy = BaseActor(0f, 0f, s)
        buy.loadTexture("images/whitePixel.png")
        buy.width = 200f
        buy.height = 75f
        buy.color = Color.ORANGE

        val time = BaseActor(0f, 0f, s)
        time.loadTexture("images/whitePixel.png")
        time.width = 200f
        time.height = 75f
        time.color = Color.LIGHT_GRAY

        val subTable = Table()
        subTable.add(progress).colspan(2).row()
        subTable.add(buy)
        subTable.add(time)
        return subTable
    }
}
