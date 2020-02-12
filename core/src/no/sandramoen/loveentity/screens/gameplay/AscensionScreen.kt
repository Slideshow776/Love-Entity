package no.sandramoen.loveentity.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
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

    private lateinit var exitButton: Button
    private lateinit var claimButton: TextButton
    private lateinit var infoButton: Button
    private lateinit var infoTable: Table
    private lateinit var warningTable: Table

    private lateinit var titleLabel: Label
    private lateinit var infoLabel: Label
    private lateinit var warningLabel: Label
    private lateinit var warningButton: TextButton
    private lateinit var subtitleLabel: Label
    private lateinit var descriptionLabel: Label
    private lateinit var ascensionDescriptionLabel: Label
    private lateinit var bonusDescriptionLabel: Label
    private lateinit var claimedDescriptionLabel: Label
    private lateinit var restartLabel: Label

    override fun initialize() {
        titleLabel = Label("Ascension", BaseGame.labelStyle)
        titleLabel.setFontScale(.7f)

        // main exit button
        val exitButtonStyle = Button.ButtonStyle()
        exitButtonStyle.up = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("cross-white")))
        exitButton = Button(exitButtonStyle)
        exitButton.isTransform = true
        exitButton.scaleBy(-.5f)
        exitButton.setOrigin(Align.top)
        exitButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                BaseGame.setActiveScreen(LevelScreen())
            false
        }

        // info table
        infoLabel = Label("Cupcake ipsum dolor sit amet. Gingerbread marshmallow sugar plum pastry dragée gingerbread candy cookie. Bonbon dessert tiramisu dragée.\n" +
                "\n" + "Powder jelly-o lollipop. Cookie chupa chups powder cake muffin pudding. Soufflé cupcake chocolate apple pie danish toffee dessert powder. Cake pudding jelly cake jelly tootsie roll.", BaseGame.labelStyle)
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
        infoButton = Button(infoButtonStyle)
        infoButton.color = Color(95 / 255f, 152 / 255f, 209 / 255f, 1f) // grey blue
        infoButton.isTransform = true
        infoButton.setOrigin(Align.center)
        infoButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                infoTable.isVisible = !infoTable.isVisible
            false
        }

        // warning table
        warningLabel = Label("You have ${BaseGame.claimAscensionPoints} ascension points! Resetting now is a bad idea. Just keep earning love!", BaseGame.labelStyle)
        if (BaseGame.claimAscensionPoints > 0) {
            if (BaseGame.english)
                warningLabel.setText("You have ${BaseGame.claimAscensionPoints} ascension points ready to boost your love! To claim them you will need to reset your progress.")
            else
                warningLabel.setText("Du har ${BaseGame.claimAscensionPoints} oppløftningspoeng klar til å øke din kjærlighet! For å innfri disse må du starte på nytt igjen.")
        }
        warningLabel.color = Color.PURPLE
        warningLabel.setWrap(true)
        warningLabel.setFontScale(.3f)

        warningButton = TextButton("Reset now!", BaseGame.textButtonStyle)
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

        warningTable = Table()
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
        subtitleLabel = Label("Ascend to a greater compassion level!", BaseGame.labelStyle)
        subtitleLabel.setFontScale(.4f)
        subtitleLabel.color = Color.PURPLE
        descriptionLabel = Label("The more love you earn, the more Ascension points you realize! When claimed increase your capacity for love, but you'll need to start over. (Psst! It's worth it!)", BaseGame.labelStyle)
        descriptionLabel.setWrap(true)
        descriptionLabel.setFontScale(.3f)
        descriptionLabel.setAlignment(Align.center)
        ascensionDescriptionLabel = Label("Your Total Ascension Points", BaseGame.labelStyle)
        ascensionDescriptionLabel.setFontScale(.55f)
        ascensionDescriptionLabel.color = Color.PURPLE
        currentAscensionPointsLabel = Label("${BaseGame.currentAscensionPoints}", BaseGame.labelStyle)
        currentAscensionPointsLabel.setFontScale(.55f)

        val table = Table()
        table.add(subtitleLabel).padTop(25f)
        table.add(infoButton).width(Gdx.graphics.width * .06f).height(Gdx.graphics.width * .06f).row()
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
        bonusDescriptionLabel = Label("Compassion bonus per ascension point", BaseGame.labelStyle)
        bonusDescriptionLabel.setFontScale(.25f)
        bonusDescriptionLabel.setWrap(true)
        bonusDescriptionLabel.setAlignment(Align.center)
        lowerLeftTable.add(bonusLabel).padBottom(10f).row()
        lowerLeftTable.add(bonusDescriptionLabel).width(Gdx.graphics.width * .3f)

        val lowerRightTable = Table()
        lowerRightTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(.2f, .2f, .2f, .9f))
        claimAscensionPointsLabel = Label("${BaseGame.currentAscensionPoints}", BaseGame.labelStyle)
        claimAscensionPointsLabel.setFontScale(.5f)
        claimedDescriptionLabel = Label("Ascension points claimed when restart", BaseGame.labelStyle)
        claimedDescriptionLabel.setFontScale(.25f)
        claimedDescriptionLabel.setWrap(true)
        claimedDescriptionLabel.setAlignment(Align.center)

        claimButton = TextButton("Claim", BaseGame.textButtonStyle)
        claimButton.isTransform = true
        claimButton.scaleBy(-.2f)
        claimButton.setOrigin(Align.center)
        claimButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                warningTable.isVisible = !warningTable.isVisible
            false
        }

        restartLabel = Label("Restart your love", BaseGame.labelStyle)
        restartLabel.setFontScale(.3f)
        restartLabel.setWrap(true)
        restartLabel.setAlignment(Align.center)
        lowerRightTable.add(claimAscensionPointsLabel).row()
        lowerRightTable.add(claimedDescriptionLabel).width(Gdx.graphics.width * .3f).row()
        lowerRightTable.add(claimButton).row()
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
        checkLanguage()
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

        if (infoTable.isVisible || warningTable.isVisible) {
            exitButton.touchable = Touchable.disabled
            claimButton.touchable = Touchable.disabled
            infoButton.touchable = Touchable.disabled
        } else {
            exitButton.touchable = Touchable.enabled
            claimButton.touchable = Touchable.enabled
            infoButton.touchable = Touchable.enabled
        }

        /*if (warningTable.isVisible) {
            infoButton.touchable = Touchable.disabled
            exitButton.touchable = Touchable.disabled
            claimButton.touchable = Touchable.disabled
        } else {
            infoButton.touchable = Touchable.enabled
            exitButton.touchable = Touchable.enabled
            claimButton.touchable = Touchable.enabled
        }*/
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK)
            BaseGame.setActiveScreen(LevelScreen())
        return false;
    }

    private fun checkLanguage() {
        if (BaseGame.english) {
            titleLabel.setText("Ascension")
            infoLabel.setText("Cupcake ipsum dolor sit amet. Gingerbread marshmallow sugar plum pastry dragée gingerbread candy cookie. Bonbon dessert tiramisu dragée.\n" +
                    "\n" + "Powder jelly-o lollipop. Cookie chupa chups powder cake muffin pudding. Soufflé cupcake chocolate apple pie danish toffee dessert powder. Cake pudding jelly cake jelly tootsie roll.")
            warningLabel.setText("You have ${BaseGame.claimAscensionPoints} ascension points! Resetting now is a bad idea. Just keep earning love!")
            warningButton.setText("Reset now!")
            subtitleLabel.setText("Ascend to a greater compassion level!")
            descriptionLabel.setText("The more love you earn, the more Ascension points you realize! When claimed increase your capacity for love, but you'll need to start over. (Psst! It's worth it!)")
            ascensionDescriptionLabel.setText("Your Total Ascension Points")
            bonusDescriptionLabel.setText("Compassion bonus per ascension point")
            claimedDescriptionLabel.setText("Ascension points claimed when restart")
            claimButton.setText("Claim")
            restartLabel.setText("Restart your love")
        } else {
            titleLabel.setText("Oppløftning")
            infoLabel.setText("Cupcake ipsum dolor sit amet. Gingerbread marshmallow sugar plum pastry dragée gingerbread candy cookie. Bonbon dessert tiramisu dragée.\n" +
                    "\n" + "Powder jelly-o lollipop. Cookie chupa chups powder cake muffin pudding. Soufflé cupcake chocolate apple pie danish toffee dessert powder. Cake pudding jelly cake jelly tootsie roll.")
            warningLabel.setText("Du har ${BaseGame.claimAscensionPoints} oppløftningspoeng! En omstart nå er en dårlig idé. Fortsett å tjen mer kjærlighet!")
            warningButton.setText("Omstart nå!")
            subtitleLabel.setText("Løft deg opp til et høyere medfølelsenivå")
            descriptionLabel.setText("Jo mer du tjener, jo mere oppløftningspoeng får du! Når du innfrir dem øker du din kapasitet for kjærlighet, men du må starte helt omigjen. (Psst! Det er verdt det!)")
            ascensionDescriptionLabel.setText("Dine Oppløftningspoeng")
            bonusDescriptionLabel.setText("Medfølelse bonus per oppløftningspoeng")
            claimedDescriptionLabel.setText("Oppløftningspoeng du får ved omstart")
            claimButton.setText("Innfri")
            restartLabel.setText("Elsk igjen")
        }
    }
}
