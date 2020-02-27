package no.sandramoen.loveentity.screens.shell

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import no.sandramoen.loveentity.screens.gameplay.LevelScreen
import no.sandramoen.loveentity.utils.BaseActor
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.BaseScreen

class IntroScreen : BaseScreen() {
    private lateinit var mainLabel: Label
    private lateinit var colorLabel: Label
    private var labelFollow = true
    private lateinit var loveEntity: BaseActor
    private lateinit var halo: BaseActor

    override fun initialize() {
        // setup
        val width = Gdx.graphics.width.toFloat()
        val height = Gdx.graphics.height.toFloat()

        val background = BaseActor(0f, 0f, mainStage)
        background.loadAnimation(BaseGame.textureAtlas!!.findRegion("starBackground"))
        background.width = width
        background.height = height
        background.color.a = 0f
        background.addAction(sequence(
                delay(3f),
                delay(1f),
                fadeIn(4f)
        ))
        mainStage.addActor(background)

        colorLabel = Label("kjærlighet", BaseGame.labelStyle)
        colorLabel.setPosition(width * 2f, 0f)
        colorLabel.setFontScale(.7f)
        colorLabel.setText("")
        mainStage.addActor(colorLabel)

        mainLabel = Label("Først var det lys", BaseGame.labelStyle)
        mainLabel.color.a = 0f
        mainLabel.setFontScale(.8f)
        mainLabel.setPosition(width * .15f, height / 2f)
        mainStage.addActor(mainLabel)
        if (BaseGame.english) {
            mainLabel.setText("First there was light")
            mainLabel.setPosition(width * .07f, height / 2f)
        }

        val prism = BaseActor(0f, 0f, mainStage)
        prism.loadAnimation(BaseGame.textureAtlas!!.findRegion("prism"))
        prism.width = width * 2.5f
        prism.height = prism.width / 3.5f // image ratio
        prism.color.a = 0f
        prism.rotateBy(-9f)
        prism.setPosition(width * -.05f, height * .3f)
        mainStage.addActor(prism)

        val starChild = BaseActor(0f, 0f, mainStage)
        starChild.loadAnimation(BaseGame.textureAtlas!!.findRegion("starChild"))
        starChild.width = width * .25f
        starChild.height = height * .15f
        starChild.color.a = 0f
        starChild.setPosition(width * .5f - starChild.width * .5f, height)
        mainStage.addActor(starChild)

        halo = BaseActor(0f, 0f, mainStage)
        halo.loadAnimation(BaseGame.textureAtlas!!.findRegion("colorWheelGradient"))
        halo.width = width * .42f
        halo.height = width * .42f
        halo.setPosition(width * .5f - halo.width * .5f, height * .5f)
        halo.setOrigin(Align.center)
        mainStage.addActor(halo)

        loveEntity = BaseActor(0f, 0f, mainStage)
        loveEntity.loadAnimation(BaseGame.textureAtlas!!.findRegion("loveEntity"))
        loveEntity.width = width * .7f
        loveEntity.height = height * .45f
        loveEntity.setPosition(width * .5f - loveEntity.width * .5f, height * .2f)
        loveEntity.color.a = 0f
        mainStage.addActor(loveEntity)

        // animation
        mainLabel.addAction(sequence(
                delay(3f),
                // Scene 1
                fadeIn(1f),
                moveBy(-width, 0f, 2f),
                // Scene 2
                Actions.run {
                    if (BaseGame.english)
                        mainLabel.setText("Then there was ")
                    else
                        mainLabel.setText("Så var det ")
                    mainLabel.setFontScale(.7f)
                    mainLabel.color.a = 0f
                },
                moveTo(width * .1f, height * .65f),
                fadeIn(2f),
                delay(1.5f),
                moveBy(0f, -height * .8f, 3f),
                // Scene 3
                Actions.run {
                    labelFollow = false
                    colorLabel.setPosition(width * 2f, 0f)
                    mainLabel.color.a = 0f
                    if (BaseGame.english) {
                        mainLabel.setText("You are a ")
                        mainLabel.setPosition(width * .32f, height * .8f)
                    } else {
                        mainLabel.setText("Du er en ")
                        mainLabel.setPosition(width * .35f, height * .8f)
                    }
                },
                fadeIn(3f),
                // Scene 4
                delay(1.6f),
                fadeOut(1f),
                Actions.run {
                    if (BaseGame.english) {
                        mainLabel.setText("How much can you")
                        mainLabel.setPosition(width * .15f, height * .8f)
                    } else {
                        mainLabel.setText("Hvor mye kan du")
                        mainLabel.setPosition(width * .18f, height * .8f)
                    }
                },
                fadeIn(1f),
                delay(5f),
                fadeOut(1f)
        ))
        colorLabel.addAction(sequence(
                delay(3f),
                // Scene 2
                delay(2.9f),
                Actions.run {
                    if (BaseGame.english) colorLabel.setText("love")
                    else colorLabel.setText("kjærlighet")
                },
                color(Color.PINK, 2f),
                // Scene 3
                delay(5f),
                Actions.run {
                    colorLabel.color = Color.WHITE
                    if (BaseGame.english) {
                        colorLabel.setText("love entity")
                        colorLabel.setPosition(width * .3f, height * .74f)
                    } else {
                        colorLabel.setText("kjærlighets-entitet")
                        colorLabel.setPosition(width * .16f, height * .74f)
                    }
                },
                delay(1.0f),
                color(Color.PINK, 4f),
                // Scene 4
                Actions.run {
                    if (BaseGame.english) {
                        colorLabel.setText("love?")
                        colorLabel.setPosition(width * .4f, height * .74f)
                    } else {
                        colorLabel.setText("elske?")
                        colorLabel.setPosition(width * .38f, height * .74f)
                    }
                }
        ))
        prism.addAction(sequence(
                delay(3f),
                // Scene 2
                delay(1f),
                fadeIn(2f),
                parallel(
                        moveBy(-width * 1.1f, height * -.1f, 2f),
                        rotateBy(9f, 2f)
                ),
                delay(1.5f),
                moveBy(0f, -height * .8f, 3f)
        ))
        starChild.addAction(sequence(
                delay(3f),
                // Scene 3
                delay(10f),
                moveBy(0f, height * -.5f, 0f),
                parallel(
                        fadeIn(1.25f),
                        sequence(
                                moveBy(0f, height * -.025f, 2f),
                                moveBy(0f, height * .025f, 2f)
                        )
                ),
                parallel(
                        fadeOut(1.25f),
                        sequence(
                                moveBy(0f, height * -.025f, 2f),
                                moveBy(0f, height * .025f, 2f)
                        )
                )
        ))
        halo.addAction(sequence(
                forever(rotateBy(.5f))
        ))
        loveEntity.addAction(sequence(
                delay(3f),
                delay(15f),
                parallel(
                        fadeIn(1f),
                        sequence(
                                moveBy(0f, height * -.025f, 2f),
                                moveBy(0f, height * .025f, 2f),
                                moveBy(0f, height * -.025f, 2f)
                        )
                ),
                parallel(
                        fadeOut(1f),
                        sequence(
                                moveBy(0f, height * .025f, 2f),
                                moveBy(0f, height * -.025f, 2f),
                                moveBy(0f, height * .025f, 2f)
                        )
                )
        ))
        background.addAction(sequence(
                delay(25f),
                fadeOut(2f)
        ))
        mainLabel.addAction(sequence(
                delay(28f),
                Actions.run { setLevelScreen() })
        )
    }

    override fun update(dt: Float) {
        if (labelFollow) {
            colorLabel.y = mainLabel.y
            if (BaseGame.english)
                colorLabel.x = mainLabel.x + Gdx.graphics.width * .6f
            else
                colorLabel.x = mainLabel.x + Gdx.graphics.width * .4f
        }
        halo.y = loveEntity.y + Gdx.graphics.height * .27f
        halo.color.a = loveEntity.color.a
        colorLabel.color.a = mainLabel.color.a
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        setLevelScreen()
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK)
            Gdx.app.exit()
        return false
    }

    private fun setLevelScreen() {
        BaseGame.prefs!!.putBoolean("skipIntro", true)
        BaseGame.setActiveScreen(LevelScreen())
    }
}
