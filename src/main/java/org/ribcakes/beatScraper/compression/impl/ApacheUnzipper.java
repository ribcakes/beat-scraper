package org.ribcakes.beatScraper.compression.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.ribcakes.beatScraper.compression.Unzipper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
@Component
public class ApacheUnzipper implements Unzipper {

    @Override
    public void unzip(final File file, final String destination) {
        log.info("Unzipping file [ {} ].", file.getAbsolutePath());
        try (ZipFile zipFile = new ZipFile(file)) {
            zipFile.stream()
                   .forEach(entry -> this.processEntry(zipFile, destination, entry));
        } catch (IOException e) {
            String message = String.format("Unable to open zipped file [ %s ].", file.getAbsolutePath());
            throw new RuntimeException(message, e);
        }
    }

    private void processEntry(final ZipFile zipFile, final String destination, final ZipEntry entry) {
        File destinationFile = new File(destination, entry.getName());
        if (entry.isDirectory()) {
            destinationFile.mkdirs();
        } else {
            destinationFile.getParentFile()
                           .mkdirs();
            try (InputStream inputStream = zipFile.getInputStream(entry);
                 OutputStream outputStream = new FileOutputStream(destinationFile)) {
                IOUtils.copy(inputStream, outputStream);
            } catch (IOException e) {
                String message = String.format("Unable to unzip file [ %s ].", entry.getName());
                throw new RuntimeException(message, e);
            }
        }
    }
}
