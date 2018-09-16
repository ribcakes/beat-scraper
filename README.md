# Beat Scraper
> This is a quick and dirty command line java project that scrapes and downloads all songs hosted on [BeatSaver](https://beatsaver.com). 

## Features
- **Incremental Scrapes**: The first run creates a local file, `records.json`, that enables subsequent runs to only download new songs.
- **Gradle Wrapper**: The application makes use of [Gradle Wrappers](https://docs.gradle.org/current/userguide/gradle_wrapper.html) to make building the project a breeze.

## Getting Started
- Install the [Beat Saber Mod Manager](https://github.com/Umbranoxio/BeatSaberModInstaller/releases), and install the Song Loader plugin.
```bash
# 1. Clone the repository.
git clone https://github.com/ribcakes/beat-scraper.git

# 2 Install the java 10 jdk or higher
sudo apt-get install openjdk-11-jdk

# 3. Run the Gradle wrapper.
./gradlew build 

# 4. Run the constructed jar.
java -jar build/libs/beat-scraper-0.0.1-SNAPSHOT.jar
```
A similar quickstart is available for windows, just run `gradlew.bat build` instead of `./gradlew build`.
The Oracle JDK 10 build can be found here: http://www.oracle.com/technetwork/java/javase/downloads/jdk10-downloads-4416644.html

## Usage
This application is built off of [Spring Shell](https://docs.spring.io/spring-shell/docs/current/reference/htmlsingle/) which provides the `help` 
command for additional information. The shell can be exited via `exit` or `quit`.

### scrape
The `scrape` command is the application's singular entry point. It takes a single argument, `outputDir`. This should be the Custom Songs location for your BeatSaber install.

```bash
shell:> scrape "C:/Program Files (x86)/Steam/steamapps/common/Beat Saber/CustomSongs"
``` 
