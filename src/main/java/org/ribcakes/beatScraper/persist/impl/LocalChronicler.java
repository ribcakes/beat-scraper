package org.ribcakes.beatScraper.persist.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ribcakes.beatScraper.persist.Chronicler;
import org.ribcakes.beatScraper.persist.model.DownloadRecord;
import org.ribcakes.beatScraper.persist.model.ModelVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LocalChronicler implements Chronicler {

    private static final TypeReference<Map<String, DownloadRecord>> V1_TYPE_REFERENCE = new TypeReference<>() { };
    private static final TypeReference<Set<DownloadRecord>>         V2_TYPE_REFERENCE = new TypeReference<>() { };

    @NonNull
    private final ObjectMapper mapper;

    @Override
    public Set<DownloadRecord> getRecords(final String outputDir) {
        for (ModelVersion version : ModelVersion.values()) {
            File file = this.getFile(outputDir, version);
            if (file.exists()) {
                Set<DownloadRecord> records = this.readFile(file, version);

                log.info("Loaded saved records file at [ {} ].", file);
                return records;
            }
        }
        return new HashSet<>();
    }

    private Set<DownloadRecord> readFile(final File file, final ModelVersion version) {
        try {
            switch (version) {
                case V1:
                    Map<String, DownloadRecord> recordMap = this.mapper.readValue(file, V1_TYPE_REFERENCE);
                    Collection<DownloadRecord> downloadRecords = recordMap.values();
                    Set<DownloadRecord> recordSet = new HashSet<>(downloadRecords);

                    return recordSet;
                case V2:
                    Set<DownloadRecord> records = this.mapper.readValue(file, V2_TYPE_REFERENCE);

                    return records;
                default:
                    String message = String.format("Unable to read file [ %s ] for Model Version [ %s ].", file, version);
                    throw new RuntimeException(message);
            }
        } catch (IOException e) {
            String message = String.format("Unable to read record file at [ %s ].", file.getAbsolutePath());

            throw new RuntimeException(message, e);
        }
    }

    @Override
    public void saveRecords(final String outputDir, final Set<DownloadRecord> records) {
        File file = this.getFile(outputDir, ModelVersion.V2);

        try {
            this.mapper.writeValue(file, records);
        } catch (IOException e) {
            String message = String.format("Unable to write record file at [ %s ].", file.getAbsolutePath());

            throw new RuntimeException(message, e);
        }
    }

    private File getFile(final String outputDir, final ModelVersion modelVersion) {
        String fileName = String.format(modelVersion.getFileTemplate(), outputDir);
        File file = new File(fileName);

        return file;
    }
}
