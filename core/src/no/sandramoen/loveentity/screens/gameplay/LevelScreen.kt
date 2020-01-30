package no.sandramoen.loveentity.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import no.sandramoen.loveentity.actors.*
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.BaseScreen
import no.sandramoen.loveentity.utils.BigNumber
import no.sandramoen.loveentity.utils.GameUtils

class LevelScreen : BaseScreen() {
    private lateinit var loveLabel: Label
    private lateinit var burgerButton: Button
    private lateinit var communityLeadersButton: Button
    private lateinit var upgradesButton: Button
    private lateinit var ascensionButton: Button
    private var burgerMenuActive = false

    private var revealNextGenerator = false

    private lateinit var table: Table
    private lateinit var burgerTable: Table

    private var time = 0f

    private lateinit var quickLoveButton: Button
    private lateinit var quickLoveButtonStyle: Button.ButtonStyle
    private lateinit var quickLoveNumberLabel: Label
    private lateinit var quickLoveList: Array<ResourceGenerator>

    private lateinit var buyButton: Button
    private lateinit var buyAmountLabel: Label
    private var buyIndex = 1

    override fun initialize() {
        val heart = Heart(0f, 0f, mainStage)
        val veil = Veil(0f, 0f, mainStage)

        if (BaseGame.resourceGenerators.size != 10)
            initializeAssets()

        for (generator in BaseGame.resourceGenerators)
            generator.enable()

        loveLabel = Label("${BaseGame.love.presentLongScale()} love", BaseGame.labelStyle)
        loveLabel.setFontScale(.5f)

        /* debug options------------------------------------------------------------------------------------------ */
        val debugButton1 = TextButton("Add 1k love", BaseGame.textButtonStyle)
        val debugButton2 = TextButton("Add 100k love", BaseGame.textButtonStyle)
        val debugButton3 = TextButton("Add 1M love", BaseGame.textButtonStyle)
        val debugButton4 = TextButton("Add 1T love", BaseGame.textButtonStyle)
        val debugButton5 = TextButton("Restart", BaseGame.textButtonStyle)

        debugButton1.label.color = Color.GREEN
        debugButton1.label.setFontScale(.8f)
        debugButton1.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(1_000))
                BaseGame.lifeTimeLove = BaseGame.lifeTimeLove.add(BaseGame.lifeTimeLove, BigNumber(1_000))
                update(Gdx.graphics.deltaTime) // updates hidetable visibilities
            }
            false
        }
        debugButton2.label.color = Color.GREEN
        debugButton2.label.setFontScale(.8f)
        debugButton2.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(100_000))
                BaseGame.lifeTimeLove = BaseGame.lifeTimeLove.add(BaseGame.lifeTimeLove, BigNumber(100_000))
                update(Gdx.graphics.deltaTime) // updates hidetable visibilities
            }
            false
        }
        debugButton3.label.color = Color.GREEN
        debugButton3.label.setFontScale(.8f)
        debugButton3.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(1_000_000))
                BaseGame.lifeTimeLove = BaseGame.lifeTimeLove.add(BaseGame.lifeTimeLove, BigNumber(1_000_000))
                update(Gdx.graphics.deltaTime) // updates hidetable visibilities
            }
            false
        }
        debugButton4.label.color = Color.GREEN
        debugButton4.label.setFontScale(.8f)
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
        debugButton5.label.color = Color.GREEN
        debugButton5.label.setFontScale(.8f)
        debugButton5.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) { // the restart button
                BaseGame.currentAscensionPoints = 0
                GameUtils.reset() // resets a bunch of stuff
                quickLoveList.clear()
                initializeAssets()

                table.reset()
                val heartTable = Table()
                heartTable.add(heart).padBottom(Gdx.graphics.height * .035f)
                val veilTable = Table().top()
                veilTable.add(veil).expand().fill().bottom()

                val stack = Stack()
                stack.add(heartTable)
                stack.add(veilTable)

                table.add(stack).padBottom(Gdx.graphics.height * .1f).padTop(Gdx.graphics.height * .1f).row()
                for (i in 0 until 2) {
                    table.add(BaseGame.resourceGenerators[i]).padBottom(Gdx.graphics.height * .07f).row()
                    BaseGame.resourceGenerators[i].isVisible = true // solves a visibility bug
                }
                update(Gdx.graphics.deltaTime) // updates hidetable visibilities
                GameUtils.saveGameState()
            }
            false
        }

        val debugLabel = Label("These are debug options", BaseGame.labelStyle)
        debugLabel.color = Color(0 / 255f, 153 / 255f, 0 / 255f, 1f)
        debugLabel.setFontScale(.25f)

        burgerTable = Table()
        burgerTable.add(debugLabel).row()
        burgerTable.add(debugButton1).row()
        burgerTable.add(debugButton2).row()
        burgerTable.add(debugButton3).row()
        burgerTable.add(debugButton4).row()
        burgerTable.add(debugButton5).padBottom(50f).row()
        /* ------------------------------------------------------------------------------------------------------- */

        communityLeadersButton = TextButton("Community Leaders", BaseGame.textButtonStyle)
        communityLeadersButton.isTransform = true
        communityLeadersButton.setOrigin(Align.center)
        (communityLeadersButton as TextButton).label.setFontScale(.8f)
        communityLeadersButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                BaseGame.setActiveScreen(CommunityLeadersScreen())
            false
        }
        burgerTable.add(communityLeadersButton).row()

        upgradesButton = TextButton("Upgrades", BaseGame.textButtonStyle)
        upgradesButton.isTransform = true
        upgradesButton.setOrigin(Align.center)
        (upgradesButton as TextButton).label.setFontScale(.8f)
        upgradesButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                BaseGame.setActiveScreen(UpgradesScreen())
            false
        }
        burgerTable.add(upgradesButton).row()

        ascensionButton = TextButton("Ascension", BaseGame.textButtonStyle)
        ascensionButton.isTransform = true
        ascensionButton.setOrigin(Align.center)
        (ascensionButton as TextButton).label.setFontScale(.8f)
        ascensionButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                BaseGame.setActiveScreen(AscensionScreen())
            false
        }
        burgerTable.add(ascensionButton).row()

        val unlocksButton = TextButton("Unlocks", BaseGame.textButtonStyle)
        (unlocksButton as TextButton).label.setFontScale(.8f)
        unlocksButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                BaseGame.setActiveScreen(UnlocksScreen())
            false
        }
        burgerTable.add(unlocksButton)

        burgerTable.isVisible = false

        val burgerButtonStyle = Button.ButtonStyle()
        burgerButtonStyle.up = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("burger")))
        burgerButton = Button(burgerButtonStyle)
        burgerButton.isTransform = true
        burgerButton.setOrigin(Align.center)
        burgerButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                if (burgerMenuActive) {
                    burgerButtonStyle.up = TextureRegionDrawable(BaseGame.textureAtlas!!.findRegion("burger"))
                    uiTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(0f, 0f, 0f, .0f))
                    burgerTable.isVisible = false
                    for (generator in BaseGame.resourceGenerators)
                        generator.enable()
                } else {
                    burgerButtonStyle.up = TextureRegionDrawable(BaseGame.textureAtlas!!.findRegion("cross-white"))
                    uiTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(0f, 0f, 0f, .75f))
                    burgerTable.isVisible = true
                    burgerButton.clearActions()
                    burgerButton.color = Color.WHITE
                    burgerButton.rotation = 0f
                    for (generator in BaseGame.resourceGenerators)
                        generator.disable()
                }
                burgerMenuActive = !burgerMenuActive
                heart.disabled = !heart.disabled
            }
            false
        }

        val uiToggleTable = Table()
        uiToggleTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(0f, 0f, 0f, .75f))
        uiToggleTable.add(burgerButton).pad(Gdx.graphics.height * .01f)

        quickLoveList = Array()
        quickLoveButtonStyle = Button.ButtonStyle()
        quickLoveButtonStyle.up = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("info")))
        quickLoveButton = Button(quickLoveButtonStyle)
        quickLoveButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                var highest = 0f
                var generatorToBeActivated: ResourceGenerator? = null
                for (generator in quickLoveList) {
                    if (generator.incomeTime > highest)
                        generatorToBeActivated = generator
                }
                if (generatorToBeActivated != null)
                    generatorToBeActivated!!.activated = true
            }
            false
        }

        // quick love
        quickLoveNumberLabel = Label("${quickLoveList.size}", BaseGame.labelStyle)
        quickLoveNumberLabel.setFontScale(.5f)

        val quickLoveLabel = Label("Quick Love!", BaseGame.labelStyle)
        quickLoveLabel.setFontScale(.3f)

        val qTable = Table()
        qTable.add(quickLoveNumberLabel).top().right().padRight(10f).padBottom(25f).row()
        qTable.add(quickLoveLabel).bottom().padTop(35f)
        qTable.setFillParent(true)
        // qTable.debug = true

        quickLoveButton.addActor(qTable)
        // quickLoveButton.debug = true

        // buy button
        buyButton = Button()
        val buyButtonStyle = Button.ButtonStyle()
        buyButtonStyle.up = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("buyBanner")))
        buyButton = Button(buyButtonStyle)
        buyButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                var label = ""
                var amount = -1L
                when (buyIndex) {
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
                        label = "next"
                        amount = 1
                    }
                    4 -> {
                        label = "max"
                        amount = 1L
                        buyIndex = -1
                    }
                }
                for (generator in BaseGame.resourceGenerators) {
                    if (generator.hideTable.isVisible) break // saves some computing
                    when (label) {
                        "next" -> {
                            val num = generator.unlocks[generator.unlockIndex].goal - generator.owned
                            if (generator.unlockIndex < generator.unlocks.size && num >= 0) {
                                generator.nextPurchase(num.toLong())
                            } else
                                generator.nextPurchase(1L)
                        }
                        "max" -> {
                            val num = BaseGame.love.divide(BaseGame.love, BigNumber(generator.price))
                            val temp = num.convertBigNumberToString()
                            if (BaseGame.love.isGreaterThanOrEqualTo(num)) {
                                generator.nextPurchase(temp.toLong())
                            } else {
                                generator.nextPurchase(1)
                            }
                        }
                        else -> generator.nextPurchase(amount)
                    }
                }
                buyAmountLabel.setText("$label")
                buyIndex++
            }
            false
        }
        val buyLabel = Label("Buy", BaseGame.labelStyle)
        buyLabel.setFontScale(.15f)
        buyAmountLabel = Label("x1", BaseGame.labelStyle)
        buyAmountLabel.setFontScale(.4f)

        val buyTable = Table()
        buyTable.add(buyLabel).row()
        buyTable.add(buyAmountLabel)
        buyTable.setFillParent(true)
        // buyTable.debug = true

        buyButton.addActor(buyTable)

        uiTable.add(buyButton).right().width(Gdx.graphics.width * .14f).height(Gdx.graphics.height * .04f).row()
        uiTable.add(burgerTable).fillY().expandY().row()
        uiTable.add(quickLoveButton).right().width(Gdx.graphics.width * .14f).height(Gdx.graphics.height * .07f)
                .padRight(Gdx.graphics.width * .06f).padBottom(Gdx.graphics.height * .015f).row()
        uiTable.add(uiToggleTable).fillX().expandX().bottom()
        // uiTable.debug = true

        val scrollableTable = Table()

        table = Table()

        val heartTable = Table()
        heartTable.add(heart).padBottom(Gdx.graphics.height * .035f)
        val veilTable = Table().top()
        veilTable.add(veil).expand().fill().bottom()

        val stack = Stack()
        stack.add(heartTable)
        stack.add(veilTable)

        table.add(stack).padBottom(Gdx.graphics.height * .1f).padTop(Gdx.graphics.height * .1f).row()
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

        // check buy next and buy max
        for (generator in BaseGame.resourceGenerators) {
            if (generator.hideTable.isVisible) break // saves some computing
            if (buyIndex == 4) { // next
                val num = generator.unlocks[generator.unlockIndex].goal - generator.owned
                if (generator.unlockIndex < generator.unlocks.size && num >= 0) {
                    generator.nextPurchase(num.toLong())
                }
            } else if (buyIndex == 0) { // max
                val num = BaseGame.love.divide(BaseGame.love, BigNumber(generator.price))
                val temp = num.convertBigNumberToString()
                if (num.maxNumber[0] == 0 && num.maxNumber.size == 1)
                    generator.nextPurchase(1)
                else
                    generator.nextPurchase(temp.toLong())
            }
        }
        // burger menu notifications
        for (i in 0 until BaseGame.communityLeaders.size) {
            if (BaseGame.communityLeadersWiggleIndex == i &&
                    !BaseGame.communityLeaders[i].remove &&
                    BaseGame.love.isGreaterThanOrEqualTo(BaseGame.communityLeaders[i].price)) {
                BaseGame.communityLeadersWiggleIndex++ // seen, but not clicked on
                BaseGame.prefs!!.putInteger("communityLeadersWiggleIndex", BaseGame.communityLeadersWiggleIndex)
                if (!burgerTable.isVisible) wiggleButton(burgerButton)
                wiggleButton(communityLeadersButton)
                break
            }
        }

        for (i in 0 until BaseGame.upgrades.size) {
            if (BaseGame.upgradesWiggleIndex == i &&
                    !BaseGame.upgrades[i].remove &&
                    BaseGame.love.isGreaterThanOrEqualTo(BaseGame.upgrades[i].price)) {
                BaseGame.upgradesWiggleIndex++ // seen, but not clicked on
                BaseGame.prefs!!.putInteger("upgradesWiggleIndex", BaseGame.communityLeadersWiggleIndex)
                if (!burgerTable.isVisible) wiggleButton(burgerButton)
                wiggleButton(upgradesButton)
                break
            }
        }

        if (BaseGame.claimAscensionPoints > BaseGame.currentAscensionPoints * 2f) {
            if (!burgerTable.isVisible) wiggleButton(burgerButton)
            wiggleButton(ascensionButton)
        }

        // quick love button
        quickLoveButton.isVisible = quickLoveList.size > 0
        for (generator in BaseGame.resourceGenerators) {
            if (!generator.activated && generator.owned > 0) {
                quickLoveButtonStyle.up = TextureRegionDrawable(BaseGame.textureAtlas!!.findRegion(generator.avatar))
                if (!quickLoveList.contains(generator)) quickLoveList.add(generator)
                quickLoveNumberLabel.setText("${quickLoveList.size}")
            } else {
                if (quickLoveList.contains(generator)) quickLoveList.removeValue(generator, true)
                quickLoveNumberLabel.setText("${quickLoveList.size}")
            }
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK)
            Gdx.app.exit()
        return false;
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

    private fun wiggleButton(button: Button) {
        if (button.actions.size == 0) {
            button.addAction(Actions.color(Color.ORANGE, 1f))
            button.addAction(Actions.after(Actions.forever(
                    Actions.sequence(
                            Actions.rotateBy(4f, .25f),
                            Actions.rotateBy(-4f, .25f)
                    )))
            )
        }
    }

    private fun initializeAssets() {
        val allyUnlocks = Array<Unlock>()
        allyUnlocks.add(Unlock(25, "speed"), Unlock(50, "speed"), Unlock(100, "speed"))

        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Ally", "pixelAvatarTestA", allyUnlocks, 4, 1.07f, 1f, .5f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Bisexual", "pixelAvatarTestB", allyUnlocks, 60, 1.15f, 60f, 3f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Gay", "pixelAvatarTestG", allyUnlocks, 720, 1.14f, 540f, 6f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Lesbian", "pixelAvatarTestL", allyUnlocks, 8640, 1.13f, 4320f, 12f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Cisgender", "pixelAvatarTest", allyUnlocks, 103680, 1.12f, 51840f, 24f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Queer", "pixelAvatarTest", allyUnlocks, 1244160, 1.11f, 622080f, 96f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Transgender", "pixelAvatarTest", allyUnlocks, 14929920, 1.1f, 7464960f, 384f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Intersex", "pixelAvatarTest", allyUnlocks, 179159040, 1.09f, 89579520f, 1536f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Pansexual", "pixelAvatarTest", allyUnlocks, 2149908480, 1.08f, 1074954240f, 6144f))
        BaseGame.resourceGenerators.add(ResourceGenerator(0f, 0f, mainStage, "Asexual", "pixelAvatarTest", allyUnlocks, 25798901760, 1.07f, 29668737024f, 36864f))

        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 0, "pixelAvatarTest", "Name Nameson", "runs Allies", BigNumber(1_000)))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 1, "pixelAvatarTest", "Name Nameson", "runs Bisexuals", BigNumber(15000)))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 2, "pixelAvatarTest", "Name Nameson", "runs Gays", BigNumber(100000)))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 3, "pixelAvatarTest", "Name Nameson", "runs Lesbians", BigNumber(500000)))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 4, "pixelAvatarTest", "Name Nameson", "runs Cisgenders", BigNumber(1200000)))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 5, "pixelAvatarTest", "Name Nameson", "runs Queers", BigNumber(10000000)))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 6, "pixelAvatarTest", "Name Nameson", "runs Transgenders", BigNumber(111111111)))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 7, "pixelAvatarTest", "Name Nameson", "runs Intersexs", BigNumber(555555555)))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 8, "pixelAvatarTest", "Name Nameson", "runs Pansexuals", BigNumber(10000000000)))
        BaseGame.communityLeaders.add(CommunityLeader(mainStage, 9, "pixelAvatarTest", "Name Nameson", "runs Asexuals", BigNumber(100000000000)))

        // this system assumes all upgrades are multiplicable of 3's
        if (BaseGame.resourceGenerators[0].upgrade / 3 == (1 / 3)) // first upgrade,
            BaseGame.upgrades.add(Upgrade(mainStage, 0, "itemTest", "Upgrade #1", "Ally love x3", BigNumber(250)))
        if (BaseGame.resourceGenerators[1].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 1, "itemTest", "Upgrade #2", "Bisexual love x3", BigNumber(500)))
        if (BaseGame.resourceGenerators[2].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 2, "itemTest", "Upgrade #3", "Gay love x3", BigNumber(1000000)))
        if (BaseGame.resourceGenerators[3].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 3, "itemTest", "Upgrade #4", "Lesbian love x3", BigNumber(5000000)))
        if (BaseGame.resourceGenerators[4].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 4, "itemTest", "Upgrade #5", "Cisgender love x3", BigNumber(10_000_000)))
        if (BaseGame.resourceGenerators[5].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 5, "itemTest", "Upgrade #6", "Queer love x3", BigNumber(25000000)))
        if (BaseGame.resourceGenerators[6].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 6, "itemTest", "Upgrade #7", "Transgender love x3", BigNumber(500000000)))
        if (BaseGame.resourceGenerators[7].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 7, "itemTest", "Upgrade #8", "Intersex love x3", BigNumber(10_000_000_000)))
        if (BaseGame.resourceGenerators[8].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 8, "itemTest", "Upgrade #9", "Pansexual love x3", BigNumber(250_000_000_000)))
        if (BaseGame.resourceGenerators[9].upgrade / 3 == (1 / 3)) // first upgrade
            BaseGame.upgrades.add(Upgrade(mainStage, 9, "itemTest", "Upgrade #10", "Asexual love x3", BigNumber(999999999999999999)))
    }
}
