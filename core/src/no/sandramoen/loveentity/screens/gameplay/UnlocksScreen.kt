package no.sandramoen.loveentity.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.BaseScreen
import no.sandramoen.loveentity.utils.GameUtils

class UnlocksScreen : BaseScreen() {
    private lateinit var infoTable: Table
    private lateinit var exitButton: Button
    private lateinit var titleImage: Image

    override fun initialize() {
        if (BaseGame.english)
            titleImage = Image(BaseGame.textureAtlas!!.findRegion("bannerUnlocks"))
        else
            titleImage = Image(BaseGame.textureAtlas!!.findRegion("bannerOppnåelser"))

        // main exit button
        val exitButtonStyle = Button.ButtonStyle()
        exitButtonStyle.up = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("cross-white")))
        exitButton = Button(exitButtonStyle)
        exitButton.isTransform = true
        exitButton.scaleBy(-.25f)
        exitButton.setOrigin(Align.top)
        exitButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                BaseGame.setActiveScreen(LevelScreen())
            false
        }

        // info table
        val infoLabel = Label("Cupcake ipsum dolor sit amet. Gingerbread marshmallow sugar plum pastry dragée gingerbread candy cookie. Bonbon dessert tiramisu dragée.\n" +
                "\n" + "Powder jelly-o lollipop. Cookie chupa chups powder cake muffin pudding. Soufflé cupcake chocolate apple pie danish toffee dessert powder. Cake pudding jelly cake jelly tootsie roll.", BaseGame.labelStyle)
        if (!BaseGame.english)
            infoLabel.setText("Cupcake ipsum dolor sit amet. Gingerbread marshmallow sugar plum pastry dragée gingerbread candy cookie. Bonbon dessert tiramisu dragée.\n" +
                    "\n" + "Powder jelly-o lollipop. Cookie chupa chups powder cake muffin pudding. Soufflé cupcake chocolate apple pie danish toffee dessert powder. Cake pudding jelly cake jelly tootsie roll.")
        infoLabel.color = Color.PURPLE
        infoLabel.setWrap(true)
        infoLabel.setFontScale(.3f)

        infoTable = Table()
        val whiteInfoTable = Table()
        whiteInfoTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(1f, 1f, 1f, 1f))
        whiteInfoTable.add(infoLabel).expand().fill().pad(20f)
        infoTable.add(whiteInfoTable).width(Gdx.graphics.width * .5f).height(Gdx.graphics.height * .5f)
        infoTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(0f, 0f, 0f, .9f))
        infoTable.isVisible = false
        infoTable.addListener(object : ActorGestureListener() {
            override fun tap(event: InputEvent?, x: Float, y: Float, count: Int, button: Int) {
                infoTable.isVisible = !infoTable.isVisible
            }
        })
        // infoTable.debug = true

        val infoButtonStyle = Button.ButtonStyle()
        infoButtonStyle.up = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("question")))
        val infoButton = Button(infoButtonStyle)
        infoButton.isTransform = true
        infoButton.scaleBy(.2f)
        infoButton.setOrigin(Align.center)
        infoButton.color = Color(95 / 255f, 152 / 255f, 209 / 255f, 1f) // grey blue
        infoButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                infoTable.isVisible = !infoTable.isVisible
            false
        }

        // upper table
        val subtitleLabel = Label("Unlocking your potential!", BaseGame.labelStyle)
        if (!BaseGame.english) subtitleLabel.setText("Oppnå ditt potensial!")
        subtitleLabel.setFontScale(.4f)
        subtitleLabel.setAlignment(Align.center)
        subtitleLabel.color = Color.PURPLE
        val descriptionLabel = Label("Want to maximize your love? Unlock these for a sweet lovin' bonus", BaseGame.labelStyle)
        if (!BaseGame.english) descriptionLabel.setText("Har du lyst å maksimere din kjærlighet? Oppnå disse for en ekstra bonus!")
        descriptionLabel.setWrap(true)
        descriptionLabel.setFontScale(.3f)
        descriptionLabel.setAlignment(Align.center)

        val upperTable = Table()
        upperTable.add(titleImage).width(Gdx.graphics.width * .95f).height(Gdx.graphics.height * .1f)
        // upperTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(.2f, .2f, .2f, 1f))
        // upperTable.debug = true

        val exitTable = Table()
        exitTable.setFillParent(true)
        exitTable.add(exitButton).top().right().pad(Gdx.graphics.width * .01f)
        uiTable.add(exitTable)

        // unlocks table
        val unlocksTable = Table()
        // unlocksTable.debug = true
        var count = 0
        for (i in 0 until BaseGame.resourceGenerators.size) {
            if (!BaseGame.resourceGenerators[i].hideTable.isVisible
                    && BaseGame.resourceGenerators[i].unlocks.size > BaseGame.resourceGenerators[i].unlockIndex) {
                val unlockImage = Image()
                unlockImage.drawable = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion(BaseGame.resourceGenerators[i].avatar)))

                val unlockCostLabel = Label("${BaseGame.resourceGenerators[i].unlocks[BaseGame.resourceGenerators[i].unlockIndex].goal}", BaseGame.labelStyle)
                unlockCostLabel.setFontScale(.5f)
                unlockCostLabel.color = Color.PURPLE

                val unlockNameLabel = Label("", BaseGame.labelStyle)
                if (BaseGame.english)
                    unlockNameLabel.setText("${BaseGame.resourceGenerators[i].resourceName}")
                else
                    unlockNameLabel.setText("${BaseGame.resourceGenerators[i].norwegianName}")
                unlockNameLabel.setFontScale(.25f)

                val unlockEffectLabel = Label("${BaseGame.resourceGenerators[i].unlocks[BaseGame.resourceGenerators[i].unlockIndex].effect}", BaseGame.labelStyle)
                if (BaseGame.resourceGenerators[i].unlocks[BaseGame.resourceGenerators[i].unlockIndex].effect == "speed")
                    unlockEffectLabel.setText("x2")
                unlockEffectLabel.setFontScale(.25f)

                // table layout
                val patch = NinePatch(TextureRegion(BaseGame.textureAtlas!!.findRegion("border")), 24, 24, 24, 24)
                val background = NinePatchDrawable(patch).tint(Color.ORANGE)

                val unlockTable = Table()
                unlockTable.background = background
                unlockTable.add(unlockImage).width(100f).height(125f).row()
                unlockTable.add(unlockCostLabel).row()
                unlockTable.add(unlockNameLabel).row()
                unlockTable.add(unlockEffectLabel)

                if ((count + 1) % 4 == 0) // for every fourth upgrade, add a row
                    unlocksTable.add(unlockTable).width(Gdx.graphics.width * .23f).row()
                else
                    unlocksTable.add(unlockTable).width(Gdx.graphics.width * .23f)
                count++ // ensures we have no gaps in our 'rows and columns table'
            }
        }

        val table = Table()
        table.add(subtitleLabel).padTop(25f).width(Gdx.graphics.width * .85f).colspan(2).row()
        // table.add(infoButton).width(Gdx.graphics.width * .06f).height(Gdx.graphics.width * .06f).row()
        table.add(descriptionLabel).padBottom(10f).width(Gdx.graphics.width * .85f).colspan(2).row()
        table.add(unlocksTable).align(Align.center).colspan(2)
        // table.debug = true

        // table layout
        val mainTable = Table()
        mainTable.setFillParent(true)

        mainTable.add(upperTable).fillX().colspan(2)
        mainTable.row()
        mainTable.add(table).colspan(2)
        // mainTable.debug = true

        val stack = Stack() // stack allows for scene2d elements to overlap each other
        stack.add(mainTable)
        stack.add(infoTable)
        stack.setFillParent(true)
        mainTable.align(Align.top)

        mainStage.addActor(stack)
    }

    override fun update(dt: Float) {
        for (generator in BaseGame.resourceGenerators)
            generator.act(Gdx.graphics.deltaTime)

        if (infoTable.isVisible)
            exitButton.touchable = Touchable.disabled
        else
            exitButton.touchable = Touchable.enabled
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK)
            BaseGame.setActiveScreen(LevelScreen())
        return false
    }
}
