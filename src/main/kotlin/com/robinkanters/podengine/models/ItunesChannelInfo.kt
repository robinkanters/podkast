package com.robinkanters.podengine.models

import com.robinkanters.podengine.utils.StringUtils.toURL
import org.dom4j.Element
import org.dom4j.QName
import java.net.MalformedURLException
import java.net.URL

class ItunesChannelInfo(parent: Element) : ItunesInfo(parent) {
    val complete: Boolean by lazy { parseIsComplete() }
    val newFeedURL: URL? by lazy { parseNewFeedURL() }
    val owner: ItunesOwner? by lazy { parseOwner() }

    private fun parseIsComplete() = parent.element(QName.get("complete", "itunes"))?.textTrim?.toLowerCase() == "yes"

    @Throws(MalformedURLException::class)
    private fun parseNewFeedURL() = parent.element(QName.get("new-feed-url", "itunes"))?.textTrim?.toURL()

    private fun parseOwner(): ItunesOwner? {
        val ownerElement = parent.element(QName.get("owner", "itunes")) ?: return null

        return ItunesOwner(ownerElement)
    }
}
