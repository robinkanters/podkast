package com.robinkanters.podengine

import com.robinkanters.podengine.exceptions.InvalidFeedException
import com.robinkanters.podengine.exceptions.MalformedFeedException
import com.robinkanters.podengine.models.Podcast
import org.apache.commons.io.IOUtils
import org.dom4j.DocumentException
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import java.nio.charset.Charset

object PodcastLoader {
    @JvmStatic
    @Throws(InvalidFeedException::class, MalformedFeedException::class)
    fun load(feed: URL): Podcast {
        try {
            return tryLoadFeed(feed)
        } catch (e: IOException) {
            throw InvalidFeedException("Error reading feed.", e)
        } catch (e: DocumentException) {
            throw InvalidFeedException("Error parsing feed XML.", e)
        }
    }

    private fun tryLoadFeed(feed: URL): Podcast {
        instantiateConnection(feed).getInputStream().use { stream ->
            return Podcast(downloadFeedXml(stream))
        }
    }

    private fun downloadFeedXml(inputStream: InputStream): String {
        val podcastXml = IOUtils.toString(inputStream, Charset.defaultCharset())

        return podcastXml
    }

    private fun instantiateConnection(feed: URL): URLConnection {
        return feed.openConnection().apply {
            setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
        }
    }
}
