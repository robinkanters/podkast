package com.robinkanters.podkast.models

import org.dom4j.Element
import org.dom4j.QName

class ItunesItemInfo(parent: Element) : ItunesInfo(parent) {
    val duration: String? by lazy { parseDuration() }
    val isClosedCaptioned: Boolean? by lazy { parseIsClosedCaptioned() }
    val order: Int? by lazy { parseOrder() }

    private fun parseDuration(): String? {
        val durationElement = parent.element(QName.get("duration", itunesNamespace))

        return durationElement?.text
    }

    private fun parseIsClosedCaptioned(): Boolean {
        val isClosedCaptionedElement = parent.element(QName.get("isClosedCaptioned", itunesNamespace))

        return isClosedCaptionedElement?.textTrim?.toLowerCase() == "yes"
    }

    private fun parseOrder(): Int? {
        val orderElement = parent.element(QName.get("order", itunesNamespace))

        try {
            return orderElement?.textTrim?.toInt()
        } catch (e: NumberFormatException) {
            return null
        }
    }
}
