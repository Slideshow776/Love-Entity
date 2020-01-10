package no.sandramoen.loveentity.utils

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable

abstract class BaseGame : Game() {
    init {
        game = this
    }

    companion object {
        private var game: BaseGame? = null

        var labelStyle: LabelStyle? = null
        var textButtonStyle: TextButtonStyle? = null

        // game state
        var prefs: Preferences? = null
        var love = 0f

        fun setActiveScreen(s: BaseScreen) {
            game?.setScreen(s)
        }
    }

    override fun create() {
        Gdx.input.inputProcessor = InputMultiplexer()

        prefs = Gdx.app.getPreferences("loveEntityGameState")
        love = prefs!!.getFloat("love")

        val fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans.ttf"))
        val fontParameters = FreeTypeFontParameter()
        fontParameters.size = (.059f * Gdx.graphics.height).toInt() // If the resolutions height is 1440 then the font size becomes 86
        fontParameters.color = Color.WHITE
        fontParameters.borderWidth = 2f
        fontParameters.borderColor = Color.BLACK
        fontParameters.borderStraight = true
        fontParameters.minFilter = Texture.TextureFilter.Linear
        fontParameters.magFilter = Texture.TextureFilter.Linear

        val customFont = fontGenerator.generateFont(fontParameters)

        labelStyle = LabelStyle()
        labelStyle!!.font = customFont

        textButtonStyle = TextButtonStyle()
        val buttonTex = Texture(Gdx.files.internal("images/button.png"))
        val buttonPatch = NinePatch(buttonTex, 24, 24, 24, 24)
        textButtonStyle!!.up = NinePatchDrawable(buttonPatch)
        textButtonStyle!!.font = customFont
        textButtonStyle!!.fontColor = Color.PINK
    }

    override fun dispose() {
        super.dispose()

        // save game state
        prefs!!.putFloat("love", love)
        prefs!!.flush()
    }
}
