package com.robinkanters.podengine.models

import com.robinkanters.podengine.models.ExplicitLevel.*
import org.dom4j.Element
import org.dom4j.Namespace
import org.dom4j.QName

import java.net.MalformedURLException
import java.net.URL
import com.robinkanters.podengine.utils.StringUtils.toURL

abstract class ItunesInfo(protected val parent: Element) {
    protected val itunesNamespace: Namespace = parent.getNamespaceForPrefix("itunes")

    val author: String? by lazy { parseAuthor() }
    val subtitle: String? by lazy { parseSubtitle() }
    val summary: String? by lazy { parseSummary() }
    val imageString: String? by lazy { parseImageString() }
    val explicit: ExplicitLevel by lazy { parseExplicit() }
    val isBlocked: Boolean by lazy { parseIsBlocked() }
    val image: URL? by lazy { parseImage() }

    private fun parseAuthor() = parent.element(QName.get("author", itunesNamespace))?.text

    private fun parseSubtitle() = parent.element(QName.get("subtitle", itunesNamespace))?.text

    private fun parseSummary() = parent.element(QName.get("summary", itunesNamespace))?.text

    private fun parseImageString() = parent.element(QName.get("image", itunesNamespace))?.attributeValue("href")

    private fun parseExplicit(): ExplicitLevel {
        val explicitText = parent.element(QName.get("explicit", itunesNamespace)).textTrim?.toLowerCase()

        return when (explicitText) {
            "yes" -> EXPLICIT
            "clean" -> CLEAN
            else -> UNKNOWN
        }
    }

    private fun parseIsBlocked(): Boolean {
        val blockElement = parent.element(QName.get("isBlocked", itunesNamespace)) ?: return false

        return blockElement.textTrim.toLowerCase() == "yes"
    }

    @Throws(MalformedURLException::class)
    private fun parseImage() = imageString?.toURL()
}
