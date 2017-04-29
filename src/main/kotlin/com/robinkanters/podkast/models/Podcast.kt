package com.robinkanters.podkast.models

import com.robinkanters.podkast.exceptions.DateFormatException
import com.robinkanters.podkast.exceptions.MalformedFeedException
import com.robinkanters.podkast.parser.CategoryParser
import com.robinkanters.podkast.utils.StringUtils.toDate
import com.robinkanters.podkast.utils.StringUtils.toURL
import org.dom4j.Document
import org.dom4j.DocumentException
import org.dom4j.DocumentHelper
import org.dom4j.Element
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class Podcast {

    var xmlData: String? = null
        private set
    private var document: Document? = null
    var feedURL: URL? = null
        private set

    private var rootElement: Element? = null
    private val channelElement: Element

    val title: String? by lazy { parseTitle() }
    val description: String? by lazy { parseDescription() }
    val language: String? by lazy { parseLanguage() }
    val copyright: String? by lazy { parseCopyright() }
    val managingEditor: String? by lazy { parseManagingEditor() }
    val webMaster: String? by lazy { parseWebMaster() }
    val pubDateString: String? by lazy { parsePubDateString() }
    val lastBuildDateString: String? by lazy { parseLastBuildDateString() }
    val generator: String? by lazy { parseGenerator() }
    val picsRating: String? by lazy { parsePicsRating() }
    val docsString: String? by lazy { parseDocsString() }
    val link: URL? by lazy { parseLink() }
    val docs: URL? by lazy { parseDocs() }
    val pubDate: Date? by lazy { parsePubDate() }
    val lastBuildDate: Date? by lazy { parseLastBuildDate() }
    val cloudInfo: CloudInfo? by lazy { parseCloud() }
    val ttl: Int? by lazy { parseTtl() }
    val skipHours: Set<Int>? by lazy { parseSkipHours() }
    val skipDays: Set<String>? by lazy { parseSkipDays() }
    val iTunesChannelInfo: ItunesChannelInfo? by lazy { parseItunesChannelInfo() }
    val episodes: List<Episode>? by lazy { parseEpisodes() }
    val categories: Array<String> by lazy { parseCategories() }
    val imageURL: URL? by lazy { parseImageUrl() }
    val textInput: TextInputInfo? by lazy { parseTextInput() }
    val keywords: Array<String> by lazy { parseKeywords() }

    @Throws(MalformedFeedException::class)
    constructor(xml: String) {
        try {
            this.xmlData = xml
            this.document = DocumentHelper.parseText(this.xmlData!!)
            this.rootElement = this.document!!.rootElement
            this.channelElement = this.rootElement!!.element("channel")
            if (this.channelElement == null) {
                throw MalformedFeedException("Missing required element 'channel'.")
            }
        } catch (e: DocumentException) {
            throw MalformedFeedException("Error parsing feed.", e)
        }

    }

    @Throws(MalformedFeedException::class)
    constructor(xml: String, feed: URL) {
        try {
            this.xmlData = xml
            this.feedURL = feed
            this.document = DocumentHelper.parseText(this.xmlData!!)
            this.rootElement = this.document!!.rootElement
            this.channelElement = this.rootElement!!.element("channel")
            if (this.channelElement == null) {
                throw MalformedFeedException("Missing required element 'channel'.")
            }
        } catch (e: DocumentException) {
            throw MalformedFeedException("Error parsing document.", e)
        }

    }

    @Throws(MalformedFeedException::class)
    private fun parseTitle(): String {
        val titleElement = this.channelElement.element("title") ?: throw MalformedFeedException("Missing required title element.")

        return titleElement.text
    }

    @Throws(MalformedFeedException::class)
    private fun parseDescription(): String {
        val descriptionElement = this.channelElement.element("description") ?: throw MalformedFeedException("Missing required description element.")

        return descriptionElement.text
    }

    @Throws(MalformedURLException::class, MalformedFeedException::class)
    private fun parseLink(): URL {
        val linkElement = this.channelElement.element("link") ?: throw MalformedFeedException("Missing required link element.")

        val url = if (linkElement.namespacePrefix.toLowerCase() == "atom") {
            linkElement.attributeValue("href")
        } else {
            linkElement.text
        }

        return url.toURL()
    }

    private fun parseLanguage() = this.channelElement.element("language")?.text

    private fun parseCopyright() = this.channelElement.element("copyright")?.text

    private fun parseManagingEditor() = this.channelElement.element("managingEditor")?.text

    private fun parseWebMaster() = this.channelElement.element("webMaster")?.text

    @Throws(DateFormatException::class)
    private fun parsePubDate() = pubDateString?.trim()?.toDate()

    private fun parsePubDateString() = this.channelElement.element("pubDate")?.text

    @Throws(DateFormatException::class)
    private fun parseLastBuildDate() = lastBuildDateString?.toDate()

    private fun parseLastBuildDateString() = this.channelElement.element("lastBuildDate")?.text

    private fun parseCategories() = CategoryParser().parse(channelElement)

    private fun parseGenerator() = channelElement.element("generator")?.text

    @Throws(MalformedURLException::class)
    private fun parseDocs(): URL? {
        val docsString = this.docsString ?: return null

        return docsString.toURL()
    }

    private fun parseDocsString(): String? {
        val docsElement = this.channelElement.element("docs") ?: return null

        return docsElement.text
    }

    private fun parseCloud(): CloudInfo? {
        val cloudElement = this.channelElement.element("cloud") ?: return null

        return CloudInfo(cloudElement)
    }

    private fun parseTtl(): Int? {
        val ttlElement = channelElement.element("ttl") ?: return null

        try {
            return Integer.valueOf(ttlElement.textTrim)
        } catch (e: NumberFormatException) {
            return null
        }
    }

    @Throws(MalformedURLException::class)
    private fun parseImageUrl(): URL? {
        val thumbnailElement = channelElement.element("thumbnail")
        if (thumbnailElement != null)
            return thumbnailElement.attributeValue("url").toURL()

        channelElement.elements("image")
                .map { it as Element }
                .mapNotNull { parseImageUrlString(it) }
                .first { return it.toURL() }

        return null
    }

    private fun parseImageUrlString(image: Element): String? {
        if (image.namespacePrefix.toLowerCase() == "itunes") {
            return image.attributeValue("href")
        } else if (image.element("url") != null) {
            return image.element("url").text
        }

        return null
    }

    private fun parsePicsRating() = channelElement.element("rating")?.text


    private fun parseTextInput(): TextInputInfo? {
        val textInputElement = channelElement.element("textInput") ?: return null

        return TextInputInfo(textInputElement)
    }

    @Throws(MalformedFeedException::class)
    private fun parseSkipHours(): Set<Int> {
        val hourElements = channelElement.element("skipHours")?.elements("hour") ?: return emptySet()

        return hourElements.asSequence()
                .filter { it is Element }
                .map { parseHour(it as Element) }
                .toSet()
    }

    private fun parseHour(hourObject: Element): Int {
        val hour: Int
        try {
            hour = Integer.valueOf(hourObject.textTrim)!!
        } catch (e: NumberFormatException) {
            throw MalformedFeedException("Invalid hour in skipHours element.")
        }

        if (hour in 0..23) {
            return hour
        }

        throw MalformedFeedException("Hour in skipHours element is outside of valid range 0 - 23")
    }

    @Throws(MalformedFeedException::class)
    private fun parseSkipDays(): Set<String>? {
        val dayElements = channelElement.element("skipDays")?.elements("day") ?: return emptySet()
        if (dayElements.size > 7) {
            throw MalformedFeedException("More than 7 day elements present within skipDays element.")
        }

        val validDays = arrayOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")

        return dayElements
                .asSequence()
                .filter { it is Element }
                .map { it as Element }
                .map { it.textTrim }
                .filter { validDays.any { v -> v == it.toLowerCase() } }
                .toHashSet()
    }

    private fun parseKeywords(): Array<String> {
        return channelElement.elements("keywords")
                .map { it as Element }
                .flatMap { it.text.split(",") }
                .map(String::trim)
                .filter(String::isNotEmpty)
                .toTypedArray()
    }

    private fun parseEpisodes(): List<Episode>? {
        return channelElement.elements("item")
                .filterIsInstance<Element>()
                .map(::Episode)
    }

    private fun parseItunesChannelInfo() = ItunesChannelInfo(channelElement)
}

