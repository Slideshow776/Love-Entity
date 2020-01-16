package no.sandramoen.loveentity.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Array

import no.sandramoen.loveentity.actors.Heart
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.BaseScreen
import no.sandramoen.loveentity.actors.ResourceGenerator
import no.sandramoen.loveentity.utils.GameUtils
import no.sandramoen.loveentity.utils.BigNumber
import java.util.Date

class LevelScreen : BaseScreen() {
    private lateinit var heart: Heart
    private lateinit var resourceGenerators: Array<ResourceGenerator>

    private lateinit var loveCountLabel: Label
    private lateinit var burgerButton: Button
    private var burgerMenuActive = false

    override fun initialize() {
        heart = Heart(0f, 0f, mainStage)

        resourceGenerators = Array()
        resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Ally", 15, 1.15f, .1f, 1f))
        resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Bisexual", 100, 1.15f, .5f, 2f))
        resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Gay", 500, 1.15f, 4f, 4f))
        resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Lesbian", 3000, 1.15f, 10f, 8f))
        resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Cisgender", 10000, 1.15f, 40f, 16f))
        resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Queer", 200000, 1.15f, 100f, 32f))
        resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Transgender", 1666666, 1.15f, 6666f, 64f))
        resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Intersex", 123456789, 1.15f, 98765f, 128f))
        resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Pansexual", 3999999999, 1.15f, 999999f, 256f))
        resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Asexual", 75000000000, 1.15f, 10000000f, 512f))

        loveCountLabel = Label("Love: ${BaseGame.love}", BaseGame.labelStyle)
        loveCountLabel.setFontScale(.5f)

        val burgerButtonStyle = Button.ButtonStyle()
        burgerButtonStyle.up = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("burger")))

        val burgerTable = Table()

        // debug options
        val debugButton1 = TextButton("Add 1k love", BaseGame.textButtonStyle)
        val debugButton2 = TextButton("Add 100k love", BaseGame.textButtonStyle)
        val debugButton3 = TextButton("Add 1M love", BaseGame.textButtonStyle)
        val debugButton4 = TextButton("Add 100M love", BaseGame.textButtonStyle)
        val debugButton5 = TextButton("Restart", BaseGame.textButtonStyle)

        debugButton1.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(1_000))
            }
            false
        }
        debugButton2.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(100_000))
            }
            false
        }
        debugButton3.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(1_000_000))
            }
            false
        }
        debugButton4.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(100_000_000))
                // BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(999_999_999_999_999_999))
            }
            false
        }
        debugButton5.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.love = BigNumber(0)
                for (generator in resourceGenerators)
                    generator.reset()
            }
            false
        }

        burgerTable.add(debugButton1).row()
        burgerTable.add(debugButton2).row()
        burgerTable.add(debugButton3).row()
        burgerTable.add(debugButton4).row()
        burgerTable.add(debugButton5).row()
        burgerTable.isVisible = false

        burgerButton = Button(burgerButtonStyle)
        burgerButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                if (burgerMenuActive) {
                    burgerButtonStyle.up = TextureRegionDrawable(BaseGame.textureAtlas!!.findRegion("burger"))
                    uiTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(0f, 0f, 0f, .0f))
                    burgerTable.isVisible = false
                } else {
                    burgerButtonStyle.up = TextureRegionDrawable(BaseGame.textureAtlas!!.findRegion("cross"))
                    uiTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(0f, 0f, 0f, .75f))
                    burgerTable.isVisible = true
                }
                burgerMenuActive = !burgerMenuActive
            }
            false
        }

        val uiToggleTable = Table()
        uiToggleTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(0f, 0f, 0f, .75f))
        uiToggleTable.add(burgerButton).pad(Gdx.graphics.height * .01f)

        // uiTable.debug = true
        uiTable.add(burgerTable).row()
        uiTable.add(uiToggleTable).fillX().expand().bottom()

        val scrollableTable = Table()

        val table = Table()
        table.add(heart).padBottom(Gdx.graphics.height * .1f).padTop(Gdx.graphics.height * .1f).row()
        for (generator in resourceGenerators) {
            table.add(generator).padBottom(Gdx.graphics.height * .07f).row()
        }

        val scroll = ScrollPane(table)
        scrollableTable.add(scroll)

        val mainTable = Table()
        mainTable.setFillParent(true)

        mainTable.add(loveCountLabel)
        mainTable.row()
        mainTable.add(scrollableTable)

        mainStage.addActor(mainTable)

        // mainTable.debug = true
        // uiTable.debug = true
        // table.debug = true
        // scrollableTable.debug = true
    }

    override fun update(dt: Float) {
        loveCountLabel.setText("${BaseGame.love.presentLongScale()} love")
    }

    override fun resume() { // TODO: This may cause a bug if game is paused in another screen? further testing required
        super.resume()

        // add love generated since pausing
        BaseGame.lastTimePlayed = BaseGame.prefs!!.getLong("lastTimePlayed")
        if (BaseGame.lastTimePlayed != 0L) BaseGame.secondsSinceLastPlayed = (Date().time - BaseGame.lastTimePlayed) / 1000
        for (generator in resourceGenerators)
            generator.addLoveSinceLastTimedPlayed()
    }
}
