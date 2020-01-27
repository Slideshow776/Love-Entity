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
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.BaseScreen
import no.sandramoen.loveentity.utils.GameUtils

class AscensionScreen : BaseScreen() {
    var time = 0f
    private lateinit var currentAscensionPointsLabel: Label
    private lateinit var claimAscensionPointsLabel: Label
    override fun initialize() {
        val titleLabel = Label("Ascension", BaseGame.labelStyle)
        titleLabel.setFontScale(.7f)

        // main exit button
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

        // warning table
        val warningLabel = Label("You have ${BaseGame.claimAscensionPoints} ascension points! Resetting now is a bad idea. Just keep earning love!", BaseGame.labelStyle)
        if (BaseGame.claimAscensionPoints > 0) warningLabel.setText("You have ${BaseGame.claimAscensionPoints} ascension points ready to boost your love! To claim them you will need to reset your progress.")
        warningLabel.color = Color.PURPLE
        warningLabel.setWrap(true)
        warningLabel.setFontScale(.3f)

        val warningButton = TextButton("Reset now!", BaseGame.textButtonStyle)
        warningButton.isTransform = true
        warningButton.scaleBy(-.2f)
        warningButton.setOrigin(Align.center)
        warningButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.currentAscensionPoints += BaseGame.claimAscensionPoints
                currentAscensionPointsLabel.setText("${BaseGame.currentAscensionPoints}")
                claimAscensionPointsLabel.setText("${BaseGame.claimAscensionPoints}")
                GameUtils.reset()
            }
            false
        }

        val warningExitButtonStyle = Button.ButtonStyle()
        warningExitButtonStyle.up = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("cross-black")))
        val warningExitButton = Button(warningExitButtonStyle)
        warningExitButton.isTransform = true
        warningExitButton.scaleBy(-.5f)
        warningExitButton.setOrigin(Align.bottomRight)

        val whiteWarningTable = Table()
        whiteWarningTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(1f, 1f, 1f, 1f))
        whiteWarningTable.add(warningExitButton).right().row()
        whiteWarningTable.add(warningLabel).expandX().fillX().pad(20f).row()
        whiteWarningTable.add(warningButton)
        whiteWarningTable.align(Align.bottom)
        // whiteWarningTable.debug = true

        val warningTable = Table()
        warningTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(0f, 0f, 0f, .9f))
        warningTable.isVisible = false
        warningTable.add(whiteWarningTable).width(Gdx.graphics.width * .6f).height(Gdx.graphics.height * .25f).row()
        warningTable.addListener(object : ActorGestureListener() {
            override fun tap(event: InputEvent?, x: Float, y: Float, count: Int, button: Int) {
                warningTable.isVisible = !warningTable.isVisible
            }
        })
        // warningTable.debug = true

        // upper table
        val subtitleLabel = Label("Ascend to a greater compassion level!", BaseGame.labelStyle)
        subtitleLabel.setFontScale(.4f)
        subtitleLabel.color = Color.PURPLE
        val descriptionLabel = Label("The more love you earn, the more Ascension points you realize! When claimed increase your capacity for love, but you'll need to start over. (Psst! It's worth it!)", BaseGame.labelStyle)
        descriptionLabel.setWrap(true)
        descriptionLabel.setFontScale(.3f)
        descriptionLabel.setAlignment(Align.center)
        val ascensionDescriptionLabel = Label("Your Total Ascension Points", BaseGame.labelStyle)
        ascensionDescriptionLabel.setFontScale(.55f)
        ascensionDescriptionLabel.color = Color.PURPLE
        currentAscensionPointsLabel = Label("${BaseGame.currentAscensionPoints}", BaseGame.labelStyle)
        currentAscensionPointsLabel.setFontScale(.55f)

        val table = Table()
        table.add(subtitleLabel).padTop(25f)
        table.add(infoButton).row()
        table.add(descriptionLabel).right().padBottom(100f).width(Gdx.graphics.width * .85f).row()
        table.add(ascensionDescriptionLabel).row()
        table.add(currentAscensionPointsLabel).padBottom(25f).row()
        table.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(.05f, .05f, .05f, 1f))
        // table.debug = true

        val upperTable = Table()
        upperTable.add(titleLabel).expandX().center()
        upperTable.add(exitButton).row()
        upperTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(.2f, .2f, .2f, 1f))
        // upperTable.debug = true

        val lowerLeftTable = Table()
        lowerLeftTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(.2f, .2f, .2f, .9f))
        val bonusLabel = Label("2%", BaseGame.labelStyle)
        bonusLabel.setFontScale(.5f)
        val bonusDescriptionLabel = Label("Compassion bonus per ascension point", BaseGame.labelStyle)
        bonusDescriptionLabel.setFontScale(.25f)
        bonusDescriptionLabel.setWrap(true)
        bonusDescriptionLabel.setAlignment(Align.center)
        lowerLeftTable.add(bonusLabel).padBottom(10f).row()
        lowerLeftTable.add(bonusDescriptionLabel).width(Gdx.graphics.width * .3f)

        val lowerRightTable = Table()
        lowerRightTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(.2f, .2f, .2f, .9f))
        claimAscensionPointsLabel = Label("${BaseGame.currentAscensionPoints}", BaseGame.labelStyle)
        claimAscensionPointsLabel.setFontScale(.5f)
        val claimedDescriptionLabel = Label("Ascension points claimed when restart", BaseGame.labelStyle)
        claimedDescriptionLabel.setFontScale(.25f)
        claimedDescriptionLabel.setWrap(true)
        claimedDescriptionLabel.setAlignment(Align.center)

        val button = TextButton("Claim", BaseGame.textButtonStyle)
        button.isTransform = true
        button.scaleBy(-.2f)
        button.setOrigin(Align.center)
        button.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                warningTable.isVisible = !warningTable.isVisible
            false
        }

        val restartLabel = Label("Restart your love", BaseGame.labelStyle)
        restartLabel.setFontScale(.3f)
        restartLabel.setWrap(true)
        restartLabel.setAlignment(Align.center)
        lowerRightTable.add(claimAscensionPointsLabel).row()
        lowerRightTable.add(claimedDescriptionLabel).width(Gdx.graphics.width * .3f).row()
        lowerRightTable.add(button).row()
        lowerRightTable.add(restartLabel)

        // table layout
        val mainTable = Table()
        mainTable.setFillParent(true)

        mainTable.add(upperTable).fillX().colspan(2)
        mainTable.row()
        mainTable.add(table).colspan(2)
        mainTable.row()
        mainTable.add(lowerLeftTable).height(Gdx.graphics.height * .3f)
        mainTable.add(lowerRightTable).height(Gdx.graphics.height * .3f)
        // mainTable.debug = true

        val stack = Stack() // stack allows for scene2d elements to overlap each other
        stack.add(mainTable)
        stack.add(infoTable)
        stack.add(warningTable)
        stack.setFillParent(true)
        mainTable.align(Align.top)

        mainStage.addActor(stack)
    }

    override fun update(dt: Float) {
        claimAscensionPointsLabel.setText("${BaseGame.claimAscensionPoints}")
        currentAscensionPointsLabel.setText("${BaseGame.currentAscensionPoints}")

        for (generator in BaseGame.resourceGenerators)
            generator.act(Gdx.graphics.deltaTime)

        time += dt
        if (time > 1) { // every second
            time = 0f
            GameUtils.calculateAscension()
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if(keycode == Input.Keys.BACK)
            BaseGame.setActiveScreen(LevelScreen())
        return false;
    }
}
