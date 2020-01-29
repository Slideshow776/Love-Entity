package no.sandramoen.loveentity.utils

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import java.util.Date
import kotlin.math.sqrt

class GameUtils {
    companion object {
        fun isTouchDownEvent(e: Event): Boolean { // Custom type checker
            return e is InputEvent && e.type == InputEvent.Type.touchDown
        }

        fun saveGameState() {
            putBigNumber("love", BaseGame.love)
            putBigNumber("lifeTimeLove", BaseGame.love)
            BaseGame.prefs!!.putLong("lastTimePlayed", Date().time)
            BaseGame.prefs!!.putInteger("revealNextGeneratorIndex", BaseGame.revealNextGeneratorIndex)
            BaseGame.prefs!!.putLong("currentAscensionPoints", BaseGame.currentAscensionPoints)
            BaseGame.prefs!!.putInteger("communityLeadersWiggleIndex", BaseGame.communityLeadersWiggleIndex)
            BaseGame.prefs!!.putInteger("upgradesWiggleIndex", BaseGame.upgradesWiggleIndex)
            BaseGame.prefs!!.flush()
        }

        fun getBigNumber(value: String): BigNumber {
            val bigNumber = BigNumber(0)
            var i = 0
            while (BaseGame.prefs!!.contains("$value-$i")) {
                bigNumber.maxNumber.add(BaseGame.prefs!!.getInteger("$value-$i"))
                i++
            }
            bigNumber.maxNumber.removeIndex(0) // removes the initialized value
            return bigNumber
        }

        fun getInformationText(name: String): String {
            return when (name) {
                "Ally" -> return "An ally, straight ally, or heterosexual ally is a heterosexual and cisgender person who supports equal civil rights, gender equality, and LGBT social movements, and challenges homophobia, biphobia, and transphobia. An ally acknowledges that LGBTQ people face discrimination and thus are socially disadvantaged. They aim to use their position as heterosexual and cisgender individuals in a society focused on heteronormativity to counter discrimination against LGBTQ people."
                "Bisexual" -> return "Bisexuality is romantic or sexual attraction to both males and females."
                "Gay" -> return "Gay is a term that primarily refers to a homosexual person or the trait of being homosexual. The term was originally used to mean \"carefree\", \"cheerful\", or \"bright and showy\"."
                "Lesbian" -> return "A lesbian is a homosexual woman.\n The word lesbian is also used for women in relation to their sexual identity or sexual behavior, regardless of sexual orientation, or as an adjective to characterize or associate nouns with female homosexuality or same-sex attraction."
                "Cisgender" -> return "Cisgender (sometimes cissexual, often abbreviated to simply cis) is a term for people whose gender identity matches the sex that they were assigned at birth. For example, someone who identifies as a woman and was assigned female at birth is a cisgender woman. The term cisgender is the opposite of the word transgender."
                "Queer" -> return "Queer is an umbrella term for sexual and gender minorities who are not heterosexual or are not cisgender."
                "Transgender" -> return "Transgender people have a gender identity or gender expression that differs from their sex assigned at birth."
                "Intersex" -> return "Intersex people are individuals born with any of several variations in sex characteristics including chromosomes, gonads, sex hormones, or genitals that, according to the UN Office of the High Commissioner for Human Rights, \"do not fit the typical definitions for male or female bodies\"."
                "Pansexual" -> return "Pansexuality, or omnisexuality,is the sexual, romantic or emotional attraction towards people regardless of their sex or gender identity."
                "Asexual" -> return "Asexuality is the lack of sexual attraction to others, or low or absent interest in or desire for sexual activity."
                else -> "GameUtils: Error: Could not get information for name: $name"
            }
        }

        fun calculateAscension() {
            var quotient = 0.0
            try {
                try { // most precise
                    quotient = BaseGame.lifeTimeLove.convertBigNumberToString().toDouble() / 1_000_000_000_000_000 // 10^15
                } catch (error: ArithmeticException) { // second most precise
                    quotient = BaseGame.lifeTimeLove.divideByExponent(15).maxNumber.toString().toDouble()
                }
            } catch (error: ArithmeticException) { // not precise, but works
                quotient = (BaseGame.lifeTimeLove.maxNumber.size / 15).toDouble()
            }
            BaseGame.claimAscensionPoints = (150 * sqrt(quotient)).toLong()
        }

        fun reset() {
            BaseGame.love = BigNumber(0)
            BaseGame.revealNextGeneratorIndex = 0
            BaseGame.lifeTimeLove = BigNumber(0)
            BaseGame.revealNextGeneratorIndex = 0
            BaseGame.heartBonus = 1
            BaseGame.communityLeadersWiggleIndex = 0
            BaseGame.upgradesWiggleIndex = 0
            for (generator in BaseGame.resourceGenerators)
                generator.reset()
            BaseGame.resourceGenerators.clear()
            BaseGame.communityLeaders.clear()
            BaseGame.upgrades.clear()
            saveGameState()
        }

        private fun putBigNumber(value: String, bigNumber: BigNumber) {
            var i = 0
            while (BaseGame.prefs!!.contains("$value-$i")) { // clear value in case of contamination
                BaseGame.prefs!!.remove("$value-$i")
                i++
            }

            for (i in 0 until bigNumber.maxNumber.size) {
                BaseGame.prefs!!.putInteger("$value-$i", bigNumber.maxNumber[i])
            }
        }
    }
}
