package no.sandramoen.loveentity.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import no.sandramoen.loveentity.utils.BaseActor
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.GameUtils
import java.math.BigInteger
import kotlin.math.floor
import kotlin.math.pow

class ResourceGenerator(x: Float, y: Float, s: Stage,
                        var norwegianName: String, saveName: String, avatar: String, unlocks: Array<Unlock>, baseCost: Long, multiplier: Float, income: Float, incomeTime: Float)
    : BaseActor(x, y, s) {
    var hideTable: Table
    var infoTable: Table
    var avatar: String
    var unlocks: Array<Unlock>
    var unlockIndex = 0
    private var heartIcon: BaseActor

    private var table: Table
    var resourceName: String = saveName
    private var nameLabel: Label

    private var selfWidth = Gdx.graphics.width * .95f // 600f
    private var selfHeight = 300f

    var hasCommunityLeader = false
    var upgrade: Int = 1
    var activated = false

    var owned: BigInteger = BigInteger.ZERO
    var baseCost: Long = baseCost
    private var multiplier: Float = multiplier
    var price: BigInteger = BigInteger((baseCost * multiplier.pow(owned.toFloat())).toLong().toString())
    var nextPurchase: BigInteger = BigInteger((baseCost * multiplier.pow(owned.toFloat())).toLong().toString())
    private var nextPurchaseAmount = BigInteger.ONE
    private var income: Float = income
    var incomeTime: Float = incomeTime
    private var originalIncomeTime: Float = incomeTime
    private var time: Float = 0f

    private lateinit var activateButton: Button
    private lateinit var ownedLabel: Label
    private lateinit var timeLabel: Label
    private lateinit var buyNameLabel: Label
    private lateinit var buyPriceLabel: Label
    private lateinit var buyAmountLabel: Label
    private lateinit var buyNumberLabel: Label
    private var baseCostLabel: Label
    private var infoLabel: Label
    private lateinit var buyButton: Button
    private var infoButton: Button
    private lateinit var timeProgress: BaseActor
    private var unlockProgression: Button

    init {
        this.isVisible = false // solves a visibility bug
        loadAnimation(BaseGame.textureAtlas!!.findRegion("whitePixel"))
        width = selfWidth
        height = selfHeight
        // color = Color(MathUtils.random(0, 255) / 255f, MathUtils.random(0, 255) / 255f, MathUtils.random(0, 255) / 255f, 1f)
        color = Color.DARK_GRAY
        unlockProgression = Button(BaseGame.textButtonStyle)

        if (BaseGame.english)
            nameLabel = Label(saveName, BaseGame.labelStyle)
        else
            nameLabel = Label(norwegianName, BaseGame.labelStyle)
        nameLabel.setFontScale(.75f)

        this.avatar = avatar
        this.unlocks = unlocks

        // load game state
        owned = GameUtils.getBigNumber(resourceName + "Owned")
        time = BaseGame.prefs!!.getFloat(resourceName + "Time")
        hasCommunityLeader = BaseGame.prefs!!.getBoolean(resourceName + "HasCommunityLeader")
        activated = BaseGame.prefs!!.getBoolean(resourceName + "Activated")
        if (BaseGame.prefs!!.getInteger(resourceName + "Upgrade") > 0)
            upgrade = BaseGame.prefs!!.getInteger(resourceName + "Upgrade")
        price = BigInteger(baseCost.toString()).multiply(BigInteger(multiplier.pow(owned.toFloat()).toLong().toString()))
        nextPurchase = BigInteger(price.toString())
        addLoveSinceLastTimedPlayed()
        unlockIndex = BaseGame.prefs!!.getInteger(resourceName + "UnlockIndex")
        for (i in 0 until unlockIndex)
            if (owned >= BigInteger((unlocks[i].goal).toString()))
                applyEffect(unlocks[i].effect)


        // hide table
        val hideLabel = Label("???", BaseGame.labelStyle)
        hideLabel.color = Color.PURPLE
        heartIcon = BaseActor(0f, 0f, s)
        heartIcon.loadAnimation(BaseGame.textureAtlas!!.findRegion("heart"))
        heartIcon.width = 60f
        heartIcon.height = 60f
        baseCostLabel = Label("", BaseGame.labelStyle)
        if (BaseGame.longScale)
            baseCostLabel.setText("${GameUtils.presentLongScale(BigInteger(baseCost.toString()))}")
        else
            baseCostLabel.setText("${GameUtils.presentShortScale(BigInteger(baseCost.toString()))}")
        baseCostLabel.setFontScale(.5f)

        hideTable = Table()
        hideTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(MathUtils.random(.1f, .2f), MathUtils.random(.1f, .2f), MathUtils.random(.1f, .2f), 1f))
        hideTable.isVisible = owned <= BigInteger.ZERO
        hideTable.isTransform = true
        hideTable.setOrigin(0f, Gdx.graphics.height * .058f)
        hideTable.scaleBy(0f, .355f)

        hideTable.add(hideLabel).colspan(2).row()
        hideTable.add(heartIcon).padRight(10f)
        hideTable.add(baseCostLabel)
        // hideTable.debug = true

        // info table
        infoLabel = Label(GameUtils.getInformationText(resourceName), BaseGame.labelStyle)
        infoLabel.color = Color.PURPLE
        infoLabel.setWrap(true)
        infoLabel.setFontScale(.3f)

        infoTable = Table()
        infoTable.add(infoLabel).expand().fill()
        infoTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(0f, 0f, 0f, .9f))
        infoTable.isVisible = false
        infoTable.addListener(object : ActorGestureListener() {
            override fun tap(event: InputEvent?, x: Float, y: Float, count: Int, button: Int) {
                infoTable.isVisible = !infoTable.isVisible
            }
        })
        // infoTable.debug = true

        val buttonStyle = Button.ButtonStyle()
        val buttonTex = BaseGame.textureAtlas!!.findRegion("info")
        val buttonRegion = TextureRegion(buttonTex)
        buttonStyle.up = TextureRegionDrawable(buttonRegion)
        infoButton = Button(buttonStyle)

        val nameAndInfoTable = Table()
        nameAndInfoTable.add(nameLabel)
        nameAndInfoTable.add(infoButton).padBottom(Gdx.graphics.height * .02f).padLeft(10f)
        nameAndInfoTable.addListener(object : ActorGestureListener() {
            override fun tap(event: InputEvent?, x: Float, y: Float, count: Int, button: Int) {
                infoTable.isVisible = !infoTable.isVisible
            }
        })
        // nameAndInfoTable.debug = true

        // table
        table = Table()
        table.add(nameAndInfoTable).top().colspan(2).row()
        table.add(leftTable(s)).pad(selfWidth * .01f).width(Gdx.graphics.width * .25f).height(Gdx.graphics.height * .15f).align(Align.center)
        table.add(rightTable(s))
        // table.align(Align.center)

        val stack = Stack() // stack allows for scene2d elements to overlap each other
        stack.add(table)
        stack.add(infoTable)
        stack.add(hideTable)
        stack.width = selfWidth // fill x
        stack.height = selfHeight // fill y
        addActor(stack)

        /*table.debug()
        this.debug*/
    }

    override fun act(dt: Float) {
        super.act(dt)
        labelTime(time)

        if (time >= incomeTime) {
            val product = owned.multiply(BigInteger(income.toLong().toString()))
            val totalIncome = if (BaseGame.currentAscensionPoints > 0)
                product.toLong() * upgrade * (BaseGame.currentAscensionPoints * BaseGame.ascensionBonus * BaseGame.heartBonus)
            else
                product.toLong() * upgrade * BaseGame.heartBonus
            BaseGame.love = BaseGame.love.add(BigInteger(totalIncome.toString()))
            BaseGame.lifeTimeLove = BaseGame.lifeTimeLove.add(BigInteger(totalIncome.toString()))

            activated = hasCommunityLeader
            time = 0f
            timeProgress.width = 0f
        }

        if (activated || (hasCommunityLeader && owned > BigInteger.ZERO)) {
            time += dt
            BaseGame.prefs!!.putFloat(resourceName + "Time", time)
            BaseGame.prefs!!.putBoolean(resourceName + "Activated", true)
            labelTime(time)
            timeProgress.width = (selfWidth * .68f) * (time / incomeTime)
            activateButton.clearActions()
            activateButton.addAction(Actions.scaleTo(1f, 1f, .25f))
        } else {
            if (activateButton.actions.isEmpty && owned > BigInteger.ZERO) {
                activateButton.addAction(Actions.forever(Actions.sequence(
                        Actions.scaleTo(1.1f, 1.1f, .25f),
                        Actions.delay(.125f),
                        Actions.scaleTo(1.0f, 1.0f, .25f)
                )))
            }
        }
        timeProgress.setPosition(0f, timeProgress.y) // TODO: solves some weird displacement bug...

        if (BaseGame.love >= nextPurchase)
            buyButton.color = Color.ORANGE
        else
            buyButton.color = Color.GRAY
    }

    fun addLoveSinceLastTimedPlayed() {
        if (hasCommunityLeader || activated) {
            val temp = floor((BaseGame.secondsSinceLastPlayed + BaseGame.prefs!!.getFloat(resourceName + "Time")) / incomeTime)
            if (BaseGame.currentAscensionPoints > 0)
                BaseGame.love = BaseGame.love.add(BigInteger((temp * income * upgrade * BaseGame.currentAscensionPoints * BaseGame.ascensionBonus).toLong().toString()))
            else
                BaseGame.love = BaseGame.love.add(BigInteger((temp * income * upgrade).toLong().toString()))
            time += BaseGame.secondsSinceLastPlayed % time
        }
    }

    fun reset() {
        owned = BigInteger.ZERO
        incomeTime = originalIncomeTime
        unlockIndex = 0
        if (BaseGame.longScale)
            ownedLabel.setText("${GameUtils.presentLongScale(owned)} / ${GameUtils.presentLongScale(BigInteger(unlocks[unlockIndex].goal.toString()))}")
        else
            ownedLabel.setText("${GameUtils.presentShortScale(owned)} / ${GameUtils.presentShortScale(BigInteger(unlocks[unlockIndex].goal.toString()))}")
        unlockProgression.width = 0f

        price = BigInteger((baseCost * multiplier.pow(owned.toFloat())).toLong().toString())
        nextPurchase = price
        var split: List<String>?
        if (BaseGame.longScale)
            split = GameUtils.presentLongScale(nextPurchase).split(" ")
        else
            split = GameUtils.presentShortScale(nextPurchase).split(" ")
        buyPriceLabel.setText("${split[0]}")
        buyNumberLabel.setText("x1")
        if (BaseGame.longScale)
            buyAmountLabel.setText("x${GameUtils.presentLongScale(BigInteger(nextPurchaseAmount.toString()))}")
        else
            buyAmountLabel.setText("x${GameUtils.presentShortScale(BigInteger(nextPurchaseAmount.toString()))}")

        if (BaseGame.english)
            buyNameLabel.setText("Acquire")
        else
            buyNameLabel.setText("Erverv")

        time = 0f
        timeLabel.setText("?")
        activated = false
        hasCommunityLeader = false
        upgrade = 1

        timeProgress.width = 0f

        activateButton.actions.clear()
        activateButton.addAction(Actions.scaleTo(1f, 1f, .25f))

        BaseGame.prefs!!.putFloat(resourceName + "Time", time)
        BaseGame.prefs!!.putBoolean(resourceName + "HasCommunityLeader", false)
        BaseGame.prefs!!.putBoolean(resourceName + "Activated", false)
        BaseGame.prefs!!.putInteger(resourceName + "Upgrade", 1)
        GameUtils.putBigNumber(resourceName + "Owned", owned)
        BaseGame.prefs!!.putInteger(resourceName + "UnlockIndex", unlockIndex)

        isVisible = false
        hideTable.isVisible = true
        heartIcon.isVisible = true
        infoTable.isVisible = false
        hideTable.addAction(Actions.alpha(1f, 0f, Interpolation.linear))
        heartIcon.addAction(Actions.alpha(1f, 0f, Interpolation.linear))
    }

    fun exposeResourceGenerator() {
        hideTable.addAction(Actions.alpha(0f, .5f, Interpolation.linear))
        heartIcon.addAction(Actions.alpha(0f, .5f, Interpolation.linear))
        hideTable.addAction(Actions.sequence(
                Actions.delay(1f),
                Actions.run { hideTable.isVisible = false }
        ))
    }

    fun disable() {
        infoButton.touchable = Touchable.disabled
        activateButton.touchable = Touchable.disabled
        buyButton.touchable = Touchable.disabled
    }

    fun enable() {
        infoButton.touchable = Touchable.enabled
        activateButton.touchable = Touchable.enabled
        buyButton.touchable = Touchable.enabled
    }

    fun nextPurchase(amount: BigInteger) {
        nextPurchase = calculatePriceForAmount(amount)
        nextPurchaseAmount = amount
        labelBuyButton()
    }

    fun checkLanguage() {
        labelBuyButton()

        if (BaseGame.english) {
            buyNameLabel.setText("Acquire")
            nameLabel.setText(resourceName)
        } else {
            buyNameLabel.setText("Erverv")
            nameLabel.setText(norwegianName)
        }
        infoLabel.setText(GameUtils.getInformationText(resourceName))
    }

    fun calculateAmountUpToValue(amount: BigInteger): BigInteger {
        var temp = BigInteger.ZERO
        var bi = BigInteger.ZERO
        while (true) {
            val calc = BigInteger((baseCost * multiplier.pow(owned.toFloat() + bi.toFloat())).toLong().toString())
            if (temp.add(calc) <= amount) {
                temp = temp.add(calc)
                bi = bi.add(BigInteger.ONE)
            } else
                break
        }
        if (bi == BigInteger.ZERO)
            bi = BigInteger.ONE
        return bi
    }

    fun checkScale() {
        if (BaseGame.longScale)
            baseCostLabel.setText("${GameUtils.presentLongScale(BigInteger(baseCost.toString()))}")
        else
            baseCostLabel.setText("${GameUtils.presentShortScale(BigInteger(baseCost.toString()))}")
    }

    private fun leftTable(s: Stage): Table {
        val buttonStyle = Button.ButtonStyle()
        var buttonTex = BaseGame.textureAtlas!!.findRegion(avatar)
        val buttonRegion = TextureRegion(buttonTex)
        buttonStyle.up = TextureRegionDrawable(buttonRegion)

        activateButton = Button(buttonStyle)

        try {
            if (BaseGame.longScale)
                ownedLabel = Label("${GameUtils.presentLongScale(owned)} / ${GameUtils.presentLongScale(BigInteger(unlocks[unlockIndex].goal.toString()))}", BaseGame.labelStyle)
            else
                ownedLabel = Label("${GameUtils.presentShortScale(owned)} / ${GameUtils.presentShortScale(BigInteger(unlocks[unlockIndex].goal.toString()))}", BaseGame.labelStyle)
        } catch (error: IndexOutOfBoundsException) {
            if (BaseGame.longScale)
                ownedLabel = Label("${GameUtils.presentLongScale(owned)}", BaseGame.labelStyle)
            else
                ownedLabel = Label("${GameUtils.presentShortScale(owned)}", BaseGame.labelStyle)
        }
        ownedLabel.setFontScale(.4f)
        ownedLabel.color = Color.YELLOW

        val unlockProgress = Button(BaseGame.textButtonStyle)
        unlockProgress.isTransform = true
        unlockProgress.setPosition(Gdx.graphics.width * .007f, -Gdx.graphics.height * .01f)
        unlockProgress.width = selfWidth * .25f
        unlockProgress.height = selfHeight * .25f
        unlockProgress.color = Color.FIREBRICK

        if (unlocks.size > unlockIndex) {
            if (unlockIndex == 0) {
                unlockProgression.width = (selfWidth * .25f) * (owned.toFloat() / unlocks[unlockIndex].goal)
            } else {
                unlockProgression.width = (selfWidth * .25f) * ((owned.subtract(BigInteger((unlocks[unlockIndex - 1].goal).toString())).toFloat() / (unlocks[unlockIndex].goal - unlocks[unlockIndex - 1].goal).toFloat()))
            }
            unlockProgression.isTransform
            unlockProgression.setScale(0f, 0f)
            unlockProgression.height = selfHeight * .25f
            unlockProgression.color = Color.GREEN
            unlockProgression.setPosition(Gdx.graphics.width * .002f, 0f)
            unlockProgress.addActor(unlockProgression)
        }

        ownedLabel.setPosition((unlockProgress.width / 2) - ownedLabel.width / 5, -Gdx.graphics.height * .0235f) // TODO: weird offsets that just works...
        unlockProgress.addActor(ownedLabel)

        activateButton.addActor(unlockProgress)
        activateButton.isTransform = true
        activateButton.setOrigin(Align.center)
        activateButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                if (owned > BigInteger.ZERO) {
                    activated = true
                    BaseGame.prefs!!.putBoolean(resourceName + "Activated", true)
                }
            }
            false
        }
        return activateButton
    }

    private fun rightTable(s: Stage): Table {
        // progress
        timeProgress = BaseActor(0f, 0f, s)
        timeProgress.loadAnimation(BaseGame.textureAtlas!!.findRegion("whitePixel"))
        timeProgress.width = 0f
        timeProgress.height = selfHeight * .375f
        timeProgress.color = Color.GREEN

        // buy
        if (BaseGame.english)
            buyNameLabel = Label("Acquire", BaseGame.labelStyle)
        else
            buyNameLabel = Label("Erverv", BaseGame.labelStyle)

        buyAmountLabel = Label("x1", BaseGame.labelStyle)
        var split: List<String>
        if (BaseGame.longScale)
            split = GameUtils.presentLongScale(nextPurchase).split(" ")
        else
            split = GameUtils.presentShortScale(nextPurchase).split(" ")
        buyPriceLabel = Label("${split[0]}", BaseGame.labelStyle)
        if (split.size > 1)
            buyNumberLabel = Label("${split[1]}", BaseGame.labelStyle)
        else
            buyNumberLabel = Label("", BaseGame.labelStyle)
        buyNameLabel.setFontScale(.4f)
        buyPriceLabel.setFontScale(.4f)
        buyAmountLabel.setFontScale(.4f)
        buyNumberLabel.setFontScale(.25f)
        buyNumberLabel.color = Color.BLACK
        val buyTable = Table()
        buyTable.setFillParent(true)
        buyTable.add(buyNameLabel).left().expandX().padLeft(Gdx.graphics.width * .02f)
        buyTable.add(buyPriceLabel).right().padRight(Gdx.graphics.width * .02f).row()
        buyTable.add(buyAmountLabel).left().padLeft(Gdx.graphics.width * .02f)
        buyTable.add(buyNumberLabel).right().padRight(Gdx.graphics.width * .02f).padBottom(50f)
        // buyTable.debug = true

        val buttonStyle = BaseGame.textButtonStyle
        buyButton = Button(buttonStyle)
        buyButton.addActor(buyTable)
        buyButton.color = Color.ORANGE
        buyButton.addListener(object : ActorGestureListener() {
            override fun tap(event: InputEvent?, x: Float, y: Float, count: Int, button: Int) {
                timeProgress.setPosition(0f, timeProgress.y) // TODO: solves some weird displacement bug...
                if (BaseGame.love >= nextPurchase) {
                    BaseGame.love = BaseGame.love.subtract(nextPurchase)
                    owned = owned.add(nextPurchaseAmount)
                    GameUtils.putBigNumber(resourceName + "Owned", owned)
                    try {
                        if (owned >= BigInteger((unlocks[unlockIndex].goal).toString()))
                            if (BaseGame.longScale)
                                ownedLabel.setText("${GameUtils.presentLongScale(owned)} / ${GameUtils.presentLongScale(BigInteger((unlocks[unlockIndex + 1].goal).toString()))}")
                            else
                                ownedLabel.setText("${GameUtils.presentShortScale(owned)} / ${GameUtils.presentShortScale(BigInteger((unlocks[unlockIndex + 1].goal).toString()))}")
                        else
                            if (BaseGame.longScale)
                                ownedLabel.setText("${GameUtils.presentLongScale(owned)} / ${GameUtils.presentLongScale(BigInteger(unlocks[unlockIndex].goal.toString()))}")
                            else
                                ownedLabel.setText("${GameUtils.presentShortScale(owned)} / ${GameUtils.presentShortScale(BigInteger(unlocks[unlockIndex].goal.toString()))}")
                    } catch (error: IndexOutOfBoundsException) {
                        if (BaseGame.longScale)
                            ownedLabel.setText("${GameUtils.presentLongScale(owned)}")
                        else
                            ownedLabel.setText("${GameUtils.presentShortScale(owned)}")
                    }
                    price = BigInteger((baseCost * multiplier.pow(owned.toFloat())).toLong().toString())
                    nextPurchase = BigInteger((price.multiply(BigInteger(nextPurchaseAmount.toString()))).toString())

                    labelBuyButton()

                    if (BaseGame.english)
                        buyNameLabel.setText("Acquire")
                    else
                        buyNameLabel.setText("Erverv")

                    while (unlocks.size > unlockIndex && owned >= BigInteger((unlocks[unlockIndex].goal).toString())) {
                        applyEffect(unlocks[unlockIndex].effect)
                        unlockIndex++
                        BaseGame.prefs!!.putInteger(resourceName + "UnlockIndex", unlockIndex)
                    }

                    if (unlocks.size > unlockIndex) {
                        if (unlockIndex == 0) {
                            unlockProgression.width = (selfWidth * .25f) * (owned.toFloat() / unlocks[unlockIndex].goal)
                        } else {
                            unlockProgression.width = (selfWidth * .25f) * ((owned.subtract(BigInteger((unlocks[unlockIndex - 1].goal).toString())).toFloat() / (unlocks[unlockIndex].goal - unlocks[unlockIndex - 1].goal).toFloat()))
                        }
                    }
                }
            }
        })

        // time
        timeLabel = Label("?", BaseGame.labelStyle)
        timeLabel.setFontScale(.4f)
        timeLabel.setFillParent(true)
        timeLabel.setAlignment(Align.center)
        val time = Button(BaseGame.textButtonStyle)
        time.isTransform = true
        time.color = Color.GRAY
        time.addActor(timeLabel)

        val table = Table()
        table.add(timeProgress).colspan(2).pad(selfWidth * .01f).row()
        table.add(buyButton).pad(selfWidth * .01f).width(selfWidth * .5f).height(selfHeight * .6f)
        table.add(time).pad(selfWidth * .01f).width(selfWidth * .25f).height(selfHeight * .6f)
        return table
    }

    private fun labelTime(timeInSeconds: Float) { // labels time, e.g. "1h 23m 17s"
        var timeLeftInSeconds = incomeTime - timeInSeconds

        val hours = (timeLeftInSeconds / 3600).toInt()
        var remainder = (timeLeftInSeconds - (hours * 3600)).toInt()
        val minutes = remainder / 60
        remainder -= (minutes * 60)
        val seconds = remainder

        var text = ""
        if (hours != 0) {
            if (BaseGame.english) text += "${hours}h "
            else text += "${hours}t "
        }
        if (minutes != 0)
            text += "${minutes}m "
        if (seconds != 0 && hours == 0)
            text += "${seconds}s"
        else if (hours == 0 && minutes == 0 && seconds == 0)
            text += "${seconds}s"

        timeLabel.setText(text)
    }

    private fun applyEffect(effect: String) {
        if (effect[0].toString() == "x")
            upgrade *= effect.substring(1, effect.length).toInt()
        else if (effect == "speed")
            incomeTime /= 2
    }

    private fun calculatePriceForAmount(amount: BigInteger): BigInteger {
        var temp = BigInteger.ZERO
        var bi = BigInteger.ZERO
        while (bi < amount) {
            temp = temp.add(BigInteger((baseCost * multiplier.pow(owned.toFloat() + bi.toString().toFloat())).toLong().toString()))
            bi = bi.add(BigInteger.ONE)
        }
        return temp
    }

    private fun labelBuyButton() {
        var split: List<String>?
        if (BaseGame.longScale)
            split = GameUtils.presentLongScale(nextPurchase).split(" ")
        else
            split = GameUtils.presentShortScale(nextPurchase).split(" ")
        buyPriceLabel.setText("${split[0]}")
        if (split.size > 1)
            buyNumberLabel.setText("${split[1]}")
        else
            buyNumberLabel.setText("")
        if (BaseGame.longScale)
            buyAmountLabel.setText("x${GameUtils.presentLongScale(BigInteger(nextPurchaseAmount.toString()))}")
        else
            buyAmountLabel.setText("x${GameUtils.presentShortScale(BigInteger(nextPurchaseAmount.toString()))}")
    }
}
