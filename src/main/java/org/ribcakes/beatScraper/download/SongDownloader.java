package org.ribcakes.beatScraper.download;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Optional;

public interface SongDownloader {
    Optional<File> download(int songId, int downloadId, String fileName) throws MalformedURLException;
}
