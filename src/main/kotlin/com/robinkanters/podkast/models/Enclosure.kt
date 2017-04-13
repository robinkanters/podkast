package com.robinkanters.podkast.models

import com.robinkanters.podkast.exceptions.MalformedFeedException
import com.robinkanters.podkast.utils.ElementUtils.getAttributeValueOrThrow
import com.robinkanters.podkast.utils.StringUtils.toURL
import org.dom4j.Element
import java.net.MalformedURLException
import java.net.URL

class Enclosure(private val enclosureElement: Element) {
    val url: URL by lazy { parseURL() }
    val length: Long by lazy { parseLength() }
    val mimeType: String by lazy { parseMimeType() }

    private fun parseURL(): URL {
        try {
            return enclosureElement.getAttributeValueOrThrow("url").toURL()
        } catch (e: MalformedURLException) {
            throw MalformedFeedException("Invalid value for URL attribute for element Enclosure.")
        }
    }

    private fun parseLength(): Long {
        return enclosureElement.getAttributeValueOrThrow("length").toLong()
    }

    private fun parseMimeType(): String {
        return enclosureElement.getAttributeValueOrThrow("type")
    }
}
