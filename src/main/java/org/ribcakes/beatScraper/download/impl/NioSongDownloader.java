package org.ribcakes.beatScraper.download.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ribcakes.beatScraper.download.SongDownloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NioSongDownloader implements SongDownloader {

    @NonNull
    private final String outputDir;

    @PostConstruct
    public void init() {
        new File(this.outputDir + "/").mkdirs();
    }

    @Override
    @SneakyThrows
    public Optional<File> download(final String downloadUrl, final String fileName) {
        URL url = new URL(downloadUrl);

        String downloadLocation = String.format("%s/%s.zip", this.outputDir, fileName);
        try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(downloadLocation);
             FileChannel fileChannel = fileOutputStream.getChannel()) {
            log.info("Downloading song from url [ {} ] to file [ {} ].", url, downloadLocation);

            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            File file = new File(downloadLocation);
            if (file.exists()) {
                log.info("Downloaded song from url [ {} ] to file [ {} ].", url, file.getAbsolutePath());

                return Optional.of(file);
            }

            String message = String.format("Downloaded url [ %s ], but it wasn't saved.", url);
            throw new RuntimeException(message);
        } catch (IOException e) {
            if (FileNotFoundException.class.isInstance(e)) {
                log.info("No song found on url [ {} ].", url);
                return Optional.empty();
            }

            String message = String.format("Unable to downloaded url [ %s ].", url);
            throw new RuntimeException(message, e);
        }
    }
}
