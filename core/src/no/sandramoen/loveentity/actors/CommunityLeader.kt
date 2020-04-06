package no.sandramoen.loveentity.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import no.sandramoen.loveentity.utils.BaseActor
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.GameUtils
import java.math.BigInteger

class CommunityLeader(s: Stage, id: Int, avatarImage: String, name: String, descriptionEN: String, descriptionNO: String, price: BigInteger) : BaseActor(0f, 0f, s) {
    var remove = false
    var hideTable: Table
    var price: BigInteger = price
    var button: TextButton

    private var selfWidth = Gdx.graphics.width * .9f
    private var selfHeight = Gdx.graphics.height * .1f

    private var avatar: BaseActor
    private var nameLabel: Label
    private var descriptionLabel: Label
    private var heartIcon: BaseActor
    private var costLabel: Label

    private var descriptionEN: String
    private var descriptionNO: String

    init {
        this.isVisible = false // solves a visibility bug
        loadAnimation(BaseGame.textureAtlas!!.findRegion("whitePixel"))
        color = Color(MathUtils.random(0, 100) / 255f, MathUtils.random(0, 100) / 255f, MathUtils.random(0, 100) / 255f, 1f)
        width = selfWidth
        height = selfHeight
        this.descriptionEN = descriptionEN
        this.descriptionNO = descriptionNO

        // avatar image
        avatar = BaseActor(0f, 0f, s)
        avatar.loadAnimation(BaseGame.textureAtlas!!.findRegion(avatarImage))
        avatar.width = avatar.width * (selfHeight / avatar.height) // ensure aspect ratio
        avatar.height = selfHeight

        // name
        nameLabel = Label(name, BaseGame.labelStyle)
        nameLabel.setFontScale(.5f)
        nameLabel.color = Color(MathUtils.random(100, 255) / 255f, MathUtils.random(100, 255) / 255f, MathUtils.random(100, 255) / 255f, 1f)

        // description
        if (BaseGame.english)
            descriptionLabel = Label(descriptionEN, BaseGame.labelStyle)
        else
            descriptionLabel = Label(descriptionNO, BaseGame.labelStyle)
        descriptionLabel.setFontScale(.25f)
        descriptionLabel.color = Color.LIGHT_GRAY

        // cost image
        heartIcon = BaseActor(0f, 0f, s)
        heartIcon.loadAnimation(BaseGame.textureAtlas!!.findRegion("heart"))
        heartIcon.width = 40f
        heartIcon.height = 40f

        // cost
        if (BaseGame.longScale)
            costLabel = Label("${GameUtils.presentLongScale(price)}", BaseGame.labelStyle)
        else
            costLabel = Label("${GameUtils.presentShortScale(price)}", BaseGame.labelStyle)
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
        if (BaseGame.english)
            button = TextButton("Recruit!", BaseGame.textButtonStyle)
        else
            button = TextButton("Rekrutter!", BaseGame.textButtonStyle)
        button.label.color = Color.WHITE
        button.label.setFontScale(.72f)
        button.isTransform = true
        if (BaseGame.love >= price) button.color = Color.ORANGE
        else button.color = Color.GRAY
        button.scaleBy(-.04f)
        button.setOrigin(Align.center)
        button.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                if (BaseGame.love >= price) {
                    if (!BaseGame.muteAudio) BaseGame.piingSound!!.play(.25f)
                    BaseGame.love = BaseGame.love.subtract(price)
                    GameUtils.saveGameState()
                    BaseGame.resourceGenerators[id].hasCommunityLeader = true
                    BaseGame.prefs!!.putBoolean(BaseGame.resourceGenerators[id].resourceName + "HasCommunityLeader", true)

                    addAction(Actions.sequence(
                            Actions.parallel(
                                    Actions.alpha(0f, .5f, Interpolation.linear),
                                    Actions.run { avatar.addAction(Actions.alpha(0f, .5f, Interpolation.linear)) },
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

        hideTable.add(hideLabel).colspan(2).row()
        // hideTable.debug = true

        // table layout
        val table = Table()
        table.width = Gdx.graphics.width.toFloat() * 1.0f
        table.height = selfHeight

        table.add(avatar).width(Gdx.graphics.width * .16f).padLeft(Gdx.graphics.width * .025f)
        table.add(infoTable).width(Gdx.graphics.width * .44f)
        table.add(button).width(Gdx.graphics.width * .3f).padRight(Gdx.graphics.width * .025f)
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
        if (BaseGame.love >= price)
            button.color = Color.ORANGE
        else
            button.color = Color.GRAY
    }


    fun checkLanguage() {
        if (BaseGame.english) {
            descriptionLabel.setText(descriptionEN)
            button.label.setText("Recruit!")
        } else {
            descriptionLabel.setText(descriptionNO)
            button.label.setText("Rekrutter!")
        }
    }

    fun checkScale() {
        if (BaseGame.longScale)
            costLabel.setText("${GameUtils.presentLongScale(BigInteger(price.toString()))}")
        else
            costLabel.setText("${GameUtils.presentShortScale(BigInteger(price.toString()))}")
    }

    override fun act(dt: Float) {
        super.act(dt)
        checkAffordable()
    }
}
