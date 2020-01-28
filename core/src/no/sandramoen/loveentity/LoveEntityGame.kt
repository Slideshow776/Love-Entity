package no.sandramoen.loveentity

import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.screens.shell.SplashScreen

class LoveEntityGame : BaseGame() {
    override fun create() {
        super.create()
        setActiveScreen(SplashScreen())
        // setActiveScreen(LevelScreen())
    }
}
