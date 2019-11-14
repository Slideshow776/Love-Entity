package no.sandramoen.loveclicker

import no.sandramoen.loveclicker.screens.LevelScreen
import no.sandramoen.loveclicker.utils.BaseGame

class LoveclickerGame : BaseGame() {
    override fun create() {
        super.create()
        setActiveScreen(LevelScreen())
    }
}
