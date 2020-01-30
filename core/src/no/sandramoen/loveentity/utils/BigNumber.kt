package no.sandramoen.loveentity.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Array

/*
* Warning:  Only supports natural numbers (including 0s).
*           Does not work with whole numbers, integers, rational numbers, irrational numbers or real numbers
*
*           Input of BigNumber initialization and functions currently only supports a long,
*           which is 999_999_999_999_999_999.
* */
class BigNumber(number: Long) {
    var maxNumber: Array<Int> = Array()

    init {
        maxNumber = convertNumberToArray(number)
    }

    fun presentLongScale(): String {
        removeLeadingZeroes()
        var number = ""
        when {
            maxNumber.size >= 600 + 1 -> number = labelBigNumber("centillion", 600)
            maxNumber.size >= 120 + 1 -> number = labelBigNumber("vigintillion", 120)
            maxNumber.size >= 114 + 1 -> number = labelBigNumber("novemdecillion", 114)
            maxNumber.size >= 108 + 1 -> number = labelBigNumber("octodecillion", 108)
            maxNumber.size >= 102 + 1 -> number = labelBigNumber("septendecillion", 102)
            maxNumber.size >= 96 + 1 -> number = labelBigNumber("sexdecillion", 96)
            maxNumber.size >= 90 + 1 -> number = labelBigNumber("quindecillion", 90)
            maxNumber.size >= 84 + 1 -> number = labelBigNumber("quattuordecillion", 84)
            maxNumber.size >= 78 + 1 -> number = labelBigNumber("tredecillion", 78)
            maxNumber.size >= 72 + 1 -> number = labelBigNumber("duodecillion", 72)
            maxNumber.size >= 66 + 1 -> number = labelBigNumber("undecillion", 66)
            maxNumber.size >= 60 + 1 -> number = labelBigNumber("decillion", 60)
            maxNumber.size >= 54 + 1 -> number = labelBigNumber("nonillion", 54)
            maxNumber.size >= 48 + 1 -> number = labelBigNumber("octillion", 48)
            maxNumber.size >= 42 + 1 -> number = labelBigNumber("septillion", 42)
            maxNumber.size >= 36 + 1 -> number = labelBigNumber("sextillion", 36)
            maxNumber.size >= 30 + 1 -> number = labelBigNumber("quintillion", 30)
            maxNumber.size >= 24 + 1 -> number = labelBigNumber("quadrillion", 24)
            maxNumber.size >= 18 + 1 -> number = labelBigNumber("trillion", 18)
            maxNumber.size >= 15 + 1 -> number = labelBigNumber("billiard", 15)
            maxNumber.size >= 12 + 1 -> number = labelBigNumber("billion", 12)
            maxNumber.size >= 9 + 1 -> number = labelBigNumber("milliard", 9)
            /*maxNumber.size >= 6 + 1 -> number = labelBigNumber("million", 6)*/
            else -> {
                for (i in maxNumber.size - 1 downTo 0) {
                    if (i % 3 == 0 && i > 0)
                        number += "${maxNumber[maxNumber.size - (i + 1)]}," // 9 - 8 = 1
                    else
                        number += "${maxNumber[maxNumber.size - (i + 1)]}"
                }
            }
        }
        return number
    }

    fun presentShortScale(): String {
        removeLeadingZeroes()
        var number = ""
        when {
            maxNumber.size >= 303 + 1 -> number = labelBigNumber("centillion", 303)
            maxNumber.size >= 63 + 1 -> number = labelBigNumber("vigintillion", 63)
            maxNumber.size >= 60 + 1 -> number = labelBigNumber("novemdecillion", 60)
            maxNumber.size >= 57 + 1 -> number = labelBigNumber("octodecillion", 57)
            maxNumber.size >= 54 + 1 -> number = labelBigNumber("septendecillion", 54)
            maxNumber.size >= 51 + 1 -> number = labelBigNumber("sexdecillion", 51)
            maxNumber.size >= 48 + 1 -> number = labelBigNumber("quindecillion", 48)
            maxNumber.size >= 45 + 1 -> number = labelBigNumber("quattuordecillion", 45)
            maxNumber.size >= 42 + 1 -> number = labelBigNumber("tredecillion", 42)
            maxNumber.size >= 39 + 1 -> number = labelBigNumber("duodecillion", 39)
            maxNumber.size >= 36 + 1 -> number = labelBigNumber("undecillion", 36)
            maxNumber.size >= 33 + 1 -> number = labelBigNumber("decillion", 33)
            maxNumber.size >= 30 + 1 -> number = labelBigNumber("nonillion", 30)
            maxNumber.size >= 27 + 1 -> number = labelBigNumber("octillion", 27)
            maxNumber.size >= 24 + 1 -> number = labelBigNumber("septillion", 24)
            maxNumber.size >= 21 + 1 -> number = labelBigNumber("sextillion", 21)
            maxNumber.size >= 18 + 1 -> number = labelBigNumber("quintillion", 18)
            maxNumber.size >= 15 + 1 -> number = labelBigNumber("quadrillion", 15)
            maxNumber.size >= 12 + 1 -> number = labelBigNumber("trillion", 12)
            maxNumber.size >= 9 + 1 -> number = labelBigNumber("billion", 9)
            /*maxNumber.size >= 6 + 1 -> number = labelBigNumber("million", 6)*/
            else -> {
                for (i in maxNumber.size - 1 downTo 0) {
                    if (i % 3 == 0 && i > 0)
                        number += "${maxNumber[maxNumber.size - (i + 1)]}," // 9 - 8 = 1
                    else
                        number += "${maxNumber[maxNumber.size - (i + 1)]}"
                }
            }
        }
        return number
    }

    fun add(augend: BigNumber, addend: BigNumber): BigNumber {
        // make sure both BigNumbers are of equal size
        if (augend.size() < addend.size()) {
            for (i in 0 until addend.size() - augend.size())
                augend.maxNumber.insert(0, 0)
        } else if (augend.size() > addend.size()) {
            for (i in 0 until augend.size() - addend.size())
                addend.maxNumber.insert(0, 0)
        }

        // initialize empty sum array
        val sum = BigNumber(0)
        for (i in 0 until augend.size() - 1)
            sum.maxNumber.add(0)

        // perform the addition
        for (i in 0 until augend.maxNumber.size)
            sum.maxNumber[i] = augend.maxNumber[i]
        for (i in 0 until addend.maxNumber.size)
            sum.maxNumber[i] += addend.maxNumber[i]

        // carry over values that exceed 9
        for (i in sum.size() - 1 downTo 0) {
            if (sum.maxNumber[i] > 9) {
                sum.maxNumber[i] -= 10
                if (i > 0)
                    sum.maxNumber[i - 1] += 1
                else
                    sum.maxNumber.insert(0, 1) // the carry over should create a new highest place
            }
        }

        /*println("Adding --- \n ${augend.maxNumber} \n ${addend.maxNumber}")
        println(" ${sum.maxNumber} \n ---")*/
        return sum
    }

    /*
    * Warning: This algorithm assumes minuend >= subtrahend
    * */
    fun subtract(minuend: BigNumber, subtrahend: BigNumber): BigNumber {
        // make sure both BigNumbers are of equal size
        if (minuend.size() < subtrahend.size()) {
            for (i in 0 until subtrahend.size() - minuend.size())
                minuend.maxNumber.insert(0, 0)
        } else if (minuend.size() > subtrahend.size()) {
            for (i in 0 until minuend.size() - subtrahend.size())
                subtrahend.maxNumber.insert(0, 0)
        }

        // initialize empty difference array
        var difference = BigNumber(0)
        for (number in minuend.maxNumber)
            difference.maxNumber.add(number)
        difference.maxNumber.removeIndex(0)

        for (i in (difference.size() - 1) downTo 0) {
            if (i > 0 && (difference.maxNumber[i] < subtrahend.maxNumber[i])) { // if we can borrow
                difference.maxNumber[i - 1] -= 1 // borrow from the next higher digit
                difference.maxNumber[i] += 10
                difference.maxNumber[i] -= subtrahend.maxNumber[i]
            } else if (difference.maxNumber[i] >= subtrahend.maxNumber[i]) {
                difference.maxNumber[i] -= subtrahend.maxNumber[i]
            }
        }

        difference.removeLeadingZeroes()

        if (!minuend.isGreaterThanOrEqualTo(subtrahend)) {
            Gdx.app.log("BigNumber", "BigNumber: Error: subtrahend is greater than the minuend!")
            Gdx.app.log("BigNumber", "BigNumber: Subtracting --- \n ${minuend.maxNumber} \n ${subtrahend.maxNumber} \n ${difference.maxNumber} \n ---")

        }
        // Gdx.app.log("BigNumber", "BigNumber: Subtracting --- \n ${minuend.maxNumber} \n ${subtrahend.maxNumber} \n ${difference.maxNumber} \n ---")
        return difference
    }

    /*
    * Warning:  Becomes more and more imprecise
    *           This algorithm assumes dividend >= divisor
    * */
    fun divide(dividend: BigNumber, divisor: BigNumber): BigNumber {
        var quotient = BigNumber(0)
        try {
            try { // most precise
                quotient = BigNumber((dividend.convertBigNumberToString().toDouble() / divisor.convertBigNumberToString().toDouble()).toLong())
            } catch (error: ArithmeticException) { // second most precise
                quotient = BigNumber(dividend.divideByExponent(divisor.maxNumber.size.toLong()).maxNumber.toString().toDouble().toLong())
            }
        } catch (error: ArithmeticException) { // not precise, but works
            quotient = BigNumber(dividend.maxNumber.size / divisor.maxNumber.size.toLong())
        }
        return quotient
    }

    fun isGreaterThanOrEqualTo(number: BigNumber): Boolean {
        // make sure both BigNumbers are of equal size
        if (maxNumber.size < number.size()) {
            for (i in 0 until number.size() - maxNumber.size)
                maxNumber.insert(0, 0)
        } else if (maxNumber.size > number.size()) {
            for (i in 0 until maxNumber.size - number.size())
                number.maxNumber.insert(0, 0)
        }

        // simple array size check
        if (maxNumber.size > number.size())
            return true
        if (maxNumber.size < number.size())
            return false

        // if both numbers are of equal size
        for (i in 0 until maxNumber.size) { // checks highest digit first
            if (maxNumber[i] > number.maxNumber[i])
                return true
            if (maxNumber[i] < number.maxNumber[i])
                return false
        }
        return true // numbers are of equal size
    }

    fun divideByExponent(number: Long): BigNumber {
        for (i in 0 until number.toString().length - 1)
            maxNumber.removeIndex(maxNumber.size - 1)
        return this
    }

    fun convertBigNumberToString(): String {
        removeLeadingZeroes()
        var number = ""
        for (i in 0 until maxNumber.size)
            number += maxNumber[i]
        return number
    }

    private fun labelBigNumber(name: String, current: Int): String {
        var number = ""

        // integers
        for (i in current until maxNumber.size)
            number += maxNumber[i - current]

        // decimals
        when {
            maxNumber[maxNumber.size - current + 2] > 0 -> number += ".${maxNumber[maxNumber.size - current + 0]}${maxNumber[maxNumber.size - current + 1]}${maxNumber[maxNumber.size - current + 2]}"
            maxNumber[maxNumber.size - current + 1] > 0 -> number += ".${maxNumber[maxNumber.size - current + 0]}${maxNumber[maxNumber.size - current + 1]}"
            maxNumber[maxNumber.size - current + 0] > 0 -> number += ".${maxNumber[maxNumber.size - current + 0]}"
        }

        // add name
        number += " $name"
        return number
    }

    private fun size() = maxNumber.size

    private fun convertNumberToArray(number: Long): Array<Int> {
        // Gdx.app.error("BigNumber", "$number")
        val numberString: String = number.toString()
        /*if (numberString[0].toString() == "-") { // TODO: totally imprecise
            if (maxNumber.size == 0) {
                val temp  = Array<Int>()
                for (i in 0..999999999999999999) // when $number is more than 1 trillion, just add a trillion
                    temp.add(0)
                maxNumber = temp
            } else {
                for (i in 0..999999999999999999) // when $number is more than 1 trillion, just add a trillion
                    maxNumber.add(0)
            }
            return maxNumber
        }*/
        val converted = Array<Int>(numberString.length)
        for (element in numberString) {
            converted.add(element.toString().toInt())
        }
        return converted  // highest tens comes first, e.g. 10 => [1, 0]
    }

    private fun removeLeadingZeroes() {
        val indexesThatShouldBeRemoved: Array<Int> = Array()
        for (i in 0 until maxNumber.size) { // count leading zeros
            if (maxNumber[i] == 0) indexesThatShouldBeRemoved.add(i)
            else break
        }
        for (i in 0 until indexesThatShouldBeRemoved.size)
            if (maxNumber.size > 1) // if maxNumber = [0], don't remove it
                maxNumber.removeIndex(0)
    }
}