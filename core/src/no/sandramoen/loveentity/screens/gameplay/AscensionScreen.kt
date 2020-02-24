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

    private lateinit var titleImage: Image
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
        if (BaseGame.english)
            titleImage = Image(BaseGame.textureAtlas!!.findRegion("bannerAscension"))
        else
            titleImage = Image(BaseGame.textureAtlas!!.findRegion("bannerOppløftning"))

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
        if (BaseGame.currentAscensionPoints > 0) {
            if (BaseGame.english)
                warningLabel.setText("You have ${BaseGame.claimAscensionPoints} ascension points ready to boost your love! To claim them you will need to reset your progress.")
            else
                warningLabel.setText("Du har ${BaseGame.claimAscensionPoints} oppløftningspoeng klar til å øke din kjærlighet! For å innfri disse må du starte på nytt igjen.")
        }
        warningLabel.color = Color.PURPLE
        warningLabel.setWrap(true)
        warningLabel.setFontScale(.4f)
        warningLabel.setAlignment(Align.center)

        warningButton = TextButton("Reset now!", BaseGame.textButtonStyle)
        warningButton.isTransform = true
        // warningButton.scaleBy(-.2f)
        if (BaseGame.claimAscensionPoints > 0)
            warningButton.color = Color.ORANGE
        else
            warningButton.color = Color.LIGHT_GRAY
        warningButton.setOrigin(Align.center)
        warningButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                if (BaseGame.claimAscensionPoints > 0) {
                    BaseGame.currentAscensionPoints += BaseGame.claimAscensionPoints
                    BaseGame.claimAscensionPoints = 0
                    currentAscensionPointsLabel.setText("${BaseGame.currentAscensionPoints}")
                    claimAscensionPointsLabel.setText("${BaseGame.claimAscensionPoints}")
                    GameUtils.reset()
                    warningTable.isVisible = !warningTable.isVisible
                    checkLanguage()
                    warningButton.color = Color.LIGHT_GRAY
                }
            }
            false
        }

        val warningExitButtonStyle = Button.ButtonStyle()
        warningExitButtonStyle.up = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("cross-black")))
        val warningExitButton = Button(warningExitButtonStyle)
        warningExitButton.isTransform = true
        warningExitButton.scaleBy(-.25f)
        warningExitButton.setOrigin(Align.bottomRight)
        warningExitButton.addListener(object : ActorGestureListener() {
            override fun tap(event: InputEvent?, x: Float, y: Float, count: Int, button: Int) {
                warningTable.isVisible = !warningTable.isVisible
            }
        })

        val whiteWarningTable = Table()
        whiteWarningTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(.95f, .95f, .95f, 1f))
        whiteWarningTable.add(warningExitButton).padLeft(Gdx.graphics.width * .6f).row()
        whiteWarningTable.add(warningLabel).width(Gdx.graphics.width * .6f).row()
        whiteWarningTable.add(warningButton).padBottom(Gdx.graphics.height * .025f)
        whiteWarningTable.align(Align.bottom)
        // whiteWarningTable.debug = true

        warningTable = Table()
        warningTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(0f, 0f, 0f, .8f))
        warningTable.isVisible = false
        warningTable.add(whiteWarningTable).width(Gdx.graphics.width * .75f).height(Gdx.graphics.height * .32f).row()
        // warningTable.debug = true

        // upper table
        subtitleLabel = Label("Ascend to a greater compassion level!", BaseGame.labelStyle)
        subtitleLabel.setFontScale(.4f)
        subtitleLabel.color = Color.PURPLE
        descriptionLabel = Label("The more love you earn, the more Ascension points you realize! When claimed increases your capacity for love, but you'll need to start over. (Psst! It's worth it!)", BaseGame.labelStyle)
        descriptionLabel.setWrap(true)
        descriptionLabel.setFontScale(.3f)
        descriptionLabel.setAlignment(Align.center)
        ascensionDescriptionLabel = Label("Your Total Ascension Points", BaseGame.labelStyle)
        ascensionDescriptionLabel.setFontScale(.55f)
        ascensionDescriptionLabel.color = Color.PURPLE
        currentAscensionPointsLabel = Label("${BaseGame.currentAscensionPoints}", BaseGame.labelStyle)
        currentAscensionPointsLabel.setFontScale(.75f)

        val table = Table()
        table.add(subtitleLabel).padTop(Gdx.graphics.height * .01f).row()
        // table.add(infoButton).width(Gdx.graphics.width * .06f).height(Gdx.graphics.width * .06f).row()
        table.add(descriptionLabel).right().padBottom(Gdx.graphics.height * .05f).width(Gdx.graphics.width * .85f).row()
        table.add(ascensionDescriptionLabel).row()
        table.add(currentAscensionPointsLabel).padBottom(Gdx.graphics.height * .01f).row()
        // table.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(.05f, .05f, .05f, 1f))
        // table.debug = true

        val upperTable = Table()
        upperTable.add(titleImage).width(Gdx.graphics.width * .95f).height(Gdx.graphics.height * .1f).row()
        // upperTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(.2f, .2f, .2f, 1f))
        // upperTable.debug = true

        val exitTable = Table()
        exitTable.setFillParent(true)
        exitTable.add(exitButton).top().right().pad(Gdx.graphics.width * .01f)
        uiTable.add(exitTable)

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
        claimAscensionPointsLabel.setFontScale(.75f)
        claimedDescriptionLabel = Label("Ascension points claimed when restart", BaseGame.labelStyle)
        claimedDescriptionLabel.setFontScale(.25f)
        claimedDescriptionLabel.setWrap(true)
        claimedDescriptionLabel.setAlignment(Align.center)

        claimButton = TextButton("Claim", BaseGame.textButtonStyle)
        claimButton.isTransform = true
        claimButton.scaleBy(-.2f)
        claimButton.setOrigin(Align.center)
        claimButton.color = Color.ORANGE
        claimButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                warningTable.isVisible = !warningTable.isVisible
            false
        }

        restartLabel = Label("Restart your love", BaseGame.labelStyle)
        restartLabel.setFontScale(.4f)
        restartLabel.setWrap(true)
        restartLabel.setAlignment(Align.center)
        lowerRightTable.add(claimAscensionPointsLabel).row()
        lowerRightTable.add(claimedDescriptionLabel).width(Gdx.graphics.width * .3f).row()
        lowerRightTable.add(claimButton).row()
        lowerRightTable.add(restartLabel).padTop(Gdx.graphics.height * .01f)

        // table layout
        val mainTable = Table()
        mainTable.setFillParent(true)

        mainTable.add(upperTable).fillX().colspan(2)
        mainTable.row()
        mainTable.add(table).colspan(2)
        mainTable.row()
        mainTable.add(lowerLeftTable).height(Gdx.graphics.height * .3f).width(Gdx.graphics.width * .45f)
        mainTable.add(lowerRightTable).height(Gdx.graphics.height * .3f).width(Gdx.graphics.width * .45f)
        // mainTable.debug = true

        val stack = Stack() // stack allows for scene2d elements to overlap each other
        stack.add(mainTable)
        stack.add(infoTable)
        stack.add(warningTable)
        stack.setFillParent(true)
        mainTable.align(Align.top)

        mainStage.addActor(stack)
        checkLanguage()
        GameUtils.fadeOut(uiStage)
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
        return false
    }

    private fun checkLanguage() {
        if (BaseGame.english) {
            titleImage = Image(BaseGame.textureAtlas!!.findRegion("bannerAscension"))
            infoLabel.setText("Cupcake ipsum dolor sit amet. Gingerbread marshmallow sugar plum pastry dragée gingerbread candy cookie. Bonbon dessert tiramisu dragée.\n" +
                    "\n" + "Powder jelly-o lollipop. Cookie chupa chups powder cake muffin pudding. Soufflé cupcake chocolate apple pie danish toffee dessert powder. Cake pudding jelly cake jelly tootsie roll.")
            if (BaseGame.claimAscensionPoints > 0) {
                warningLabel.setText("You have ${BaseGame.claimAscensionPoints} ascension points ready to boost your love! To claim them you will need to reset your progress.")
                warningButton.color = Color.ORANGE
            } else {
                warningLabel.setText("You have ${BaseGame.claimAscensionPoints} ascension points! Resetting now is a bad idea. Just keep earning love!")
                warningButton.color = Color.LIGHT_GRAY
            }
            warningButton.setText("Reset now!")
            subtitleLabel.setText("Ascend to a greater compassion level!")
            descriptionLabel.setText("The more love you earn, the more Ascension points you realize! When claimed increases your capacity for love, but you'll need to start over. (Psst! It's worth it!)")
            ascensionDescriptionLabel.setText("Your Total Ascension Points")
            bonusDescriptionLabel.setText("Compassion bonus per ascension point")
            claimedDescriptionLabel.setText("Ascension points claimed when restart")
            claimButton.setText("Claim")
            restartLabel.setText("Restart your love")
        } else {
            titleImage = Image(BaseGame.textureAtlas!!.findRegion("bannerOppløftning"))
            infoLabel.setText("Cupcake ipsum dolor sit amet. Gingerbread marshmallow sugar plum pastry dragée gingerbread candy cookie. Bonbon dessert tiramisu dragée.\n" +
                    "\n" + "Powder jelly-o lollipop. Cookie chupa chups powder cake muffin pudding. Soufflé cupcake chocolate apple pie danish toffee dessert powder. Cake pudding jelly cake jelly tootsie roll.")
            if (BaseGame.currentAscensionPoints > 0) {
                warningLabel.setText("Du har ${BaseGame.claimAscensionPoints} oppløftningspoeng klar til å øke din kjærlighet! For å innfri disse må du starte på nytt igjen.")
                warningButton.color = Color.ORANGE
            } else {
                warningLabel.setText("Du har ${BaseGame.claimAscensionPoints} oppløftningspoeng! En omstart nå er en dårlig idé. Fortsett å tjen mer kjærlighet!")
                warningButton.color = Color.LIGHT_GRAY
            }
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
