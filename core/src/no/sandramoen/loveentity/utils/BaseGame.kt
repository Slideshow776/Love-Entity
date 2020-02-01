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
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.utils.Array
import no.sandramoen.loveentity.actors.CommunityLeader
import no.sandramoen.loveentity.actors.ResourceGenerator
import no.sandramoen.loveentity.actors.Upgrade
import java.math.BigInteger
import java.util.Date

abstract class BaseGame : Game(), AssetErrorListener {
    init {
        game = this
    }

    companion object {
        private var game: BaseGame? = null

        lateinit var assetManager: AssetManager
        lateinit var fontGenerator: FreeTypeFontGenerator

        var labelStyle: LabelStyle? = null
        var textButtonStyle: TextButtonStyle? = null
        var textureAtlas: TextureAtlas? = null
        var splashAnim: Animation<TextureRegion>? = null
        var splashTexture: Texture? = null

        // game state
        var prefs: Preferences? = null
        lateinit var love: BigInteger
        lateinit var lifeTimeLove: BigInteger
        var revealNextGeneratorIndex = 0
        var lastTimePlayed = 0L
        var secondsSinceLastPlayed = 0L
        lateinit var resourceGenerators: Array<ResourceGenerator>
        lateinit var communityLeaders: Array<CommunityLeader>
        var communityLeadersWiggleIndex = 0
        var upgradesWiggleIndex = 0
        lateinit var upgrades: Array<Upgrade>
        var currentAscensionPoints: Long = 1L
        var claimAscensionPoints: Long = 0L
        var ascensionBonus = 2
        var heartBonus = 1
        var buyIndex = 1
        var heartTouched = false
        var wiggleAscension = true

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
        if (love == BigInteger.ZERO) love = BigInteger("0")
        if (lifeTimeLove == BigInteger.ZERO) lifeTimeLove = BigInteger("0")
        revealNextGeneratorIndex = BaseGame.prefs!!.getInteger("revealNextGeneratorIndex")
        lastTimePlayed = prefs!!.getLong("lastTimePlayed")
        if (lastTimePlayed != 0L) secondsSinceLastPlayed = (Date().time - lastTimePlayed) / 1000
        resourceGenerators = Array()
        communityLeaders = Array()
        communityLeadersWiggleIndex = prefs!!.getInteger("communityLeadersWiggleIndex")
        upgrades = Array()
        upgradesWiggleIndex = prefs!!.getInteger("upgradesWiggleIndex")
        currentAscensionPoints = prefs!!.getLong("currentAscensionPoints")
        wiggleAscension = prefs!!.getBoolean("wiggleAscension")

        // asset manager
        assetManager = AssetManager()
        assetManager.setErrorListener(this)
        assetManager.load("images/included/packed/loveEntity.pack.atlas", TextureAtlas::class.java)
        val resolver = InternalFileHandleResolver()
        assetManager.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        assetManager.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))
        assetManager.finishLoading();
        textureAtlas = assetManager.get("images/included/packed/loveEntity.pack.atlas") // all images are found in this global static variable

        // images that are excluded from the asset manager
        splashTexture = Texture(Gdx.files.internal("images/excluded/splash.jpg"))
        splashTexture!!.setFilter(TextureFilter.Nearest, TextureFilter.Nearest)
        splashAnim = Animation(1f, TextureRegion(splashTexture))

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
        for (generator in resourceGenerators)
            generator.addLoveSinceLastTimedPlayed()
    }

    override fun pause() {
        super.pause()
        GameUtils.saveGameState()
    }

    override fun dispose() {
        GameUtils.saveGameState()
        super.dispose()

        assetManager.dispose()
        fontGenerator.dispose()
        /*try { // TODO: uncomment this when development is done
            assetManager.dispose()
            fontGenerator.dispose()
        } catch (error: UninitializedPropertyAccessException) {
            Gdx.app.error("BaseGame", "Error $error")
        }*/
    }

    override fun error(asset: AssetDescriptor<*>, throwable: Throwable) {
        Gdx.app.error("BaseGame.kt", "Could not load asset: " + asset.fileName, throwable)
    }
}
