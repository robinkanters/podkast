package com.robinkanters.podengine.models

import com.robinkanters.podengine.exceptions.DateFormatException
import com.robinkanters.podengine.exceptions.MalformedFeedException
import com.robinkanters.podengine.utils.StringUtils.toDate
import com.robinkanters.podengine.utils.StringUtils.toURL
import org.dom4j.Element
import org.dom4j.QName
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class Episode(private val itemElement: Element) {
    val title: String? by lazy { parseTitle() }
    val link: URL? by lazy { parseLink() }
    val description: String by lazy { parseDescription() }
    val author: String? by lazy { parseAuthor() }
    val categories: Set<String> by lazy { parseCategories() }
    val comments: URL? by lazy { parseComments() }
    val enclosure: Enclosure? by lazy { parseEnclosure() }
    val uuid: UUID? by lazy { parseUuid() }
    val pubDate: Date? by lazy { parsePubDate() }
    val sourceName: String? by lazy { parseSourceName() }
    val sourceUrl: URL? by lazy { parseSourceUrl() }
    val iTunesInfo: ItunesItemInfo? by lazy { parseItunesItemInfo() }
    val contentEncoded: String? by lazy { parseContentEncoded() }

    @Throws(MalformedFeedException::class)
    private fun parseTitle(): String {
        val titleElement = itemElement.element("title") ?: throw MalformedFeedException("Item is missing required element title.")

        return titleElement.text
    }

    @Throws(MalformedURLException::class)
    private fun parseLink(): URL? {
        val linkElement = itemElement.element("link") ?: return null

        if (linkElement.namespacePrefix.toLowerCase() == "atom") {
            return linkElement.attributeValue("href").toURL()
        }

        return linkElement.text.toURL()
    }

    @Throws(MalformedFeedException::class)
    private fun parseDescription(): String {
        val descriptionElement = itemElement.element("description") ?: throw MalformedFeedException("Item is missing required element description.")

        return descriptionElement.text
    }

    private fun parseAuthor() = itemElement.element("author")?.text

    private fun parseCategories(): Set<String> {
        return itemElement.elements("category")
                .map { it as Element }
                .map { it.textTrim }
                .toSet()
    }

    @Throws(MalformedURLException::class)
    private fun parseComments() = itemElement.element("comments").textTrim?.toURL()

    private fun parseEnclosure(): Enclosure? {
        val enclosureElement = itemElement.element("enclosure") ?: return null

        return Enclosure(enclosureElement)
    }

    private fun parseUuid(): UUID? {
        val guidElement = itemElement.element("uuid") ?: return null

        return UUID.fromString(guidElement.textTrim)
    }

    @Throws(DateFormatException::class)
    private fun parsePubDate() = itemElement.element("pubDate")?.textTrim!!.toDate()

    private fun parseSourceName() = itemElement.element("source")?.text

    @Throws(MalformedFeedException::class, MalformedURLException::class)
    private fun parseSourceUrl(): URL? {
        val sourceElement = itemElement.element("source") ?: return null
        val urlAttribute = sourceElement.attribute("url") ?: throw MalformedFeedException("Missing required attribute URL for element Source.")

        return urlAttribute.text.toURL()
    }

    private fun parseItunesItemInfo() = ItunesItemInfo(itemElement)

    private fun parseContentEncoded(): String? {
        val namespace = itemElement.getNamespaceForPrefix("content")

        return itemElement.element(QName.get("encoded", namespace))?.text
    }
}
