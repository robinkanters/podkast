package com.robinkanters.podengine.models

import org.dom4j.Element
import org.dom4j.QName

class ItunesOwner(private val ownerElement: Element) {
    val name: String? by lazy { parseName() }
    val email: String? by lazy { parseEmail() }

    private fun parseName() = ownerElement.element(QName.get("name", "itunes"))?.text

    private fun parseEmail() = ownerElement.element(QName.get("email", "itunes"))?.text
}
