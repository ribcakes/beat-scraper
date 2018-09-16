package org.ribcakes.beatScraper.compression;

import java.io.File;

public interface Unzipper {
    void unzip(File file, String destination);
}
