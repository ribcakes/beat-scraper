package org.ribcakes.beatScraper.persist;

import org.ribcakes.beatScraper.persist.model.DownloadRecord;

import java.util.Map;

public interface Chronicler {
    Map<String, DownloadRecord> getRecords(String outputDir);

    void saveRecords(String outputDir, Map<String, DownloadRecord> records);
}
