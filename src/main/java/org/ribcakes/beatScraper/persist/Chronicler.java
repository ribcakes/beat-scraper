package org.ribcakes.beatScraper.persist;

import org.ribcakes.beatScraper.persist.model.DownloadRecord;

import java.util.Set;

public interface Chronicler {
    Set<DownloadRecord> getRecords(String outputDir);

    void saveRecords(String outputDir, Set<DownloadRecord> records);
}
