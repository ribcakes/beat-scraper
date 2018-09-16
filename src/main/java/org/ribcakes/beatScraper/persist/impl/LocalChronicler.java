package org.ribcakes.beatScraper.persist.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ribcakes.beatScraper.persist.Chronicler;
import org.ribcakes.beatScraper.persist.model.DownloadRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LocalChronicler implements Chronicler {

    private static final TypeReference<Map<String, DownloadRecord>> TYPE_REFERENCE = new TypeReference<>() { };

    @NonNull
    private final String       recordFile;
    @NonNull
    private final ObjectMapper mapper;

    @Override
    public Map<String, DownloadRecord> getRecords(final String outputDir) {
        File file = this.getFile(outputDir);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try {
            Map<String, DownloadRecord> records = this.mapper.readValue(file, TYPE_REFERENCE);
            return records;
        } catch (IOException e) {
            String message = String.format("Unable to read record file at [ %s ].", file.getAbsolutePath());

            throw new RuntimeException(message, e);
        }
    }

    @Override
    public void saveRecords(final String outputDir, final Map<String, DownloadRecord> records) {
        File file = this.getFile(outputDir);

        try {
            this.mapper.writeValue(file, records);
        } catch (IOException e) {
            String message = String.format("Unable to write record file at [ %s ].", file.getAbsolutePath());

            throw new RuntimeException(message, e);
        }
    }

    private File getFile(final String outputDir) {
        String fileName = String.format("%s/%s.json", outputDir, this.recordFile);
        File file = new File(fileName);

        return file;
    }
}
