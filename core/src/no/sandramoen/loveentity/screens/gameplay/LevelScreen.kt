package no.sandramoen.loveentity.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import no.sandramoen.loveentity.actors.*
import no.sandramoen.loveentity.screens.shell.IntroScreen
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.BaseScreen
import no.sandramoen.loveentity.utils.GameUtils
import java.math.BigInteger

class LevelScreen : BaseScreen() {
    private lateinit var loveLabel: Label
    private lateinit var burgerButton: Button
    private lateinit var communityLeadersButton: TextButton
    private lateinit var upgradesButton: TextButton
    private lateinit var ascensionButton: TextButton
    private lateinit var unlocksButton: TextButton
    private lateinit var buyButtonLabel: Label
    private lateinit var quickLoveLabel: Label

    private var revealNextGenerator = false

    private lateinit var table: Table
    private lateinit var burgerTable: Table
    private var burgerMenuActive = false

    private var time = 0f

    private lateinit var quickLoveButton: Button
    private lateinit var quickLoveButtonStyle: Button.ButtonStyle
    private lateinit var quickLoveNumberLabel: Label
    private lateinit var quickLoveList: Array<ResourceGenerator>

    private lateinit var buyButton: Button
    private lateinit var buyAmountLabel: Label

    private lateinit var debugButton1: TextButton
    private lateinit var debugButton2: TextButton
    private lateinit var debugButton3: TextButton
    private lateinit var debugButton4: TextButton
    private lateinit var debugButton5: TextButton
    private lateinit var debugLabel: Label

    private lateinit var viewIntroButton: TextButton
    private lateinit var languageButton: TextButton
    private lateinit var scaleButton: TextButton
    private var allyUnlocks = Array<Unlock>()

    private lateinit var heart: Heart
    private lateinit var rainbowVeil: RainbowVeil

    override fun initialize() {
        heart = Heart(0f, 0f, mainStage)
        rainbowVeil = RainbowVeil(0f, 0f, mainStage)

        if (BaseGame.resourceGenerators.size != 10) {
            initializeAssets()
        }

        for (generator in BaseGame.resourceGenerators)
            generator.enable()


        /* debug options------------------------------------------------------------------------------------------ */
        debugButton1 = TextButton("Add 1k love", BaseGame.textButtonStyle)
        debugButton2 = TextButton("Add 100k love", BaseGame.textButtonStyle)
        debugButton3 = TextButton("Add 1M love", BaseGame.textButtonStyle)
        debugButton4 = TextButton("Add 1T love", BaseGame.textButtonStyle)
        debugButton5 = TextButton("Restart", BaseGame.textButtonStyle)

        debugButton1.label.color = Color.GREEN
        debugButton1.color = Color.GRAY
        debugButton1.label.setFontScale(.6f)
        debugButton1.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.love = BaseGame.love.add(BigInteger("1000"))
                BaseGame.lifeTimeLove = BaseGame.lifeTimeLove.add(BigInteger("1000"))
                update(Gdx.graphics.deltaTime) // updates hidetable visibilities
            }
            false
        }
        debugButton2.label.color = Color.GREEN
        debugButton2.color = Color.GRAY
        debugButton2.label.setFontScale(.6f)
        debugButton2.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.love = BaseGame.love.add(BigInteger("100000"))
                BaseGame.lifeTimeLove = BaseGame.lifeTimeLove.add(BigInteger("100000"))
                update(Gdx.graphics.deltaTime) // updates hidetable visibilities
            }
            false
        }
        debugButton3.label.color = Color.GREEN
        debugButton3.color = Color.GRAY
        debugButton3.label.setFontScale(.6f)
        debugButton3.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.love = BaseGame.love.add(BigInteger("1000000"))
                BaseGame.lifeTimeLove = BaseGame.lifeTimeLove.add(BigInteger("1000000"))
                update(Gdx.graphics.deltaTime) // updates hidetable visibilities
            }
            false
        }
        debugButton4.label.color = Color.GREEN
        debugButton4.color = Color.GRAY
        debugButton4.label.setFontScale(.6f)
        debugButton4.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.love = BaseGame.love.add(BigInteger("1000000000000000000"))
                BaseGame.lifeTimeLove = BaseGame.love.add(BigInteger("1000000000000000000"))
                update(Gdx.graphics.deltaTime) // updates hidetable visibilities
            }
            false
        }
        debugButton5.label.color = Color.GREEN
        debugButton5.color = Color.GRAY
        debugButton5.label.setFontScale(.6f)
        debugButton5.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) { // the restart button
                BaseGame.currentAscensionPoints = 0
                GameUtils.reset() // resets a bunch of stuff
                quickLoveList.clear()
                checkLanguage()
                initializeAssets()

                table.reset()
                val heartTable = Table()
                heartTable.add(heart).padBottom(Gdx.graphics.height * .06f)
                val veilTable = Table().top()
                veilTable.add(rainbowVeil).expand().fill().bottom()

                val swirlTable1 = Table()
                val glowSwirl1 = GlowSwirl(0f, 0f, mainStage)
                glowSwirl1.addAction(Actions.forever(Actions.rotateBy(.15f)))
                swirlTable1.add(glowSwirl1).padBottom(Gdx.graphics.height * .1f)

                val stack = Stack()
                stack.add(swirlTable1)
                stack.add(heartTable)
                stack.add(veilTable)

                table.add(stack).padBottom(Gdx.graphics.height * .1f).padTop(Gdx.graphics.height * .1f).row()
                for (i in 0 until 2) {
                    table.add(BaseGame.resourceGenerators[i]).padBottom(Gdx.graphics.height * .1f).row()
                    BaseGame.resourceGenerators[i].isVisible = true // solves a visibility bug
                }
                update(Gdx.graphics.deltaTime) // updates hidetable visibilities
                GameUtils.saveGameState()

                buyButton.isVisible = false
                buyButton.addAction(Actions.fadeOut(0f))
                buyButton.touchable = Touchable.enabled
            }
            false
        }

        debugLabel = Label("These are debug options", BaseGame.labelStyle)
        debugLabel.color = Color(0 / 255f, 153 / 255f, 0 / 255f, 1f)
        debugLabel.setFontScale(.25f)

        burgerTable = Table()
        burgerTable.add(debugLabel).colspan(3).row()
        burgerTable.add(debugButton1).colspan(3).row()
        burgerTable.add(debugButton2).colspan(3).row()
        burgerTable.add(debugButton3).colspan(3).row()
        burgerTable.add(debugButton4).colspan(3).row()
        burgerTable.add(debugButton5).colspan(3).padBottom(50f).row()
        /* ------------------------------------------------------------------------------------------------------- */

        // language ui setup
        viewIntroButton = TextButton("Change to Norwegian", BaseGame.textButtonStyle)
        viewIntroButton.color = Color(224 / 255f, 224 / 255f, 224 / 255f, 1f) // light light gray
        viewIntroButton.label.color = Color.BLUE // (135 / 255f, 200 / 255f, 255 / 255f, 1f)
        viewIntroButton.isTransform = true
        viewIntroButton.setOrigin(Align.center)
        viewIntroButton.label.setFontScale(.8f)
        viewIntroButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                BaseGame.setActiveScreen(IntroScreen())
            false
        }

        languageButton = TextButton("Change to Norwegian", BaseGame.textButtonStyle)
        languageButton.color = Color(224 / 255f, 224 / 255f, 224 / 255f, 1f) // light light gray
        languageButton.label.color = Color.BLUE // (135 / 255f, 200 / 255f, 255 / 255f, 1f)
        languageButton.isTransform = true
        languageButton.setOrigin(Align.center)
        languageButton.label.setFontScale(.8f)
        languageButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.english = !BaseGame.english
                checkLanguage()
                checkScale()
                for (generator in BaseGame.resourceGenerators) {
                    generator.checkLanguage()
                    generator.checkScale()
                }
                for (leader in BaseGame.communityLeaders) {
                    leader.checkLanguage()
                    leader.checkScale()
                }
                for (upgrade in BaseGame.upgrades) {
                    upgrade.checkLanguage()
                    upgrade.checkScale()
                }
            }
            false
        }

        // scale ui setup
        scaleButton = TextButton("Change to short scale", BaseGame.textButtonStyle)
        scaleButton.color = Color(224 / 255f, 224 / 255f, 224 / 255f, 1f) // light light gray
        scaleButton.label.color = Color.BLUE // Color(135 / 255f, 200 / 255f, 255 / 255f, 1f)
        scaleButton.isTransform = true
        scaleButton.setOrigin(Align.center)
        scaleButton.label.setFontScale(.8f)
        scaleButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.longScale = !BaseGame.longScale
                checkScale()
                for (generator in BaseGame.resourceGenerators)
                    generator.checkScale()
            }
            false
        }

        // burger table
        burgerTable.add(viewIntroButton).row()
        burgerTable.add(languageButton).row()
        burgerTable.add(scaleButton).padBottom(20f).row()

        communityLeadersButton = TextButton("Community Leaders", BaseGame.textButtonStyle)
        communityLeadersButton.color = Color(224 / 255f, 224 / 255f, 224 / 255f, 1f) // light light gray
        communityLeadersButton.label.color = Color.PURPLE //(255/255f, 204/255f, 255/255f, 1f) // light pink
        communityLeadersButton.isTransform = true
        communityLeadersButton.setOrigin(Align.center)
        communityLeadersButton.label.setFontScale(.8f)
        communityLeadersButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                fadeToScreen(CommunityLeadersScreen())
            false
        }

        burgerTable.add(communityLeadersButton).row()

        upgradesButton = TextButton("Upgrades", BaseGame.textButtonStyle)
        upgradesButton.color = Color(224 / 255f, 224 / 255f, 224 / 255f, 1f) // light light gray
        upgradesButton.label.color = Color.PURPLE //(255/255f, 204/255f, 255/255f, 1f) // light pink
        upgradesButton.isTransform = true
        upgradesButton.setOrigin(Align.center)
        upgradesButton.label.setFontScale(.8f)
        upgradesButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                fadeToScreen(UpgradesScreen())
            false
        }
        burgerTable.add(upgradesButton).row()

        ascensionButton = TextButton("Ascension", BaseGame.textButtonStyle)
        ascensionButton.color = Color(224 / 255f, 224 / 255f, 224 / 255f, 1f) // light light gray
        ascensionButton.label.color = Color.PURPLE //(255/255f, 204/255f, 255/255f, 1f) // light pink
        ascensionButton.isTransform = true
        ascensionButton.setOrigin(Align.center)
        ascensionButton.label.setFontScale(.8f)
        ascensionButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                if (BaseGame.claimAscensionPoints > BaseGame.currentAscensionPoints * 2f)
                    BaseGame.wiggleAscension = false
                fadeToScreen(AscensionScreen())
            }
            false
        }
        burgerTable.add(ascensionButton).row()

        unlocksButton = TextButton("Unlocks", BaseGame.textButtonStyle)
        unlocksButton.color = Color(224 / 255f, 224 / 255f, 224 / 255f, 1f) // light light gray
        unlocksButton.label.color = Color.PURPLE //Color(255/255f, 204/255f, 255/255f, 1f) // light pink
        unlocksButton.label.setFontScale(.8f)
        unlocksButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                fadeToScreen(UnlocksScreen())
            false
        }
        burgerTable.add(unlocksButton)

        burgerTable.isVisible = false

        // burger button
        val burgerButtonStyle = Button.ButtonStyle()
        burgerButtonStyle.up = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("burger")))
        burgerButton = Button(burgerButtonStyle)
        burgerButton.isTransform = true
        burgerButton.scaleBy(.4f, .4f)
        burgerButton.setOrigin(Align.center)
        burgerButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                if (burgerMenuActive) {
                    burgerButtonStyle.up = TextureRegionDrawable(BaseGame.textureAtlas!!.findRegion("burger"))
                    uiTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(0f, 0f, 0f, .0f))
                    burgerTable.addAction(Actions.sequence(
                            Actions.fadeOut(.125f),
                            Actions.run { burgerTable.isVisible = false }
                    ))
                    for (generator in BaseGame.resourceGenerators)
                        generator.enable()
                    if (buyButton.isVisible) {
                        buyButton.touchable = Touchable.enabled
                        buyButton.color.a = 1f
                        buyButtonLabel.color.a = 1f
                        buyAmountLabel.color.a = 1f
                    }
                    quickLoveButton.touchable = Touchable.enabled
                    quickLoveButton.color.a = 1f
                    quickLoveLabel.color.a = 1f
                    quickLoveNumberLabel.color.a = 1f
                } else {
                    burgerButtonStyle.up = TextureRegionDrawable(BaseGame.textureAtlas!!.findRegion("cross-white"))
                    uiTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(0f, 0f, 0f, .75f))
                    burgerTable.isVisible = true
                    burgerTable.addAction(Actions.sequence(
                            Actions.fadeOut(0f),
                            Actions.fadeIn(.125f)
                    ))
                    burgerButton.clearActions()
                    burgerButton.color = Color.WHITE
                    burgerButton.rotation = 0f
                    for (generator in BaseGame.resourceGenerators)
                        generator.disable()

                    if (buyButton.isVisible) {
                        buyButton.touchable = Touchable.disabled
                        buyButton.color.a = .75f
                        buyButtonLabel.color.a = .75f
                        buyAmountLabel.color.a = .75f
                    }
                    quickLoveButton.touchable = Touchable.disabled
                    quickLoveButton.color.a = .75f
                    quickLoveLabel.color.a = .75f
                    quickLoveNumberLabel.color.a = .75f
                }
                burgerMenuActive = !burgerMenuActive
                heart.disabled = !heart.disabled
            }
            false
        }

        val uiToggleTable = Table()
        uiToggleTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(0f, 0f, 0f, .75f))
        uiToggleTable.add(burgerButton).pad(Gdx.graphics.height * .015f)

        // quick love
        quickLoveList = Array()
        quickLoveButtonStyle = Button.ButtonStyle()
        quickLoveButtonStyle.up = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("info")))
        quickLoveButton = Button(quickLoveButtonStyle)
        quickLoveButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                var highest = 0f
                var generatorToBeActivated: ResourceGenerator? = null
                for (generator in quickLoveList) {
                    if (generator.incomeTime > highest) {
                        generatorToBeActivated = generator
                        highest = generator.incomeTime
                    }
                }
                if (generatorToBeActivated != null) {
                    generatorToBeActivated!!.activated = true
                    generatorToBeActivated.createAndStartStarEffect(
                            generatorToBeActivated.activateButton.x,
                            generatorToBeActivated.activateButton.y,
                            generatorToBeActivated.activateButton.width,
                            generatorToBeActivated.activateButton.height
                    )
                }
            }
            false
        }

        quickLoveNumberLabel = Label("${quickLoveList.size}", BaseGame.labelStyle)
        quickLoveNumberLabel.setFontScale(.5f)

        quickLoveLabel = Label("Quick Love!", BaseGame.labelStyle)
        quickLoveLabel.setFontScale(.3f)

        val quickLoveTable = Table()
        quickLoveTable.add(quickLoveNumberLabel).top().right().padRight(10f).padBottom(25f).row()
        quickLoveTable.add(quickLoveLabel).bottom().padTop(35f)
        quickLoveTable.setFillParent(true)
        // qTable.debug = true

        quickLoveButton.addActor(quickLoveTable)
        // quickLoveButton.debug = true

        // buy button
        buyButton = Button()
        val buyButtonStyle = Button.ButtonStyle()
        buyButtonStyle.up = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("button")))
        buyButton = Button(buyButtonStyle)
        buyButton.isTransform = true
        buyButton.setOrigin(Align.center)
        buyButton.color = Color.PURPLE
        if (BaseGame.love >= BigInteger(BaseGame.resourceGenerators[0].baseCost.toString()).multiply(BigInteger("5"))) {
            buyButton.addAction(Actions.fadeIn(0f))
            buyButton.addAction(Actions.after(Actions.run { buyButton.isVisible = true }))
        } else {
            buyButton.addAction(Actions.fadeOut(0f))
            buyButton.addAction(Actions.after(Actions.run { buyButton.isVisible = false }))
        }
        buyButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                var label = ""
                var amount = -1L
                when (BaseGame.buyIndex) {
                    0 -> {
                        label = "x1"
                        amount = 1L
                    }
                    1 -> {
                        label = "x10"
                        amount = 10L
                    }
                    2 -> {
                        label = "x100"
                        amount = 100L
                    }
                    3 -> {
                        if (BaseGame.english) label = "next"
                        else label = "neste"
                        amount = 1
                    }
                    4 -> {
                        if (BaseGame.english) label = "max"
                        else label = "maks"
                        amount = 1L
                        BaseGame.buyIndex = -1
                    }
                }
                for (generator in BaseGame.resourceGenerators) {
                    when (label) {
                        "next", "neste" -> {
                            if (generator.unlockIndex < generator.unlocks.size) {
                                val num = BigInteger((generator.unlocks[generator.unlockIndex].goal).toString()).subtract(generator.owned)
                                generator.nextPurchase(BigInteger(num.toString()))
                            } else
                                generator.nextPurchase(BigInteger.ONE)
                        }
                        "max", "maks" -> {
                            generator.calculateAmountUpToValue(BaseGame.love)
                        }
                        else -> generator.nextPurchase(BigInteger(amount.toString()))
                    }
                }
                buyAmountLabel.setText("$label")
                BaseGame.buyIndex++
            }
            false
        }
        buyButtonLabel = Label("Buy", BaseGame.labelStyle)
        buyButtonLabel.setFontScale(.3f)
        var buyTemp = ""
        when (BaseGame.buyIndex) {
            1 -> buyTemp = "x1"
            2 -> buyTemp = "x10"
            3 -> buyTemp = "x100"
            4 -> {
                if (BaseGame.english) buyTemp = "next"
                else buyTemp = "neste"
            }
            0 -> {
                if (BaseGame.english) buyTemp = "max"
                else buyTemp = "maks"
            }
        }
        buyAmountLabel = Label(buyTemp, BaseGame.labelStyle)
        buyAmountLabel.setFontScale(.5f)

        val buyTable = Table()
        buyTable.add(buyButtonLabel).row()
        buyTable.add(buyAmountLabel)
        buyTable.setFillParent(true)
        // buyTable.debug = true

        buyButton.addActor(buyTable)

        // table layout
        // uiTable
        uiTable.add(buyButton).right().padRight(Gdx.graphics.width * -.016f).width(Gdx.graphics.width * .2f).height(Gdx.graphics.height * .08f).row()
        uiTable.add(burgerTable).fillY().expandY().row()
        uiTable.add(quickLoveButton).right().width(Gdx.graphics.width * .14f).height(Gdx.graphics.height * .07f)
                .padRight(Gdx.graphics.width * .06f).padBottom(Gdx.graphics.height * .015f).row()
        uiTable.add(uiToggleTable).fillX().expandX().bottom()
        // uiTable.debug = true

        // gameplay table
        val heartTable = Table()
        heartTable.add(heart).padBottom(Gdx.graphics.height * .06f)
        val rainbowVeilTable = Table().top()
        rainbowVeilTable.add(rainbowVeil).expand().fill().bottom().padBottom(Gdx.graphics.height * .035f)
        val swirlTable1 = Table()
        val glowSwirl1 = GlowSwirl(0f, 0f, mainStage)
        glowSwirl1.addAction(Actions.forever(Actions.rotateBy(.15f)))
        swirlTable1.add(glowSwirl1).padBottom(Gdx.graphics.height * .1f)

        val heartStack = Stack()
        heartStack.add(swirlTable1)
        heartStack.add(heartTable)
        heartStack.add(rainbowVeilTable)

        table = Table()
        table.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("starBackground")))
        table.add(heartStack).padBottom(Gdx.graphics.height * .05f).padTop(Gdx.graphics.height * .15f).row()
        if (BaseGame.revealNextGeneratorIndex < 1) {
            for (i in 0 until 2) {
                table.add(BaseGame.resourceGenerators[i]).padBottom(Gdx.graphics.height * .1f).row()
                BaseGame.resourceGenerators[i].isVisible = true // solves a visibility bug
            }
        } else {
            for (i in 0 until BaseGame.revealNextGeneratorIndex) {
                table.add(BaseGame.resourceGenerators[i]).padBottom(Gdx.graphics.height * .1f).row()
                BaseGame.resourceGenerators[i].isVisible = true // solves a visibility bug
                BaseGame.resourceGenerators[i].hideTable.isVisible = false
            }
            revealTwoNextGenerators()
        }

        val scrollableTable = Table()
        val scroll = ScrollPane(table)
        scrollableTable.add(scroll)

        val mainTable = Table()
        mainTable.setFillParent(true)

        loveLabel = Label("${GameUtils.presentLongScale(BaseGame.love)} love", BaseGame.labelStyle)
        loveLabel.setFontScale(.55f)
        loveLabel.setWrap(true)
        loveLabel.setAlignment(Align.center)

        val loveLabelTable = Table()
        loveLabelTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(0f, 0f, 0f, .75f))
        loveLabelTable.add(loveLabel).width(Gdx.graphics.width * .69f).padTop(Gdx.graphics.height * .01f).padBottom(Gdx.graphics.height * .01f)
        // loveLabelTable.debug = true
        val loveLabelTableTable = Table()
        loveLabelTableTable.add(loveLabelTable).width(Gdx.graphics.width.toFloat())
        loveLabelTableTable.align(Align.top)

        val scrollStack = Stack()
        scrollStack.add(scrollableTable)
        scrollStack.add(loveLabelTableTable)

        mainTable.add(scrollStack)
        // mainTable.debug = true

        mainStage.addActor(mainTable)

        checkScale()
        checkLanguage()
        GameUtils.fadeOut(uiStage)
    }

    override fun update(dt: Float) {
        if (BaseGame.english)
            if (BaseGame.longScale)
                loveLabel.setText("${GameUtils.presentLongScale(BaseGame.love)} love")
            else
                loveLabel.setText("${GameUtils.presentShortScale(BaseGame.love)} love")
        else
            if (BaseGame.longScale)
                loveLabel.setText("${GameUtils.presentLongScale(BaseGame.love)} kjærlighet")
            else
                loveLabel.setText("${GameUtils.presentShortScale(BaseGame.love)} kjærlighet")

        if (BaseGame.heartTouched)
            mainStage.cancelTouchFocus()

        calculateAscension(dt)
        revealNextGeneratorUpdate()
        checkBuyNextAndMaxUpdate()
        burgerMenuUpdate()
        quickLoveUpdate()
        buyButtonAppearance()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK) {
            GameUtils.saveGameState()
            super.dispose()
            BaseGame.assetManager.dispose()
            BaseGame.fontGenerator.dispose()
            Gdx.app.exit()
        }
        return false;
    }

    private fun calculateAscension(dt: Float) {
        time += dt
        if (time > 1) { // calculate every second
            time = 0f
            GameUtils.calculateAscension()
        }
    }

    private fun revealNextGeneratorUpdate() {
        if (BaseGame.revealNextGeneratorIndex < BaseGame.resourceGenerators.size &&
                BaseGame.love >= BigInteger((BaseGame.resourceGenerators[BaseGame.revealNextGeneratorIndex].baseCost).toString()))
            revealNextGenerator = true

        if (revealNextGenerator)
            revealNextGenerator()
    }

    private fun checkBuyNextAndMaxUpdate() { // check buy next and buy max
        for (generator in BaseGame.resourceGenerators) {
            if (generator.hideTable.isVisible) break // saves some computing
            if (BaseGame.buyIndex == 4) { // next
                if (generator.unlockIndex < generator.unlocks.size) {
                    val num = BigInteger((generator.unlocks[generator.unlockIndex].goal).toString()).subtract(generator.owned)
                    generator.nextPurchase(BigInteger(num.toString()))
                } else
                    generator.nextPurchase(BigInteger.ONE)
            } else if (BaseGame.buyIndex == 0) { // max
                generator.calculateAmountUpToValue(BaseGame.love)
            }
        }
    }

    private fun burgerMenuUpdate() { // burger menu notifications
        for (i in 0 until BaseGame.communityLeaders.size) {
            if (BaseGame.communityLeadersWiggleIndex == i &&
                    !BaseGame.communityLeaders[i].remove &&
                    BaseGame.love >= BaseGame.communityLeaders[i].price) {
                BaseGame.communityLeadersWiggleIndex++ // seen, but not clicked on
                BaseGame.prefs!!.putInteger("communityLeadersWiggleIndex", BaseGame.communityLeadersWiggleIndex)
                if (!burgerTable.isVisible) wiggleButton(burgerButton, true)
                wiggleButton(communityLeadersButton, false)
                break
            }
        }

        for (i in 0 until BaseGame.upgrades.size) {
            if (BaseGame.upgradesWiggleIndex == i &&
                    !BaseGame.upgrades[i].remove &&
                    BaseGame.love >= BaseGame.upgrades[i].price) {
                BaseGame.upgradesWiggleIndex++ // seen, but not clicked on
                BaseGame.prefs!!.putInteger("upgradesWiggleIndex", BaseGame.communityLeadersWiggleIndex)
                if (!burgerTable.isVisible) wiggleButton(burgerButton, true)
                wiggleButton(upgradesButton, false)
                break
            }
        }

        if (BaseGame.claimAscensionPoints > BaseGame.currentAscensionPoints * 2f) {
            if (BaseGame.wiggleAscension && !burgerTable.isVisible) wiggleButton(burgerButton, true)
            wiggleButton(ascensionButton, false)
        }
    }

    private fun quickLoveUpdate() { // quick love button
        quickLoveButton.isVisible = quickLoveList.size > 0
        for (generator in BaseGame.resourceGenerators) {
            if (!generator.hasCommunityLeader && !generator.activated && generator.owned > BigInteger.ZERO) {
                quickLoveButtonStyle.up = TextureRegionDrawable(BaseGame.textureAtlas!!.findRegion(generator.avatar))
                if (!quickLoveList.contains(generator))
                    quickLoveList.add(generator)
                quickLoveNumberLabel.setText("${quickLoveList.size}")
            } else {
                if (quickLoveList.contains(generator))
                    quickLoveList.removeValue(generator, true)
                quickLoveNumberLabel.setText("${quickLoveList.size}")
            }
        }
    }

    private fun revealNextGenerator() {
        if (BaseGame.revealNextGeneratorIndex < BaseGame.resourceGenerators.size) {
            BaseGame.resourceGenerators[BaseGame.revealNextGeneratorIndex].exposeResourceGenerator()

            if (BaseGame.revealNextGeneratorIndex < BaseGame.resourceGenerators.size)
                BaseGame.revealNextGeneratorIndex++
            if (BaseGame.revealNextGeneratorIndex < BaseGame.resourceGenerators.size - 1) {
                table.add(BaseGame.resourceGenerators[BaseGame.revealNextGeneratorIndex + 1]).padBottom(Gdx.graphics.height * .1f).row()
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
                    table.add(BaseGame.resourceGenerators[BaseGame.revealNextGeneratorIndex + i]).padBottom(Gdx.graphics.height * .1f).row()
                    BaseGame.resourceGenerators[BaseGame.revealNextGeneratorIndex + i].isVisible = true
                }
            }
        }
    }

    private fun wiggleButton(button: Button, changeColor: Boolean) {
        if (button.actions.size == 0) {
            if (changeColor) button.addAction(Actions.color(Color.ORANGE, 1f))
            button.addAction(Actions.after(Actions.forever(
                    Actions.sequence(
                            Actions.rotateBy(4f, .25f),
                            Actions.rotateBy(-4f, .25f)
                    )))
            )
        }
    }

    private fun initializeAssets() {
        allyUnlocks = Array()
        allyUnlocks.add(Unlock(25, "speed"), Unlock(50, "speed"), Unlock(100, "speed"))

        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Alliert", "Ally", "avatar-1", "straightAllyFlag", allyUnlocks, 4, 1.07f, 1f, .5f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Biseksuell", "Bisexual", "avatar-5", "bisexualFlag", allyUnlocks, 60, 1.15f, 60f, 3f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Homofil", "Gay", "avatar-3", "bearBrotherhoodFlag", allyUnlocks, 720, 1.14f, 540f, 6f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Lesbisk", "Lesbian", "avatar-4", "lesbianFlag", allyUnlocks, 8640, 1.13f, 4320f, 12f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Ciskjønnet", "Cisgender", "avatar-2", "classicRainbowFlag", allyUnlocks, 103680, 1.12f, 51840f, 24f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Skeiv", "Queer", "avatar-6", "genderqueerFlag", allyUnlocks, 1244160, 1.11f, 622080f, 96f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Transkjønnet", "Transgender", "avatar-7", "transFlag", allyUnlocks, 14929920, 1.1f, 7464960f, 384f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Interkjønnet", "Intersex", "avatar-8", "intersexFlag", allyUnlocks, 179159040, 1.09f, 89579520f, 1536f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Panseksuell", "Pansexual", "avatar-9", "pansexualityFlag", allyUnlocks, 2149908480, 1.08f, 1074954240f, 6144f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Aseksuell", "Asexual", "avatar-10", "asexualFlag", allyUnlocks, 25798901760, 1.07f, 29668737024f, 36864f))

        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 0, "victorErheim", "Victor Erheim", "runs Allies", "administrerer de Allierte", BigInteger("1000")))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 1, "avatar-5", "Name Nameson", "runs Bisexuals", "administrerer de Biseksuelle", BigInteger("15000")))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 2, "avatar-3", "Name Nameson", "runs Gays", "administrerer de Homofile", BigInteger("100000")))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 3, "avatar-4", "Name Nameson", "runs Lesbians", "administrerer de Lesbiske", BigInteger("500000")))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 4, "avatar-2", "Name Nameson", "runs Cisgenders", "administrerer de Ciskjønnede", BigInteger("1200000")))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 5, "VeroNika", "Vero Nika", "runs Queers", "administrerer de Skeive", BigInteger("10000000")))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 6, "luckyLuca", "Lucky Luca", "runs Transgenders", "administrerer de Transkjønnede", BigInteger("111111111")))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 7, "avatar-8", "Name Nameson", "runs Intersexs", "administrerer de Interkjønnede", BigInteger("555555555")))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 8, "avatar-9", "Name Nameson", "runs Pansexuals", "administrerer de Panseksuelle", BigInteger("10000000000")))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 9, "avatar-10", "Name Nameson", "runs Asexuals", "administrerer de Aseksuelle", BigInteger("100000000000")))

        // this system assumes all upgrades are multiplicable of 3's
        if (BaseGame.resourceGenerators[0].upgrade / 3 == (1 / 3)) // first upgrade,
            BaseGame.upgrades.add(Upgrade(mainStage, 0, "Ally Upgrade", "Alliert Oppgradering", "Ally love x3", "Alliert kjærlighet x3", BigInteger("250")))
        if (BaseGame.resourceGenerators[1].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 1, "Bisexual Upgrade", "Biseksuell Oppgradering", "Bisexual love x3", "Biseksuell kjærlighet x3", BigInteger("500")))
        if (BaseGame.resourceGenerators[2].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 2, "Gay Upgrade", "Homofil Oppgradering", "Gay love x3", "Homofil kjærlighet x3", BigInteger("1000000")))
        if (BaseGame.resourceGenerators[3].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 3, "Lesbian Upgrade", "Lesbisk Oppgradering", "Lesbian love x3", "Lesbisk kjærlighet x3", BigInteger("5000000")))
        if (BaseGame.resourceGenerators[4].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 4, "Cisgender Upgrade", "Ciskjønnet Oppgradering", "Cisgender love x3", "Ciskjønnet kjærlighet x3", BigInteger("10000000")))
        if (BaseGame.resourceGenerators[5].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 5, "Queer Upgrade", "Skeiv Oppgradering", "Queer love x3", "Skeiv kjærlighet x3", BigInteger("25000000")))
        if (BaseGame.resourceGenerators[6].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 6, "Transgender Upgrade", "Transkjønnet Oppgradering", "Transgender love x3", "Transkjønnet kjærlighet x3", BigInteger("500000000")))
        if (BaseGame.resourceGenerators[7].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 7, "Intersex Upgrade", "Interkjønnet Oppgradering", "Intersex love x3", "Interkjønnet kjærlighet x3", BigInteger("10000000000")))
        if (BaseGame.resourceGenerators[8].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 8, "Pansexual Upgrade", "Panseksuell Oppgradering", "Pansexual love x3", "Panseksuell kjærlighet x3", BigInteger("250000000000")))
        if (BaseGame.resourceGenerators[9].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 9, "Asexual Upgrade", "Aseksuell Oppgradering", "Asexual love x3", "Aseksuell kjærlighet x3", BigInteger("999999999999999999")))
    }

    private fun checkLanguage() {
        if (BaseGame.english) {
            debugButton1.setText("Add 1k love")
            debugButton2.setText("Add 100k love")
            debugButton3.setText("Add 1M love")
            debugButton4.setText("Add 1T love")
            debugButton5.setText("Restart")
            debugLabel.setText("These are debug options")
            communityLeadersButton.setText("Community Leaders")
            upgradesButton.setText("Upgrades")
            ascensionButton.setText("Ascension")
            unlocksButton.setText("Unlocks")
            quickLoveLabel.setText("Quick Love!")
            buyButtonLabel.setText("Buy")
            languageButton.setText("Change to Norwegian")
            viewIntroButton.setText("View Intro video")
            if (buyAmountLabel.textEquals("maks"))
                buyAmountLabel.setText("max")
            else if (buyAmountLabel.textEquals("neste"))
                buyAmountLabel.setText("next")
            if (BaseGame.longScale)
                scaleButton.setText("Change to short scale")
            else
                scaleButton.setText("Change to long scale")
        } else { // Norwegian
            debugButton1.setText("Legg til 1k kjærlighet")
            debugButton2.setText("Legg til 100k kjærlighet")
            debugButton3.setText("Legg til 1M kjærlighet")
            debugButton4.setText("Legg til 1T kjærlighet")
            debugButton5.setText("Omstart")
            debugLabel.setText("Dette er debug valg")
            communityLeadersButton.setText("Organisasjonsledere")
            upgradesButton.setText("Oppgraderinger")
            ascensionButton.setText("Oppløftning")
            unlocksButton.setText("Oppnåelser")
            quickLoveLabel.setText("Ta en Kjappis!")
            buyButtonLabel.setText("Kjøp")
            languageButton.setText("Bytt til Engelsk")
            viewIntroButton.setText("Se intro video")
            if (buyAmountLabel.textEquals("max"))
                buyAmountLabel.setText("maks")
            else if (buyAmountLabel.textEquals("next"))
                buyAmountLabel.setText("neste")
            if (BaseGame.longScale)
                scaleButton.setText("Bytt til kort skala")
            else
                scaleButton.setText("Bytt til lang skala")
        }
        checkScale()
    }

    private fun checkScale() {
        if (BaseGame.english) {
            if (BaseGame.longScale)
                scaleButton.label.setText("Change to short scale")
            else
                scaleButton.label.setText("Change to long scale")
        } else {
            if (BaseGame.longScale)
                scaleButton.label.setText("Bytt til kort skala")
            else
                scaleButton.label.setText("Bytt til lang skala")
        }
    }

    private fun buyButtonAppearance() {
        if (!buyButton.isVisible && buyButton.actions.size == 0 && BaseGame.love >= BigInteger(BaseGame.resourceGenerators[0].baseCost.toString()).multiply(BigInteger("5"))) {
            buyButton.isVisible = true
            buyButton.addAction(Actions.sequence(
                    Actions.fadeOut(0f),
                    Actions.fadeIn(2f),
                    Actions.rotateBy(4f, .25f),
                    Actions.rotateBy(-4f, .25f),
                    Actions.rotateBy(4f, .25f),
                    Actions.rotateBy(-4f, .25f),
                    Actions.rotateBy(4f, .25f),
                    Actions.rotateBy(-4f, .25f)
            ))
        }
    }

    private fun fadeToScreen(baseScreen: BaseScreen) {
        burgerTable.addAction(Actions.fadeOut(.125f))
        burgerTable.addAction(Actions.sequence(
                Actions.delay(.125f),
                Actions.run { BaseGame.setActiveScreen(baseScreen) }
        ))
    }
}
