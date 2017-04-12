package com.robinkanters.podengine.utils

import com.robinkanters.podengine.exceptions.MalformedFeedException
import org.dom4j.Element

object ElementUtils {
    @JvmStatic
    fun Element.getAttributeValueOrThrow(field: String): String {
        return attribute(field)?.value ?: throw MalformedFeedException("Missing required $field attribute for element Enclosure.")
    }
}
