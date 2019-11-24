package no.sandramoen.loveentity

import no.sandramoen.loveentity.screens.LevelScreen
import no.sandramoen.loveentity.utils.BaseGame

class LoveEntityGame : BaseGame() {
    override fun create() {
        super.create()
        setActiveScreen(LevelScreen())
    }
}
