package no.sandramoen.loveentity.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import no.sandramoen.loveentity.utils.BaseActor
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.BigNumber
import no.sandramoen.loveentity.utils.GameUtils

class CommunityLeader(s: Stage, id: Int, avatarImage: String, name: String, description: String, price: BigNumber) : BaseActor(0f, 0f, s) {

    var remove = false

    private var selfWidth = Gdx.graphics.width * .9f
    private var selfHeight = Gdx.graphics.height * .1f

    private var avatar: BaseActor
    private var nameLabel: Label
    private var descriptionLabel: Label
    private var heartIcon: BaseActor
    private var costLabel: Label
    private var button: TextButton

    init {
        loadAnimation(BaseGame.textureAtlas!!.findRegion("whitePixel"))
        color = Color(com.badlogic.gdx.math.MathUtils.random(0, 100) / 255f, MathUtils.random(0, 100) / 255f, MathUtils.random(0, 100) / 255f, 1f)
        width = selfWidth
        height = selfHeight

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
        button = TextButton("Recruit", BaseGame.textButtonStyle)
        button.isTransform = true
        if (!BaseGame.love.isGreaterThanOrEqualTo(price)) button.color = Color.DARK_GRAY
        button.scaleBy(-.5f)
        button.setOrigin(Align.center)
        button.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                if (BaseGame.love.isGreaterThanOrEqualTo(price)) {
                    BaseGame.love.subtract(BaseGame.love, price)
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

        val table = Table()
        table.width = Gdx.graphics.width.toFloat() * 1.0f
        table.height = selfHeight

        table.add(avatar).padRight(20f)
        table.add(infoTable)
        table.add(button).padLeft(-40f)
        // table.debug = true

        addActor(table)
        // debug()
    }

    override fun act(dt: Float) {
        super.act(dt)
    }
}