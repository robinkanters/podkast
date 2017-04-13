# Podkast
_Simpler library to read podcast feeds_

## Usage
__Read Feed__
	
### Java

```java
Podcast podcast = PodcastLoader.load(new URL("FEED URL"));
System.out.println("Title - " + podcast.getTitle());
```

### Kotlin

```kotlin
val podcast = PodcastLoader.load(URL("FEED URL"))
println("Title - ${podcast.title}")
```

__Get Episodes__

### Java

```java
Podcast podcast = PodcastLoader.load(new URL("FEED URL"));
List<Episode> episodes = podcast.getEpisodes();
for(Episode episode : episodes) {
    System.out.println("Episode Title - " + episode.getTitle());
}
```

### Kotlin

```java
val podcast = PodcastLoader.load(URL("FEED URL"))
val episodes = podcast.episodes
episodes.forEach {
    println("Episode Title - ${it.title}"
}
```

## Installation

### Gradle

1. Add the JitPack.io repo to your gradle config:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. Add Podkast as dependency:

```groovy
dependencies {
    compile 'com.github.robinkanters:podkast:0.1.1'
}
```

## Inspiration
[Podcast-Feed-Library](https://github.com/MarkusLewis/Podcast-Feed-Library) _Original implementation in Java_
