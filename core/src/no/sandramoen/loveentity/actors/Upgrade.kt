package no.sandramoen.loveentity.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import no.sandramoen.loveentity.utils.BaseActor
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.BigNumber
import no.sandramoen.loveentity.utils.GameUtils

class Upgrade(s: Stage, id: Int, upgradeImage: String, name: String, description: String, price: BigNumber) : BaseActor(0f, 0f, s) {
    var remove = false
    var hideTable: Table
    var id = id

    private var selfWidth = Gdx.graphics.width * .9f
    private var selfHeight = Gdx.graphics.height * .1f

    private var image: BaseActor
    private var nameLabel: Label
    private var descriptionLabel: Label
    private var heartIcon: BaseActor
    private var costLabel: Label
    private var button: TextButton

    private var price = price

    init {
        this.isVisible = false // solves a visibility bug
        loadAnimation(BaseGame.textureAtlas!!.findRegion("whitePixel"))
        color = Color(com.badlogic.gdx.math.MathUtils.random(0, 100) / 255f, MathUtils.random(0, 100) / 255f, MathUtils.random(0, 100) / 255f, 1f)
        width = selfWidth
        height = selfHeight

        // image
        image = BaseActor(0f, 0f, s)
        image.loadAnimation(BaseGame.textureAtlas!!.findRegion(upgradeImage))
        image.width = image.width * (selfHeight / image.height) // ensure aspect ratio
        image.height = selfHeight

        // name
        nameLabel = Label(name, BaseGame.labelStyle)
        nameLabel.setFontScale(.5f)
        nameLabel.color = Color(MathUtils.random(100, 255) / 255f, MathUtils.random(100, 255) / 255f, MathUtils.random(100, 255) / 255f, 1f)

        // description
        descriptionLabel = Label(description, BaseGame.labelStyle)
        descriptionLabel.setFontScale(.25f)
        descriptionLabel.color = Color.GRAY

        // cost image
        heartIcon = BaseActor(0f, 0f, s)
        heartIcon.loadAnimation(BaseGame.textureAtlas!!.findRegion("heart"))
        heartIcon.width = 40f
        heartIcon.height = 40f

        // cost
        costLabel = Label("${price.presentLongScale()}", BaseGame.labelStyle)
        costLabel.setFontScale(.5f)

        val infoTable = Table()
        infoTable.width = selfWidth // fill x
        infoTable.height = selfHeight // fill y
        infoTable.add(nameLabel).colspan(2).row()
        infoTable.add(descriptionLabel).colspan(2).row()
        infoTable.add(heartIcon).right().padRight(10f)
        infoTable.add(costLabel).left()
        // infoTable.debug = true

        // button
        button = TextButton("Invest!", BaseGame.textButtonStyle)
        button.isTransform = true
        if (!BaseGame.love.isGreaterThanOrEqualTo(price))
            button.color = Color.DARK_GRAY
        button.scaleBy(-.2f)
        button.setOrigin(Align.center)
        button.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                if (BaseGame.love.isGreaterThanOrEqualTo(price)) {
                    BaseGame.love = BaseGame.love.subtract(BaseGame.love, price)
                    GameUtils.saveGameState()
                    BaseGame.resourceGenerators[id].upgrade *= 3 // this systems assumes all upgrades are multiplicable of 3's
                    BaseGame.prefs!!.putInteger(BaseGame.resourceGenerators[id].resourceName + "Upgrade", BaseGame.resourceGenerators[id].upgrade)

                    addAction(Actions.sequence(
                            Actions.parallel(
                                    Actions.alpha(0f, .5f, Interpolation.linear),
                                    Actions.run { image.addAction(Actions.alpha(0f, .5f, Interpolation.linear)) },
                                    Actions.run { heartIcon.addAction(Actions.alpha(0f, .5f, Interpolation.linear)) }
                            ),
                            Actions.run { remove = true }
                    ))
                }
            }
            false
        }

        // hide table
        val hideLabel = Label("???", BaseGame.labelStyle)
        hideLabel.color = Color.PURPLE

        hideTable = Table()
        hideTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(MathUtils.random(.1f, .2f), MathUtils.random(.1f, .2f), MathUtils.random(.1f, .2f), 1f))
        hideTable.isVisible = true
        hideTable.isTransform = true
        hideTable.setOrigin(0f, Gdx.graphics.height * .058f)
        hideTable.scaleBy(0f, .355f)

        hideTable.add(hideLabel).colspan(2).row()
        // hideTable.debug = true

        // table layout
        val table = Table()
        table.width = Gdx.graphics.width.toFloat() * 1.0f
        table.height = selfHeight

        table.add(image).padRight(20f)
        table.add(infoTable)
        table.add(button).padLeft(-40f)
        // table.debug = true

        val stack = Stack() // stack allows for scene2d elements to overlap each other
        stack.add(table)
        stack.add(hideTable)
        stack.width = selfWidth // fill x
        stack.height = selfHeight // fill y
        addActor(stack)
        // debug()
    }

    fun checkAffordable() {
        if (BaseGame.love.isGreaterThanOrEqualTo(price))
            button.color = Color.WHITE
        else
            button.color = Color.DARK_GRAY
    }

    override fun act(dt: Float) {
        super.act(dt)
        checkAffordable()
    }
}
