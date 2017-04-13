package com.robinkanters.podkast.models

import com.robinkanters.podkast.utils.StringUtils.toURL
import org.dom4j.Element
import java.net.MalformedURLException
import java.net.URL

class TextInputInfo(private val textInputElement: Element) {
    val title: String? by lazy { parseTitle() }
    val description: String? by lazy { parseDescription() }
    val name: String? by lazy { parseName() }
    val link: URL? by lazy { parseLink() }

    private fun parseTitle() = textInputElement.element("title")?.text

    private fun parseDescription() = textInputElement.element("description")?.text

    private fun parseName() = textInputElement.element("name")?.text

    @Throws(MalformedURLException::class)
    private fun parseLink() = textInputElement.element("link")?.textTrim?.toURL()
}
