package org.ribcakes.beatScraper.download;

import java.io.File;
import java.util.Optional;

public interface SongDownloader {
    Optional<File> download(String downloadUrl, String key);
}
