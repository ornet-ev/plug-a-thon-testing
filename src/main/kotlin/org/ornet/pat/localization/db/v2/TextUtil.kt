package org.ornet.pat.localization.db.v2

import org.somda.dsl.biceps.base.LocalizedTextWidth
import java.text.BreakIterator
import java.util.*

fun textWidthForText(language: String, text: String): LocalizedTextWidth {
    val locale = Locale.forLanguageTag(language)
    val breakIterator = BreakIterator.getCharacterInstance(locale).apply { setText(text) }
    var boundaryIndex = breakIterator.first()
    var graphemeCount = 0
    while (boundaryIndex != BreakIterator.DONE) {
        graphemeCount++
        boundaryIndex = breakIterator.next()
    }

    return when {
        graphemeCount <= 4 -> LocalizedTextWidth.XS
        graphemeCount <= 8 -> LocalizedTextWidth.S
        graphemeCount <= 12 -> LocalizedTextWidth.M
        graphemeCount <= 16 -> LocalizedTextWidth.L
        graphemeCount <= 20 -> LocalizedTextWidth.XL
        else -> LocalizedTextWidth.XXL
    }
}

fun LocalizedTextWidth.serialize(): String {
    return when (this) {
        LocalizedTextWidth.XS -> "xs"
        LocalizedTextWidth.S -> "s"
        LocalizedTextWidth.M -> "m"
        LocalizedTextWidth.L -> "l"
        LocalizedTextWidth.XL -> "xl"
        LocalizedTextWidth.XXL -> "xxl"
    }
}

fun lineCount(text: String): Int {
    return text.split("\n").size
}