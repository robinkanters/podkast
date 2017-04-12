package com.robinkanters.podengine.models

import org.dom4j.Element

class CloudInfo(private val cloudElement: Element) {
    val domain: String? by lazy { parseDomain() }
    val path: String? by lazy { parsePath() }
    val registerProcedure: String? by lazy { parseRegisterProcedure() }
    val protocol: String? by lazy { parseProtocol() }
    val port: Int? by lazy { parsePort() }

    private fun parseDomain() = cloudElement.attribute("domain")?.value

    private fun parsePath() = cloudElement.attribute("path")?.value

    private fun parseRegisterProcedure() = cloudElement.attribute("registerProcedure")?.value

    private fun parseProtocol() = cloudElement.attribute("protocol")?.value

    private fun parsePort() = cloudElement.attribute("port")?.value!!.toInt()
}
