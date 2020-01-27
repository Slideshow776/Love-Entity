package no.sandramoen.loveentity.screens.gameplay

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
import no.sandramoen.loveentity.actors.Unlock
import no.sandramoen.loveentity.utils.GameUtils
import no.sandramoen.loveentity.utils.BigNumber

class LevelScreen : BaseScreen() {
    private lateinit var heart: Heart

    private lateinit var loveLabel: Label
    private lateinit var burgerButton: Button
    private var burgerMenuActive = false

    private var revealNextGenerator = false

    private lateinit var table: Table

    private var time = 0f

    override fun initialize() {
        heart = Heart(0f, 0f, mainStage)

        val allyUnlocks = Array<Unlock>()
        allyUnlocks.add(Unlock(25, "speed"), Unlock(50, "speed"), Unlock(100, "speed"))

        if (BaseGame.resourceGenerators.size != 10) {
            BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Ally", "pixelAvatarTest", allyUnlocks, 4, 1.07f, 1f, .5f))
            BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Bisexual", "pixelAvatarTest", allyUnlocks,  60, 1.15f, 60f, 3f))
            BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Gay", "pixelAvatarTest", allyUnlocks,   720, 1.14f, 540f, 6f))
            BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Lesbian", "pixelAvatarTest", allyUnlocks,   8640, 1.13f, 4320f, 12f))
            BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Cisgender", "pixelAvatarTest", allyUnlocks,   103680, 1.12f, 51840f, 24f))
            BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Queer", "pixelAvatarTest", allyUnlocks,   1244160, 1.11f, 622080f, 96f))
            BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Transgender", "pixelAvatarTest", allyUnlocks,   14929920, 1.1f, 7464960f, 384f))
            BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Intersex", "pixelAvatarTest", allyUnlocks,   179159040, 1.09f, 89579520f, 1536f))
            BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Pansexual", "pixelAvatarTest", allyUnlocks,   2149908480, 1.08f, 1074954240f, 6144f))
            BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Asexual", "pixelAvatarTest", allyUnlocks,   25798901760, 1.07f, 29668737024f, 36864f))
        }

        loveLabel = Label("${BaseGame.love.presentLongScale()} love", BaseGame.labelStyle)
        loveLabel.setFontScale(.5f)

        val burgerButtonStyle = Button.ButtonStyle()
        burgerButtonStyle.up = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("burger")))

        val burgerTable = Table()

        /* debug options------------------------------------------------------------------------------------------ */
        val debugButton1 = TextButton("Add 1k love", BaseGame.textButtonStyle)
        val debugButton2 = TextButton("Add 100k love", BaseGame.textButtonStyle)
        val debugButton3 = TextButton("Add 1M love", BaseGame.textButtonStyle)
        val debugButton4 = TextButton("Add 1T love", BaseGame.textButtonStyle)
        val debugButton5 = TextButton("Restart", BaseGame.textButtonStyle)

        debugButton1.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(1_000))
                BaseGame.lifeTimeLove = BaseGame.lifeTimeLove.add(BaseGame.lifeTimeLove, BigNumber(1_000))
                update(Gdx.graphics.deltaTime) // updates hidetable visibilities
            }
            false
        }
        debugButton2.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(100_000))
                BaseGame.lifeTimeLove = BaseGame.lifeTimeLove.add(BaseGame.lifeTimeLove, BigNumber(100_000))
                update(Gdx.graphics.deltaTime) // updates hidetable visibilities
            }
            false
        }
        debugButton3.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(1_000_000))
                BaseGame.lifeTimeLove = BaseGame.lifeTimeLove.add(BaseGame.lifeTimeLove, BigNumber(1_000_000))
                update(Gdx.graphics.deltaTime) // updates hidetable visibilities
            }
            false
        }
        debugButton4.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                /*BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(100_000_000))
                BaseGame.lifeTimeLove = BaseGame.lifeTimeLove.add(BaseGame.lifeTimeLove, BigNumber(100_000_000))*/
                BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(999_999_999_999_999_999))
                BaseGame.lifeTimeLove = BaseGame.love.add(BaseGame.lifeTimeLove, BigNumber(999_999_999_999_999_999))
                update(Gdx.graphics.deltaTime) // updates hidetable visibilities
            }
            false
        }
        debugButton5.addListener { e: Event ->
            // the restart button
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.love = BigNumber(0)
                BaseGame.lifeTimeLove = BigNumber(0)
                BaseGame.revealNextGeneratorIndex = 0
                BaseGame.currentAscensionPoints = 0
                for (generator in BaseGame.resourceGenerators)
                    generator.reset()

                table.reset()
                table.add(heart).padBottom(Gdx.graphics.height * .1f).padTop(Gdx.graphics.height * .1f).row()
                for (i in 0 until 2) {
                    table.add(BaseGame.resourceGenerators[i]).padBottom(Gdx.graphics.height * .07f).row()
                    BaseGame.resourceGenerators[i].isVisible = true // solves a visibility bug
                }
                update(Gdx.graphics.deltaTime) // updates hidetable visibilities
                GameUtils.saveGameState()
            }
            false
        }

        burgerTable.add(debugButton1).row()
        burgerTable.add(debugButton2).row()
        burgerTable.add(debugButton3).row()
        burgerTable.add(debugButton4).row()
        burgerTable.add(debugButton5).padBottom(100f).row()
        /* ------------------------------------------------------------------------------------------------------- */

        val communityLeadersButton = TextButton("Community Leaders", BaseGame.textButtonStyle)
        communityLeadersButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                BaseGame.setActiveScreen(CommunityLeadersScreen())
            false
        }
        burgerTable.add(communityLeadersButton).row()

        val upgradesButton = TextButton("Upgrades", BaseGame.textButtonStyle)
        upgradesButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                BaseGame.setActiveScreen(UpgradesScreen())
            false
        }
        burgerTable.add(upgradesButton).row()

        val ascensionButton = TextButton("Ascension", BaseGame.textButtonStyle)
        ascensionButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                BaseGame.setActiveScreen(AscensionScreen())
            false
        }
        burgerTable.add(ascensionButton).row()

        val unlocksButton = TextButton("Unlocks", BaseGame.textButtonStyle)
        unlocksButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                BaseGame.setActiveScreen(UnlocksScreen())
            false
        }
        burgerTable.add(unlocksButton).row()

        burgerTable.isVisible = false

        burgerButton = Button(burgerButtonStyle)
        burgerButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                if (burgerMenuActive) {
                    burgerButtonStyle.up = TextureRegionDrawable(BaseGame.textureAtlas!!.findRegion("burger"))
                    uiTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(0f, 0f, 0f, .0f))
                    burgerTable.isVisible = false
                } else {
                    burgerButtonStyle.up = TextureRegionDrawable(BaseGame.textureAtlas!!.findRegion("cross-white"))
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

        table = Table()
        table.add(heart).padBottom(Gdx.graphics.height * .1f).padTop(Gdx.graphics.height * .1f).row()
        if (BaseGame.revealNextGeneratorIndex < 1) {
            for (i in 0 until 2) {
                table.add(BaseGame.resourceGenerators[i]).padBottom(Gdx.graphics.height * .07f).row()
                BaseGame.resourceGenerators[i].isVisible = true // solves a visibility bug
            }
        } else {
            for (i in 0 until BaseGame.revealNextGeneratorIndex) {
                table.add(BaseGame.resourceGenerators[i]).padBottom(Gdx.graphics.height * .07f).row()
                BaseGame.resourceGenerators[i].isVisible = true // solves a visibility bug
                BaseGame.resourceGenerators[i].hideTable.isVisible = false
            }
            revealTwoNextGenerators()
        }

        val scroll = ScrollPane(table)
        scrollableTable.add(scroll)

        val mainTable = Table()
        mainTable.setFillParent(true)

        mainTable.add(loveLabel)
        mainTable.row()
        mainTable.add(scrollableTable)

        mainStage.addActor(mainTable)

        // mainTable.debug = true
        // uiTable.debug = true
        // table.debug = true
    }

    override fun update(dt: Float) {
        loveLabel.setText("${BaseGame.love.presentLongScale()} love")

        if (BaseGame.revealNextGeneratorIndex < BaseGame.resourceGenerators.size &&
                BaseGame.love.isGreaterThanOrEqualTo(BigNumber(BaseGame.resourceGenerators[BaseGame.revealNextGeneratorIndex].baseCost)))
            revealNextGenerator = true

        if (revealNextGenerator)
            revealNextGenerator()

        time += dt
        if (time > 1) { // calculate every second
            time = 0f
            GameUtils.calculateAscension()
        }
    }

    private fun revealNextGenerator() {
        if (BaseGame.revealNextGeneratorIndex < BaseGame.resourceGenerators.size) {
            BaseGame.resourceGenerators[BaseGame.revealNextGeneratorIndex].exposeResourceGenerator()

            if (BaseGame.revealNextGeneratorIndex < BaseGame.resourceGenerators.size)
                BaseGame.revealNextGeneratorIndex++
            if (BaseGame.revealNextGeneratorIndex < BaseGame.resourceGenerators.size - 1) {
                table.add(BaseGame.resourceGenerators[BaseGame.revealNextGeneratorIndex + 1]).padBottom(Gdx.graphics.height * .07f).row()
                BaseGame.resourceGenerators[BaseGame.revealNextGeneratorIndex + 1].isVisible = true
            }

            BaseGame.prefs!!.putInteger("revealNextGenerator", BaseGame.revealNextGeneratorIndex)
            revealNextGenerator = false
        }
    }

    private fun revealTwoNextGenerators() {
        for (i in 0 until 2) {
            if (BaseGame.revealNextGeneratorIndex < BaseGame.resourceGenerators.size) {
                if (BaseGame.revealNextGeneratorIndex < BaseGame.resourceGenerators.size - 1) {
                    table.add(BaseGame.resourceGenerators[BaseGame.revealNextGeneratorIndex + i]).padBottom(Gdx.graphics.height * .07f).row()
                    BaseGame.resourceGenerators[BaseGame.revealNextGeneratorIndex + i].isVisible = true
                }
            }
        }
    }
}
