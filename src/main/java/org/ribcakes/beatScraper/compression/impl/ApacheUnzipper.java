package org.ribcakes.beatScraper.compression.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.ribcakes.beatScraper.compression.Unzipper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream ;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApacheUnzipper implements Unzipper {

    @NonNull
    private final List<Charset> charsets;

    @Override
    public void unzip(final File file, final String destination) {
        log.debug("Unzipping file [ {} ].", file.getAbsolutePath());

        for (Charset charset : this.charsets) {
            try {
                this.unzip(file, destination, charset);
            } catch (IllegalArgumentException e) {
                boolean isMalformedException = e.getMessage()
                                                .toLowerCase()
                                                .contains("malformed");
                if (isMalformedException) {
                    String message = String.format("Encountered Malformed exception with charset [ %s ].", charset);
                    log.warn(message);
                    log.debug(message, e);
                    continue;
                }
                throw e;
            }
            return;
        }
        String message = String.format("Unable to unzip [ %s ] with the given charsets [ %s ]", file, this.charsets);
        throw new RuntimeException(message);
    }

    private void unzip(final File file, final String destination, final Charset charset) {
        log.debug("Unzipping file [ {} ] with charset [ {} ].", file.getAbsolutePath(), charset);

        try (ZipFile zipFile = new ZipFile(file, charset)) {
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
