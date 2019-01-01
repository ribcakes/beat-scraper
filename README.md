# Beat Scraper
> BeatScraper is a [SpringShell](https://docs.spring.io/spring-shell/docs/current/reference/htmlsingle/) application that scrapes and downloads all songs hosted on [BeatSaver](https://beatsaver.com). 

## Features
- **Incremental Scrapes**: The first run creates a local file, `records.json`, that enables subsequent runs to only download new songs.
- **Retry on Error**: Any song that fails to download will be tracked for later retries. 

## Getting Started
- Install the [Beat Saber Mod Manager](https://github.com/Umbranoxio/BeatSaberModInstaller/releases), and install the Song Loader plugin.
- Install JDK 10.
  - you can find the Oracle version here:  http://www.oracle.com/technetwork/java/javase/downloads/jdk10-downloads-4416644.html
- Download the latest release jar from [here](https://github.com/ribcakes/beat-scraper/releases).
- Run the release jar with `java -jar ` if you download the jar file, or just run the exe if you chose that option.
  - e.g. `java -jar beat-scraper-1.1.jar`

## Usage
After running the jar, you will be dropped into a [Spring Shell](https://docs.spring.io/spring-shell/docs/current/reference/htmlsingle/) session 
which provides the `help` command for additional information. The shell can be exited via `exit` or `quit`.

### scrape
#### Description
Scrapes the BeatSaver API and downloads every song.

#### Summary
```
scrape 
[--outputDir <value>]
```

#### Options

`--ouptutDir` (string)
> The Custom Songs location for your BeatSaber install.   
> Defaults to `C:/Program Files (x86)/Steam/steamapps/common/Beat Saber/CustomSongs`

#### Examples

```bash
shell:> scrape 
```
```bash
shell:> scrape "D:/Program Files (x86)/Steam/steamapps/common/Beat Saber/CustomSongs"
``` 