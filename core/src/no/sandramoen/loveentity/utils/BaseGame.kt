package no.sandramoen.loveentity.utils

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetErrorListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.utils.Array
import no.sandramoen.loveentity.actors.ResourceGenerator
import java.util.Date

abstract class BaseGame : Game(), AssetErrorListener {

    private lateinit var assetManager: AssetManager
    private lateinit var fontGenerator: FreeTypeFontGenerator

    init {
        game = this
    }

    companion object {
        private var game: BaseGame? = null

        var labelStyle: LabelStyle? = null
        var textButtonStyle: TextButtonStyle? = null
        var textureAtlas: TextureAtlas? = null

        // game state
        var prefs: Preferences? = null
        lateinit var love: BigNumber
        lateinit var lifeTimeLove: BigNumber
        var revealNextGeneratorIndex = 0
        var lastTimePlayed = 0L
        var secondsSinceLastPlayed = 0L
        lateinit var resourceGenerators: Array<ResourceGenerator>
        var currentAscensionPoints: Long = 1L
        var claimAscensionPoints: Long = 0L

        fun setActiveScreen(s: BaseScreen) {
            game?.setScreen(s)
        }
    }

    override fun create() {
        Gdx.input.inputProcessor = InputMultiplexer() // discrete input

        // global variables
        prefs = Gdx.app.getPreferences("loveEntityGameState")
        love = GameUtils.getBigNumber("love")
        lifeTimeLove = GameUtils.getBigNumber("lifeTimeLove")
        if (love.maxNumber.size == 0) love = BigNumber(0)
        if (lifeTimeLove.maxNumber.size == 0) lifeTimeLove = BigNumber(0)
        revealNextGeneratorIndex = BaseGame.prefs!!.getInteger("revealNextGeneratorIndex")
        lastTimePlayed = prefs!!.getLong("lastTimePlayed")
        if (lastTimePlayed != 0L) secondsSinceLastPlayed = (Date().time - lastTimePlayed) / 1000
        resourceGenerators = Array()
        currentAscensionPoints = prefs!!.getLong("currentAscensionPoints")

        // asset manager
        assetManager = AssetManager()
        assetManager.setErrorListener(this)
        assetManager.load("images/packed/loveEntity.pack.atlas", TextureAtlas::class.java)
        val resolver = InternalFileHandleResolver()
        assetManager.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        assetManager.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))
        assetManager.finishLoading();
        textureAtlas = assetManager.get("images/packed/loveEntity.pack.atlas") // all images are found in this global static variable

        // fonts
        FreeTypeFontGenerator.setMaxTextureSize(2048) // solves font bug that won't show some characters like "." and "," in android
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

        val buttonFontParameters = FreeTypeFontParameter()
        buttonFontParameters.size = (.04f * Gdx.graphics.height).toInt() // If the resolutions height is 1440 then the font size becomes 86
        buttonFontParameters.color = Color.WHITE
        buttonFontParameters.borderWidth = 2f
        buttonFontParameters.borderColor = Color.BLACK
        buttonFontParameters.borderStraight = true
        buttonFontParameters.minFilter = Texture.TextureFilter.Linear
        buttonFontParameters.magFilter = Texture.TextureFilter.Linear
        val buttonCustomFont = fontGenerator.generateFont(buttonFontParameters)

        labelStyle = LabelStyle()
        labelStyle!!.font = customFont

        textButtonStyle = TextButtonStyle()
        val buttonTex = textureAtlas!!.findRegion("button")
        val buttonPatch = NinePatch(buttonTex, 24, 24, 24, 24)
        textButtonStyle!!.up = NinePatchDrawable(buttonPatch)
        textButtonStyle!!.font = buttonCustomFont
        textButtonStyle!!.fontColor = Color.PINK
    }

    override fun resume() {
        super.resume()

        // add love generated since pausing
        lastTimePlayed = prefs!!.getLong("lastTimePlayed")
        if (lastTimePlayed != 0L) secondsSinceLastPlayed = (Date().time - lastTimePlayed) / 1000
        for (generator in BaseGame.resourceGenerators)
            generator.addLoveSinceLastTimedPlayed()
    }

    override fun pause() {
        super.pause()
        GameUtils.saveGameState()
    }

    override fun dispose() {
        GameUtils.saveGameState()
        super.dispose()

        /*assetManager.dispose()
        fontGenerator.dispose()*/
        try { // TODO: uncomment this when development is done
            assetManager.dispose()
            fontGenerator.dispose()
        } catch (error: UninitializedPropertyAccessException) {
            Gdx.app.error("BaseGame", "Error $error")
        }
    }

    override fun error(asset: AssetDescriptor<*>, throwable: Throwable) {
        Gdx.app.error("BaseGame.kt", "Could not load asset: " + asset.fileName, throwable)
    }
}
