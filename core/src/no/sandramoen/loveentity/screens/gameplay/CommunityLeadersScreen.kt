package no.sandramoen.loveentity.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import no.sandramoen.loveentity.actors.CommunityLeader
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.BaseScreen
import no.sandramoen.loveentity.utils.BigNumber
import no.sandramoen.loveentity.utils.GameUtils

class CommunityLeadersScreen : BaseScreen() {
    private lateinit var titleLabel: Label
    private lateinit var loveLabel: Label
    private lateinit var subtitleLabel: Label
    private lateinit var descriptionLabel: Label

    private lateinit var communityLeaders: Array<CommunityLeader>
    private lateinit var communityLeadersTable: Table

    override fun initialize() {
        titleLabel = Label("Community Leaders", BaseGame.labelStyle)
        titleLabel.setFontScale(.7f)

        loveLabel = Label("${BaseGame.love.presentLongScale()} love", BaseGame.labelStyle)
        loveLabel.setFontScale(.5f)

        val exitButtonStyle = Button.ButtonStyle()
        exitButtonStyle.up = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("cross-white")))
        val exitButton = Button(exitButtonStyle)
        exitButton.isTransform = true
        exitButton.scaleBy(-.5f)
        exitButton.setOrigin(Align.top)
        exitButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                BaseGame.setActiveScreen(LevelScreen())
            false
        }

        // info table
        val infoLabel = Label("Cupcake ipsum dolor sit amet. Gingerbread marshmallow sugar plum pastry dragée gingerbread candy cookie. Bonbon dessert tiramisu dragée.\n" +
                "\n" + "Powder jelly-o lollipop. Cookie chupa chups powder cake muffin pudding. Soufflé cupcake chocolate apple pie danish toffee dessert powder. Cake pudding jelly cake jelly tootsie roll.", BaseGame.labelStyle)
        infoLabel.color = Color.PURPLE
        infoLabel.setWrap(true)
        infoLabel.setFontScale(.3f)

        val infoTable = Table()
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
        val infoButton = Button(infoButtonStyle)
        infoButton.isTransform = true
        infoButton.scaleBy(.2f)
        infoButton.setOrigin(Align.center)
        infoButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                infoTable.isVisible = !infoTable.isVisible
            false
        }

        // upper table
        subtitleLabel = Label("Community Leaders Make Compassion Easier!", BaseGame.labelStyle)
        subtitleLabel.setFontScale(.3f)
        descriptionLabel = Label("Recruit one to act as a continuous love hub for you when you're away!", BaseGame.labelStyle)
        descriptionLabel.setFontScale(.25f)

        val upperTable = Table()
        upperTable.add(titleLabel).expandX().center()
        upperTable.add(exitButton).row()
        upperTable.add(loveLabel)
        upperTable.add(infoButton).row()
        upperTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(.2f, .2f, .2f, 1f))

        // community leaders table
        communityLeadersTable = Table()
        communityLeaders = Array()
        communityLeaders.add(CommunityLeader(mainStage, 0, "pixelAvatarTest", "Name Nameson", "runs Allies", BigNumber(1_000)))
        communityLeaders.add(CommunityLeader(mainStage, 1, "pixelAvatarTest", "Name Nameson", "runs Bisexuals", BigNumber(10_000)))
        communityLeaders.add(CommunityLeader(mainStage, 2, "pixelAvatarTest", "Name Nameson", "runs Gays", BigNumber(100_000)))
        communityLeaders.add(CommunityLeader(mainStage, 3, "pixelAvatarTest", "Name Nameson", "runs Lesbians", BigNumber(1_000_000)))
        communityLeaders.add(CommunityLeader(mainStage, 4, "pixelAvatarTest", "Name Nameson", "runs Cisgenders", BigNumber(10_000_000)))
        communityLeaders.add(CommunityLeader(mainStage, 5, "pixelAvatarTest", "Name Nameson", "runs Queers", BigNumber(100_000_000)))
        communityLeaders.add(CommunityLeader(mainStage, 6, "pixelAvatarTest", "Name Nameson", "runs Transgenders", BigNumber(1_000_000_000)))
        communityLeaders.add(CommunityLeader(mainStage, 7, "pixelAvatarTest", "Name Nameson", "runs Intersexs", BigNumber(10_000_000_000)))
        communityLeaders.add(CommunityLeader(mainStage, 8, "pixelAvatarTest", "Name Nameson", "runs Pansexuals", BigNumber(100_000_000_000)))
        communityLeaders.add(CommunityLeader(mainStage, 9, "pixelAvatarTest", "Name Nameson", "runs Asexuals", BigNumber(1_000_000_000_000)))
        initializeCommunityLeaders()

        val scroll = ScrollPane(communityLeadersTable)
        val scrollableTable = Table()
        scrollableTable.add(scroll)

        // table layout
        val mainTable = Table()

        mainTable.add(upperTable)
        mainTable.row()
        mainTable.add(scrollableTable)

        val stack = Stack() // stack allows for scene2d elements to overlap each other
        stack.add(mainTable)
        stack.add(infoTable)
        stack.setFillParent(true)
        mainTable.align(Align.top)

        mainStage.addActor(stack)

        /*stack.debug = true
        mainTable.debug = true*/
    }

    override fun update(dt: Float) {
        loveLabel.setText("${BaseGame.love.presentLongScale()} love")

        for (i in 0 until communityLeaders.size) {
            if (communityLeaders[i].remove) {
                communityLeaders[i].remove()
                initializeCommunityLeaders()
                break
            }
        }

        for (generator in BaseGame.resourceGenerators)
            generator.act(Gdx.graphics.deltaTime)
    }

    private fun initializeCommunityLeaders() {
        communityLeadersTable.reset()
        communityLeadersTable.add(subtitleLabel).padTop(25f).row()
        communityLeadersTable.add(descriptionLabel).padBottom(25f).row()
        communityLeadersTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(.05f, .05f, .05f, 1f))

        for (i in 0 until BaseGame.resourceGenerators.size) { // assumes BaseGame.resourceGenerators.size == communityLeaders.size
            if (BaseGame.resourceGenerators[i].isVisible &&
                    !communityLeaders[i].remove &&
                    !BaseGame.resourceGenerators[i].hasCommunityLeader) {

                communityLeaders[i].isVisible = true
                communityLeaders[i].checkAffordable()
                communityLeadersTable.add(communityLeaders[i]).padBottom(Gdx.graphics.height * .07f).row()

                if (!BaseGame.resourceGenerators[i].hideTable.isVisible)
                    communityLeaders[i].hideTable.isVisible = false
            }
        }
    }
}
