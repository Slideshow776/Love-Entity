package no.sandramoen.loveentity

import no.sandramoen.loveentity.screens.gameplay.LevelScreen
import no.sandramoen.loveentity.screens.shell.IntroScreen
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.screens.shell.SplashScreen

class LoveEntityGame : BaseGame() {
    override fun create() {
        super.create()
        // setActiveScreen(IntroScreen())
        setActiveScreen(SplashScreen()) // TODO: @release: uncomment this
        //setActiveScreen(LevelScreen()) // TODO: @release: comment this
    }
}
