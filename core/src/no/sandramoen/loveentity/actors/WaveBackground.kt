package no.sandramoen.loveentity.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import no.sandramoen.loveentity.utils.BaseActor
import no.sandramoen.loveentity.utils.BaseGame

class WaveBackground(x: Float, y: Float, texturePath: String, s: Stage) : BaseActor(x, y, s) {
    private var vertexShaderCode: String
    private var fragmenterShaderCode: String
    private var shaderProgram: ShaderProgram

    var time = .0f

    init {
        loadAnimation(BaseGame.textureAtlas!!.findRegion(texturePath))

        vertexShaderCode = Gdx.files.internal("shaders/default.vs").readString()
        fragmenterShaderCode = Gdx.files.internal("shaders/wave.fs").readString()
        shaderProgram = ShaderProgram(vertexShaderCode, fragmenterShaderCode)
        if (!shaderProgram.isCompiled)
            Gdx.app.error("WaveBackground.kt","Shader compile error: " + shaderProgram.log)

        this.color.a = .4f
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.shader = shaderProgram
        shaderProgram.setUniformf("u_time", time)
        shaderProgram.setUniformf("u_imageSize", Vector2(width, height))
        shaderProgram.setUniformf("u_amplitude", Vector2(2f, 3f))
        shaderProgram.setUniformf("u_wavelength", Vector2(170f, 400f))
        shaderProgram.setUniformf("u_velocity", Vector2(0f, 30f))
        super.draw(batch, parentAlpha)
        batch.shader = null
    }

    override fun act(dt: Float) {
        super.act(dt)
        time += dt
    }
}
