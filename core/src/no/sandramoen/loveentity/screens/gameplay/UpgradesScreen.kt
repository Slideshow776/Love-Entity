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

    private lateinit var upgrades: Array<Upgrade>
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
        upgrades = Array()
        // this system assumes all upgrades are multiplicable of 3's
        if (BaseGame.resourceGenerators[0].upgrade / 3 == (1 / 3)) // first upgrade,
            upgrades.add(Upgrade(mainStage, 0, "itemTest", "Upgrade #1", "Ally love x3", BigNumber(250)))
        if (BaseGame.resourceGenerators[1].upgrade / 3 == (1 / 3)) // first upgrade
            upgrades.add(Upgrade(mainStage, 1, "itemTest", "Upgrade #2", "Bisexual love x3", BigNumber(500)))
        if (BaseGame.resourceGenerators[2].upgrade / 3 == (1 / 3)) // first upgrade
            upgrades.add(Upgrade(mainStage, 2, "itemTest", "Upgrade #3", "Gay love x3", BigNumber(1000000)))
        if (BaseGame.resourceGenerators[3].upgrade / 3 == (1 / 3)) // first upgrade
            upgrades.add(Upgrade(mainStage, 3, "itemTest", "Upgrade #4", "Lesbian love x3", BigNumber(5000000)))
        if (BaseGame.resourceGenerators[4].upgrade / 3 == (1 / 3)) // first upgrade
            upgrades.add(Upgrade(mainStage, 4, "itemTest", "Upgrade #5", "Cisgender love x3", BigNumber(10_000_000)))
        if (BaseGame.resourceGenerators[5].upgrade / 3 == (1 / 3)) // first upgrade
            upgrades.add(Upgrade(mainStage, 5, "itemTest", "Upgrade #6", "Queer love x3", BigNumber(25000000)))
        if (BaseGame.resourceGenerators[6].upgrade / 3 == (1 / 3)) // first upgrade
            upgrades.add(Upgrade(mainStage, 6, "itemTest", "Upgrade #7", "Transgender love x3", BigNumber(500000000)))
        if (BaseGame.resourceGenerators[7].upgrade / 3 == (1 / 3)) // first upgrade
            upgrades.add(Upgrade(mainStage, 7, "itemTest", "Upgrade #8", "Intersex love x3", BigNumber(10_000_000_000)))
        if (BaseGame.resourceGenerators[8].upgrade / 3 == (1 / 3)) // first upgrade
            upgrades.add(Upgrade(mainStage, 8, "itemTest", "Upgrade #9", "Pansexual love x3", BigNumber(250_000_000_000)))
        if (BaseGame.resourceGenerators[9].upgrade / 3 == (1 / 3)) // first upgrade
            upgrades.add(Upgrade(mainStage, 9, "itemTest", "Upgrade #10", "Asexual love x3", BigNumber(999999999999999999)))
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

        for (i in 0 until upgrades.size) {
            if (upgrades[i].remove) {
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

        for (i in 0 until upgrades.size) {
            if (BaseGame.resourceGenerators[upgrades[i].id].isVisible && !upgrades[i].remove) {
                upgrades[i].isVisible = true
                upgrades[i].checkAffordable()
                upgradesTable.add(upgrades[i]).padBottom(Gdx.graphics.height * .07f).row()

                if (!BaseGame.resourceGenerators[upgrades[i].id].hideTable.isVisible)
                    upgrades[i].hideTable.isVisible = false
            }
        }
    }
}
