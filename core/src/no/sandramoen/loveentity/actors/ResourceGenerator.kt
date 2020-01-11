package no.sandramoen.loveentity.actors

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.compression.lzma.Base

import no.sandramoen.loveentity.utils.BaseActor
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.GameUtils

import kotlin.math.ceil
import kotlin.math.pow

class ResourceGenerator(x: Float, y: Float, s: Stage,
                        name: String, baseCost: Long, multiplier: Float, income: Float, incomeTime: Float) : BaseActor(x, y, s) {
    private var table: Table
    private var resourceName: String = name
    private var nameLabel: Label

    private var selfWidth = Gdx.graphics.width * .95f // 600f
    private var selfHeight = 300f

    private var activated = false
    private var purchased = false

    private var owned: Int = 0
    private var baseCost: Long = baseCost
    private var multiplier: Float = multiplier
    private var price: Float = baseCost * multiplier.pow(owned)
    private var income: Float = income
    private var incomeTime: Float = incomeTime
    private var time: Float = 0f

    private lateinit var activateButton: Button
    private lateinit var ownedLabel: Label
    private lateinit var timeLabel: Label
    private lateinit var buyLabel: Label
    private lateinit var timeProgress: BaseActor

    init {
        loadAnimation(BaseGame.textureAtlas!!.findRegion("whitePixel"))
        width = selfWidth
        height = selfHeight
        // color = Color(random(0, 255) / 255f, random(0, 255) / 255f, random(0, 255) / 255f, 1f)
        color = Color.DARK_GRAY

        nameLabel = Label(name, BaseGame.labelStyle)
        nameLabel.setFontScale(.75f)

        // load game state
        owned = BaseGame.prefs!!.getInteger(name + "Owned")
        time = BaseGame.prefs!!.getFloat(name + "Time")
        activated = BaseGame.prefs!!.getBoolean(name + "Activated")
        price = baseCost * multiplier.pow(owned)
        addLoveSinceLastTimedPlayed()

        // table
        table = Table()
        table.width = selfWidth
        table.height = selfHeight

        table.add(nameLabel).top().colspan(3).row()
        table.add(leftTable(s)).pad(selfWidth * .01f) // TODO: set height and width here
        table.add(rightTable(s))

        addActor(table)

        /*table.debug()
        this.debug*/
    }

    override fun act(dt: Float) {
        super.act(dt)

        if (time >= incomeTime) {
            BaseGame.love += income * owned

            // activated = false
            activated = true
            time = 0f
            timeLabel.setText("0s")
            timeProgress.width = 0f
        }

        if (activated) {
            time += dt
            BaseGame.prefs!!.putFloat(resourceName + "Time", time)
            timeLabel.setText("${incomeTime - time.toInt()}s")
            timeProgress.width = (selfWidth * .68f) * (time / incomeTime)
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
        activateButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                if (owned > 0) {
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
        val buttonStyle = Button.ButtonStyle()
        var buttonTex = BaseGame.textureAtlas!!.findRegion("whitePixel")
        val buttonRegion = TextureRegion(buttonTex)
        buttonStyle.up = TextureRegionDrawable(buttonRegion)

        buyLabel = Label("Price: ${ceil(price).toInt()}", BaseGame.labelStyle)
        buyLabel.setFontScale(.5f)
        val buy = Button(buttonStyle)
        buy.addActor(buyLabel)
        buy.color = Color.ORANGE
        buy.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                timeProgress.setPosition(0f, timeProgress.y) // TODO: solves some weird displacement bug...
                if (BaseGame.love >= price) {
                    purchased = true
                    BaseGame.love -= price
                    owned++
                    BaseGame.prefs!!.putInteger(resourceName + "Owned", owned)
                    ownedLabel.setText("$owned")
                    price = baseCost * multiplier.pow(owned)
                    buyLabel.setText("Price: ${ceil(price).toInt()}")
                }
            }
            false
        }

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
            BaseGame.love += (income / incomeTime) * BaseGame.secondsSinceLastPlayed
            time += BaseGame.secondsSinceLastPlayed % time
        }
    }
}
