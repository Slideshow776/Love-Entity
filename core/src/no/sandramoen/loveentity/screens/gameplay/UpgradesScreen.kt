package no.sandramoen.loveentity.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import no.sandramoen.loveentity.actors.Upgrade
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.BaseScreen
import no.sandramoen.loveentity.utils.BigNumber
import no.sandramoen.loveentity.utils.GameUtils

class UpgradesScreen : BaseScreen() {
    private lateinit var titleLabel: Label
    private lateinit var loveLabel: Label
    private lateinit var subtitleLabel: Label
    private lateinit var descriptionLabel: Label

    private lateinit var upgradesTable: Table

    override fun initialize() {
        titleLabel = Label("Upgrades", BaseGame.labelStyle)
        titleLabel.setFontScale(.7f)

        loveLabel = Label("${BaseGame.love.presentLongScale()} love", BaseGame.labelStyle)
        loveLabel.setFontScale(.5f)

        val exitButtonStyle = Button.ButtonStyle()
        exitButtonStyle.up = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("cross-white")))
        val exitButton = Button(exitButtonStyle)
        exitButton.isTransform = true
        exitButton.scaleBy(-.5f)
        exitButton.setOrigin(Align.top)
        exitButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                BaseGame.setActiveScreen(LevelScreen())
            false
        }

        // info table
        val infoLabel = Label("Cupcake ipsum dolor sit amet. Gingerbread marshmallow sugar plum pastry dragée gingerbread candy cookie. Bonbon dessert tiramisu dragée.\n" +
                "\n" + "Powder jelly-o lollipop. Cookie chupa chups powder cake muffin pudding. Soufflé cupcake chocolate apple pie danish toffee dessert powder. Cake pudding jelly cake jelly tootsie roll.", BaseGame.labelStyle)
        infoLabel.color = Color.PURPLE
        infoLabel.setWrap(true)
        infoLabel.setFontScale(.3f)

        val infoTable = Table()
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
        infoButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                infoTable.isVisible = !infoTable.isVisible
            false
        }

        // upper table
        subtitleLabel = Label("Upgrade your compassion capabilities!", BaseGame.labelStyle)
        subtitleLabel.setFontScale(.3f)
        descriptionLabel = Label("Invest in these skills to give your compassion a boost", BaseGame.labelStyle)
        descriptionLabel.setFontScale(.25f)

        val upperTable = Table()
        upperTable.add(titleLabel).expandX().center()
        upperTable.add(exitButton).row()
        upperTable.add(loveLabel)
        upperTable.add(infoButton).row()
        upperTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(.2f, .2f, .2f, 1f))
        // upperTable.debug = true

        // upgrades table
        upgradesTable = Table()
        initializeUpgrades()

        val scroll = ScrollPane(upgradesTable)
        val scrollableTable = Table()
        scrollableTable.add(scroll)

        // table layout
        val mainTable = Table()
        mainTable.setFillParent(true)

        mainTable.add(upperTable).fillX()
        mainTable.row()
        mainTable.add(scrollableTable)

        val stack = Stack() // stack allows for scene2d elements to overlap each other
        stack.add(mainTable)
        stack.add(infoTable)
        stack.setFillParent(true)
        mainTable.align(Align.top)

        mainStage.addActor(stack)
    }

    override fun update(dt: Float) {
        loveLabel.setText("${BaseGame.love.presentLongScale()} love")

        for (i in 0 until BaseGame.upgrades.size) {
            if (BaseGame.upgrades[i].remove) {
                initializeUpgrades()
                break
            }
        }

        for (generator in BaseGame.resourceGenerators)
            generator.act(Gdx.graphics.deltaTime)
    }

    override fun keyDown(keycode: Int): Boolean {
        if(keycode == Input.Keys.BACK)
            BaseGame.setActiveScreen(LevelScreen())
        return false;
    }

    private fun initializeUpgrades() {
        upgradesTable.reset()
        upgradesTable.add(subtitleLabel).padTop(25f).row()
        upgradesTable.add(descriptionLabel).padBottom(25f).row()
        upgradesTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(.05f, .05f, .05f, 1f))

        for (i in 0 until BaseGame.upgrades.size) {
            if (BaseGame.resourceGenerators[BaseGame.upgrades[i].id].isVisible && !BaseGame.upgrades[i].remove) {
                BaseGame.upgrades[i].isVisible = true
                BaseGame.upgrades[i].checkAffordable()
                upgradesTable.add(BaseGame.upgrades[i]).padBottom(Gdx.graphics.height * .07f).row()

                if (!BaseGame.resourceGenerators[BaseGame.upgrades[i].id].hideTable.isVisible)
                    BaseGame.upgrades[i].hideTable.isVisible = false
            }
        }
    }
}
