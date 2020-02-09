package no.sandramoen.loveentity.utils

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import java.math.BigInteger
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
            BaseGame.prefs!!.putBoolean("wiggleAscension", BaseGame.wiggleAscension)
            BaseGame.prefs!!.putBoolean("english", BaseGame.english)
            BaseGame.prefs!!.putBoolean("longScale", BaseGame.longScale)
            BaseGame.prefs!!.flush()
        }

        fun getBigNumber(value: String): BigInteger {
            if (BaseGame.prefs!!.getString(value) == "0" || BaseGame.prefs!!.getString(value) == "")
                return BigInteger.ZERO
            return BigInteger(BaseGame.prefs!!.getString(value))
        }

        fun getInformationText(name: String): String {
            return when (name) {
                "Ally" -> {
                    if (BaseGame.english)
                        return "An ally, straight ally, or heterosexual ally is a heterosexual and cisgender person who supports equal civil rights, gender equality, and LGBT social movements, and challenges homophobia, biphobia, and transphobia. An ally acknowledges that LGBTQ people face discrimination and thus are socially disadvantaged. They aim to use their position as heterosexual and cisgender individuals in a society focused on heteronormativity to counter discrimination against LGBTQ people."
                    else
                        return "En alliert er en heteroseksuell og ciskjønnet person som støtter likeverd, likestilling og rettigheter til LHBTQ folk. De utfordrer homofobi, bifobi og transfobi. En alliert erkjenner at LHBTQ folk møter diskriminering og er sosialt vanskeligstilte. Deres mål er å bruke sin posisjon som heterofile og ciskjønnede individer i et samfunn med fokus på heteronormativitet for å motvirke diskriminering av LHBTQ folk."
                }
                "Bisexual" -> {
                    if (BaseGame.english)
                        return "Bisexuality is romantic or sexual attraction to both males and females."
                    else
                        return "Bifili er en romantisk og/eller seksuell legning der en blir tiltrukket av flere enn ett kjønn, både samme og motsatt kjønn. Bifili betegnes ofte som en seksuell orientering og har ofte overlappende, men ikke identisk betydning med panfili."
                }
                "Gay" -> {
                    if (BaseGame.english)
                        return "Gay is a term that primarily refers to a homosexual person or the trait of being homosexual. The term was originally used to mean \"carefree\", \"cheerful\", or \"bright and showy\"."
                    else
                        return "Homofili er en seksuell orientering karakterisert av romantisk og/eller seksuell tiltrekning til personer av ens eget kjønn. Homofili og heterofili er relative begreper på hver side av en kontinuerlig skala, med bifili et sted på midten."
                }
                "Lesbian" -> {
                    if (BaseGame.english)
                        return "A lesbian is a homosexual woman.\n The word lesbian is also used for women in relation to their sexual identity or sexual behavior, regardless of sexual orientation, or as an adjective to characterize or associate nouns with female homosexuality or same-sex attraction."
                    else
                        return "Lesbisk er en betegnelse som brukes for å beskrive homoseksuelle kvinner som tiltrekkes av, blir forelsket i, og/eller har sex med andre kvinner."
                }
                "Cisgender" -> {
                    if (BaseGame.english)
                        return "Cisgender (sometimes cissexual, often abbreviated to simply cis) is a term for people whose gender identity matches the sex that they were assigned at birth. For example, someone who identifies as a woman and was assigned female at birth is a cisgender woman. The term cisgender is the opposite of the word transgender."
                    else
                        return "Ciskjønnethet er det motsatte av transkjønnethet og majoriteten identifiserer seg selv som ciskjønnet. Det vil altså si at en ciskjønnet person identifiserer seg med det kjønnet vedkommende ble tilegnet ved fødsel med."
                }
                "Queer" -> {
                    if (BaseGame.english)
                        return "Queer is an umbrella term for sexual and gender minorities who are not heterosexual or are not cisgender."
                    else
                        return "Skeiv er et paraplybegrep for seksuelle og kjønnsminoriteter som ikke er heteroseksuelle eller ikke er ciskjønnet"
                }
                "Transgender" -> {
                    if (BaseGame.english)
                        return "Transgender people have a gender identity or gender expression that differs from their sex assigned at birth."
                    else
                        "Transkjønnet betyr at det er manglende samsvar med det kjønnet man er tildelt ved fødselen og selvopplevd kjønn."
                }
                "Intersex" -> {
                    if (BaseGame.english)
                        return "Intersex people are individuals born with any of several variations in sex characteristics including chromosomes, gonads, sex hormones, or genitals that, according to the UN Office of the High Commissioner for Human Rights, \"do not fit the typical definitions for male or female bodies\"."
                    else
                        return "Interkjønnede mennesker er individer født med noen av flere variasjoner i kjønnskarakteristika, inkludert kromosomer, gonader, kjønnshormoner eller kjønnsorganer, som ifølge FNs kontor for høykommissær for menneskerettigheter, \"ikke passer til de typiske definisjonene for mannlige eller kvinnelige kropper."
                }
                "Pansexual" -> {
                    if (BaseGame.english)
                        return "Pansexuality is the sexual, romantic or emotional attraction towards people regardless of their sex or gender identity."
                    else
                        return "Panseksualitet er å tiltrekkes følelsesmessig og/eller seksuelt av en person uavhengig av vedkommendes biologiske kjønn eller vedkommendes kjønnsidentitet."
                }
                "Asexual" -> {
                    if (BaseGame.english)
                        return "Asexuality is the lack of sexual attraction to others, or low or absent interest in or desire for sexual activity."
                    else
                        return "Aseksualitet er mangel på seksuell tiltrekning til andre, eller lav eller mangelfull interesse eller ønske for seksuell aktivitet"
                }
                else -> "GameUtils: Error: Could not get information for name: $name"
            }
        }

        fun calculateAscension() {
            val quotient = BaseGame.lifeTimeLove.divide(BigInteger("1000000000000000")).toDouble()
            BaseGame.claimAscensionPoints = 150 * sqrt(quotient).toLong()
        }

        fun reset() {
            BaseGame.love = BigInteger("0")
            BaseGame.lifeTimeLove = BigInteger("0")
            BaseGame.revealNextGeneratorIndex = 0
            BaseGame.heartBonus = 1
            BaseGame.communityLeadersWiggleIndex = 0
            BaseGame.upgradesWiggleIndex = 0
            for (generator in BaseGame.resourceGenerators)
                generator.reset()
            BaseGame.resourceGenerators.clear()
            BaseGame.communityLeaders.clear()
            BaseGame.upgrades.clear()
            BaseGame.wiggleAscension = true
            BaseGame.english = true
            BaseGame.longScale = true
            saveGameState()
        }

        fun putBigNumber(value: String, bigNumber: BigInteger) {
            BaseGame.prefs!!.putString("$value", bigNumber.toString())
        }

        fun presentLongScale(number: BigInteger): String {
            var label = ""
            when {
                number.toString().length >= 600 + 1 -> label = labelBigNumber(number.toString(), "centillion", 600)
                number.toString().length >= 120 + 1 -> label = labelBigNumber(number.toString(), "vigintillion", 120)
                number.toString().length >= 114 + 1 -> label = labelBigNumber(number.toString(), "novemdecillion", 114)
                number.toString().length >= 108 + 1 -> label = labelBigNumber(number.toString(), "octodecillion", 108)
                number.toString().length >= 102 + 1 -> label = labelBigNumber(number.toString(), "septendecillion", 102)
                number.toString().length >= 96 + 1 -> label = labelBigNumber(number.toString(), "sexdecillion", 96)
                number.toString().length >= 90 + 1 -> label = labelBigNumber(number.toString(), "quindecillion", 90)
                number.toString().length >= 84 + 1 -> label = labelBigNumber(number.toString(), "quattuordecillion", 84)
                number.toString().length >= 78 + 1 -> label = labelBigNumber(number.toString(), "tredecillion", 78)
                number.toString().length >= 72 + 1 -> label = labelBigNumber(number.toString(), "duodecillion", 72)
                number.toString().length >= 66 + 1 -> label = labelBigNumber(number.toString(), "undecillion", 66)
                number.toString().length >= 60 + 1 -> label = labelBigNumber(number.toString(), "decillion", 60)
                number.toString().length >= 54 + 1 -> label = labelBigNumber(number.toString(), "nonillion", 54)
                number.toString().length >= 48 + 1 -> label = labelBigNumber(number.toString(), "octillion", 48)
                number.toString().length >= 42 + 1 -> label = labelBigNumber(number.toString(), "septillion", 42)
                number.toString().length >= 36 + 1 -> label = labelBigNumber(number.toString(), "sextillion", 36)
                number.toString().length >= 30 + 1 -> label = labelBigNumber(number.toString(), "quintillion", 30)
                number.toString().length >= 24 + 1 -> label = labelBigNumber(number.toString(), "quadrillion", 24)
                number.toString().length >= 18 + 1 -> label = labelBigNumber(number.toString(), "trillion", 18)
                number.toString().length >= 15 + 1 -> label = labelBigNumber(number.toString(), "billiard", 15)
                number.toString().length >= 12 + 1 -> label = labelBigNumber(number.toString(), "billion", 12)
                number.toString().length >= 9 + 1 -> label = labelBigNumber(number.toString(), "milliard", 9)
                // number.toString().length >= 6 + 1 -> label = labelBigNumber(number.toString(), "million", 6)
                else -> {
                    val temp = number.toString()
                    for (i in temp.length - 1 downTo 0) {
                        if (i % 3 == 0 && i > 0)
                            label += "${temp[temp.length - (i + 1)]}," // 9 - 8 = 1
                        else
                            label += "${temp[temp.length - (i + 1)]}"
                    }
                }
            }
            return label
        }

        fun presentShortScale(number: BigInteger): String {
            var label = ""
            when {
                number.toString().length >= 303 + 1 -> label = labelBigNumber(number.toString(), "centillion", 303)
                number.toString().length >= 63 + 1 -> label = labelBigNumber(number.toString(), "vigintillion", 63)
                number.toString().length >= 60 + 1 -> label = labelBigNumber(number.toString(), "novemdecillion", 60)
                number.toString().length >= 57 + 1 -> label = labelBigNumber(number.toString(), "octodecillion", 57)
                number.toString().length >= 54 + 1 -> label = labelBigNumber(number.toString(), "septendecillion", 54)
                number.toString().length >= 51 + 1 -> label = labelBigNumber(number.toString(), "sexdecillion", 51)
                number.toString().length >= 48 + 1 -> label = labelBigNumber(number.toString(), "quindecillion", 48)
                number.toString().length >= 45 + 1 -> label = labelBigNumber(number.toString(), "quattuordecillion", 45)
                number.toString().length >= 42 + 1 -> label = labelBigNumber(number.toString(), "tredecillion", 42)
                number.toString().length >= 39 + 1 -> label = labelBigNumber(number.toString(), "duodecillion", 39)
                number.toString().length >= 36 + 1 -> label = labelBigNumber(number.toString(), "undecillion", 36)
                number.toString().length >= 33 + 1 -> label = labelBigNumber(number.toString(), "decillion", 33)
                number.toString().length >= 30 + 1 -> label = labelBigNumber(number.toString(), "nonillion", 30)
                number.toString().length >= 27 + 1 -> label = labelBigNumber(number.toString(), "octillion", 27)
                number.toString().length >= 24 + 1 -> label = labelBigNumber(number.toString(), "septillion", 24)
                number.toString().length >= 21 + 1 -> label = labelBigNumber(number.toString(), "sextillion", 21)
                number.toString().length >= 18 + 1 -> label = labelBigNumber(number.toString(), "quintillion", 18)
                number.toString().length >= 15 + 1 -> label = labelBigNumber(number.toString(), "quadrillion", 15)
                number.toString().length >= 12 + 1 -> label = labelBigNumber(number.toString(), "trillion", 12)
                number.toString().length >= 9 + 1 -> label = labelBigNumber(number.toString(), "billion", 9)
                // number.toString().length >= 6 + 1 -> label = labelBigNumber(number.toString(), "million", 6)
                else -> {
                    val temp = number.toString()
                    for (i in temp.length - 1 downTo 0) {
                        if (i % 3 == 0 && i > 0)
                            label += "${temp[temp.length - (i + 1)]}," // 9 - 8 = 1
                        else
                            label += "${temp[temp.length - (i + 1)]}"
                    }
                }
            }
            return label
        }

        private fun labelBigNumber(number: String, name: String, current: Int): String {
            var label = ""

            // integers
            for (i in current until number.length)
                label += number[i - current]

            // decimals
            when {
                number[number.length - current + 2].toString().toInt() > 0 ->
                    label += ".${number[number.length - current + 0]}${number[number.length - current + 1]}${number[number.length - current + 2]}"
                number[number.length - current + 1].toString().toInt() > 0 ->
                    label += ".${number[number.length - current + 0]}${number[number.length - current + 1]}"
                number[number.length - current + 0].toString().toInt() > 0 ->
                    label += ".${number[number.length - current + 0]}"
            }

            // add name
            if (!BaseGame.english && (number[0].toString().toInt() > 1 || label.length > 1 && label[1].toString() != " ")) // norwegian translation
                label += " $name" + "er"
            else
                label += " $name"
            return label
        }
    }
}
