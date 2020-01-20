package no.sandramoen.loveentity.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import no.sandramoen.loveentity.utils.BaseActor
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.BigNumber
import no.sandramoen.loveentity.utils.GameUtils
import kotlin.math.floor
import kotlin.math.pow

class ResourceGenerator(x: Float, y: Float, s: Stage,
                        name: String, baseCost: Long, multiplier: Float, income: Float, incomeTime: Float) : BaseActor(x, y, s) {
    var hideTable: Table
    private var heartIcon: BaseActor

    private var table: Table
    private var resourceName: String = name
    private var nameLabel: Label

    private var selfWidth = Gdx.graphics.width * .95f // 600f
    private var selfHeight = 300f

    private var activated = false
    private var activatedAnimation = false

    var owned: Int = 0
    var baseCost: Long = baseCost
    private var multiplier: Float = multiplier
    private var price: Long = baseCost * multiplier.pow(owned).toLong()
    private var income: Float = income
    private var incomeTime: Float = incomeTime
    private var time: Float = 0f
    private var fraction: Float = 0f

    private lateinit var activateButton: Button
    private lateinit var ownedLabel: Label
    private lateinit var timeLabel: Label
    private lateinit var buyLabel: Label
    private lateinit var timeProgress: BaseActor

    init {
        this.isVisible = false // solves a visibility bug
        loadAnimation(BaseGame.textureAtlas!!.findRegion("whitePixel"))
        width = selfWidth
        height = selfHeight
        // color = Color(random(0, 255) / 255f, random(0, 255) / 255f, random(0, 255) / 255f, 1f)
        color = Color.DARK_GRAY

        nameLabel = Label(resourceName, BaseGame.labelStyle)
        nameLabel.setFontScale(.75f)

        // load game state
        owned = BaseGame.prefs!!.getInteger(name + "Owned")
        time = BaseGame.prefs!!.getFloat(name + "Time")
        activated = BaseGame.prefs!!.getBoolean(name + "Activated")
        activatedAnimation = owned >= 1 && !activated
        price = baseCost * multiplier.pow(owned).toLong()
        addLoveSinceLastTimedPlayed()

        // hide table
        val hideLabel = Label("???", BaseGame.labelStyle)
        hideLabel.color = Color.PURPLE
        heartIcon = BaseActor(0f, 0f, s)
        heartIcon.loadAnimation(BaseGame.textureAtlas!!.findRegion("heart"))
        heartIcon.width = 40f
        heartIcon.height = 40f
        val baseCostLabel = Label("${BigNumber(baseCost).presentLongScale()}", BaseGame.labelStyle)
        baseCostLabel.setFontScale(.5f)

        hideTable = Table()
        hideTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(MathUtils.random(.1f, .2f), MathUtils.random(.1f, .2f), MathUtils.random(.1f, .2f), 1f))
        hideTable.isVisible = owned <= 0
        hideTable.isTransform = true
        hideTable.setOrigin(0f, Gdx.graphics.height*.058f)
        hideTable.scaleBy(0f, .355f) // TODO

        hideTable.add(hideLabel).colspan(2).row()
        hideTable.add(heartIcon).padRight(10f)
        hideTable.add(baseCostLabel)
        // hideTable.debug = true

        // info table
        val infoLabel = Label(GameUtils.getInformationText(resourceName), BaseGame.labelStyle)
        infoLabel.color = Color.PURPLE
        infoLabel.setWrap(true)
        infoLabel.setFontScale(.3f)

        val infoTable = Table()
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
        val infoButton = Button(buttonStyle)

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
        table.add(leftTable(s)).pad(selfWidth * .01f) // TODO: set height and width here
        table.add(rightTable(s))
        table.align(Align.center)

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
            val product = income * owned
            if (product >= 1) {
                BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber((income * owned).toLong()))
            } else { // hack to support BigNumber fractions
                fraction += (product - floor(product))
                if (fraction >= 1) {
                    BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(fraction.toLong()))
                    fraction -= 1
                }
            }

            // activated = false
            activated = true
            if (!activated)
                activatedAnimation = true
            time = 0f
            timeProgress.width = 0f
        }

        if (activated) {
            time += dt
            BaseGame.prefs!!.putFloat(resourceName + "Time", time)
            labelTime(time)
            timeProgress.width = (selfWidth * .68f) * (time / incomeTime)
            activateButton.clearActions()
            activateButton.addAction(Actions.scaleTo(1f, 1f, .25f))
        } else {
            if (activatedAnimation) {
                activateButton.addAction(Actions.forever(Actions.sequence(
                        Actions.scaleTo(1.05f, 1.05f, .25f),
                        Actions.delay(.125f),
                        Actions.scaleTo(1.0f, 1.0f, .25f)
                )))
                activatedAnimation = false
            }
        }
        timeProgress.setPosition(0f, timeProgress.y) // TODO: solves some weird displacement bug...
    }

    private fun leftTable(s: Stage): Table {
        val buttonStyle = Button.ButtonStyle()
        var buttonTex = BaseGame.textureAtlas!!.findRegion("pixelAvatarTest")
        val buttonRegion = TextureRegion(buttonTex)
        buttonStyle.up = TextureRegionDrawable(buttonRegion)

        activateButton = Button(buttonStyle)

        ownedLabel = Label("$owned", BaseGame.labelStyle)
        ownedLabel.setFontScale(.5f)
        ownedLabel.color = Color.YELLOW

        val levelProgress = BaseActor(0f, 0f, s)
        levelProgress.loadAnimation(BaseGame.textureAtlas!!.findRegion("whitePixel"))
        levelProgress.width = selfWidth * .25f
        levelProgress.height = selfHeight * .175f
        levelProgress.color = Color.FIREBRICK

        ownedLabel.setPosition((levelProgress.width / 2) - ownedLabel.width / 3, -levelProgress.height / 2) // TODO: weird offsets that just works...
        levelProgress.addActor(ownedLabel)

        activateButton.addActor(levelProgress)
        activateButton.isTransform = true
        activateButton.setOrigin(Align.center)
        activateButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                if (owned > 0) {
                    activated = true
                    activatedAnimation = false
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
        val buttonStyle = Button.ButtonStyle()
        var buttonTex = BaseGame.textureAtlas!!.findRegion("whitePixel")
        val buttonRegion = TextureRegion(buttonTex)
        buttonStyle.up = TextureRegionDrawable(buttonRegion)

        buyLabel = Label("  Buy 1x     ${BigNumber(price).presentLongScale()}", BaseGame.labelStyle)
        buyLabel.setFontScale(.4f)
        val buy = Button(buttonStyle)
        buy.addActor(buyLabel)
        buy.color = Color.ORANGE
        buy.addListener(object : ActorGestureListener() {
            override fun tap(event: InputEvent?, x: Float, y: Float, count: Int, button: Int) {
                timeProgress.setPosition(0f, timeProgress.y) // TODO: solves some weird displacement bug...
                if (BaseGame.love.isGreaterThanOrEqualTo(BigNumber(price))) {
                    BaseGame.love = BaseGame.love.subtract(BaseGame.love, BigNumber(price))
                    owned++
                    BaseGame.prefs!!.putInteger(resourceName + "Owned", owned)
                    ownedLabel.setText("$owned")
                    price = (baseCost * multiplier.pow(owned)).toLong()
                    buyLabel.setText("  Buy 1x     ${BigNumber(price).presentLongScale()}")
                    if (owned == 1)
                        activatedAnimation = true
                }
            }
        })

        // time
        timeLabel = Label("?", BaseGame.labelStyle)
        timeLabel.setFontScale(.5f)
        val time = BaseActor(0f, 0f, s)
        time.addActor(timeLabel)
        time.loadAnimation(BaseGame.textureAtlas!!.findRegion("whitePixel"))
        time.width = selfWidth * .167f
        time.height = selfHeight * .375f
        time.color = Color.LIGHT_GRAY

        val table = Table()
        table.add(timeProgress).colspan(2).pad(selfWidth * .01f).row()
        table.add(buy).pad(selfWidth * .01f).width(selfWidth * .5f).height(selfHeight * .375f)
        table.add(time).pad(selfWidth * .01f)
        return table
    }

    fun addLoveSinceLastTimedPlayed() {
        if (time > 0) {
            BaseGame.love = BaseGame.love.add(BaseGame.love, BigNumber(((income / incomeTime) * BaseGame.secondsSinceLastPlayed).toLong()))
            time += BaseGame.secondsSinceLastPlayed % time
        }
    }

    fun reset() {
        owned = 0
        ownedLabel.setText("$owned")

        price = (baseCost * multiplier.pow(owned)).toLong()
        fraction = 0f
        buyLabel.setText("  Buy 1x     ${BigNumber(price).presentLongScale()}")

        time = 0f
        timeLabel.setText("?")
        activated = false

        timeProgress.width = 0f

        activateButton.actions.clear()
        activateButton.addAction(Actions.scaleTo(1f, 1f, .25f))

        BaseGame.prefs!!.putFloat(resourceName + "Time", time)
        BaseGame.prefs!!.putBoolean(resourceName + "Activated", false)
        BaseGame.prefs!!.putInteger(resourceName + "Owned", owned)

        isVisible = false
        hideTable.isVisible = true
        heartIcon.isVisible = true
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

    private fun labelTime(timeInSeconds: Float) { // labels time, e.g. "1h 23m 17s"
        var timeLeftInSeconds = incomeTime - timeInSeconds
        val hours = (timeLeftInSeconds / 3600).toInt()
        val minutes = (timeLeftInSeconds / 60).toInt()
        val seconds = (timeLeftInSeconds - (hours * 3600) - (minutes * 60)).toInt()
        when {
            minutes == 0 -> timeLabel.setText("${seconds}s")
            hours == 0 -> timeLabel.setText("${minutes}m ${seconds}s")
            else -> timeLabel.setText("${hours}h ${minutes}m ${seconds}s")
        }
    }
}
