package com.robinkanters.podkast.parser

import org.dom4j.Element

class CategoryParser {
    fun parse(channel: Element): Array<String> {
        if (channel.element("category") == null) return emptyArray()

        return channel.elements("category")
                .parallelStream()
                .map { it as Element }
                .map { parseChild(it) }
                .filter { it != null }
                .toArray { size -> Array(size, { "" }) }
    }

    private fun parseChild(child: Element): String? {
        if (child.namespacePrefix.toLowerCase() == "itunes") {
            return parseItunesCategories(child)
        }

        return child.text
    }

    private fun parseItunesCategories(child: Element): String? {
        if (child.elements("category").size > 0)
            return parseItunesSubcategories(child)

        return parseSingleItunesCategory(child)
    }

    private fun parseItunesSubcategories(child: Element): String? {
        val mainCategory = if (child.attribute("text") != null) child.attributeValue("text") else child.text

        return child.elements("category").asSequence()
                .map { it as Element }
                .map(this::parseSingleItunesCategory)
                .fold("$mainCategory > ") { acc, s -> "$acc > $s" }
    }

    private fun parseSingleItunesCategory(child: Element): String? {
        if (child.attribute("text") != null)
            return child.attributeValue("text")
        else
            return child.text
    }
}
