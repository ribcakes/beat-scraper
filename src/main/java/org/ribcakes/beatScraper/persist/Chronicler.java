package org.ribcakes.beatScraper.persist;

import org.ribcakes.beatScraper.persist.model.DownloadRecord;

import java.util.Map;

public interface Chronicler {
    Map<Integer, DownloadRecord> getRecords();

    void saveRecords(Map<Integer, DownloadRecord> records);
}
