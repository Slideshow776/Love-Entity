package no.sandramoen.loveentity

import no.sandramoen.loveentity.screens.gameplay.CommunityLeadersScreen
import no.sandramoen.loveentity.screens.gameplay.LevelScreen
import no.sandramoen.loveentity.utils.BaseGame

class LoveEntityGame : BaseGame() {
    override fun create() {
        super.create()
        setActiveScreen(LevelScreen())
        // setActiveScreen(CommunityLeadersScreen())
    }
}
