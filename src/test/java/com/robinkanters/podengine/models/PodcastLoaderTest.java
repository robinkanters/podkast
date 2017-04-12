package com.robinkanters.podengine.models;

import com.robinkanters.podengine.PodcastLoader;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;

public class PodcastLoaderTest {

    @Test
    public void testRelayFeed() throws Exception {
        Podcast podcast = PodcastLoader.load(new URL("https://www.relay.fm/master/feed"));
        assertEquals("Relay FM Master Feed", podcast.getTitle());
    }

}
