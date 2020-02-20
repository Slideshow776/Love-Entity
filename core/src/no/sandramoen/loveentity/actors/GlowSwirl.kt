package no.sandramoen.loveentity.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import no.sandramoen.loveentity.utils.BaseActor
import no.sandramoen.loveentity.utils.BaseGame

class GlowSwirl(x: Float, y: Float, s: Stage): BaseActor(x, y, s) {
    init {
        loadAnimation(BaseGame.textureAtlas!!.findRegion("glowSwirl"))
        width *= Gdx.graphics.width * .0025f
        height *= Gdx.graphics.width * .0025f
        setOrigin(Align.center)
        color.a = .5f
    }
}
