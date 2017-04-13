package com.robinkanters.podkast.utils

import com.robinkanters.podkast.exceptions.MalformedFeedException
import org.dom4j.Element

object ElementUtils {
    @JvmStatic
    fun Element.getAttributeValueOrThrow(field: String): String {
        return attribute(field)?.value ?: throw MalformedFeedException("Missing required $field attribute for element Enclosure.")
    }
}
