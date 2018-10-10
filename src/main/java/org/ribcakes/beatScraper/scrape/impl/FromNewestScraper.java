package org.ribcakes.beatScraper.scrape.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ribcakes.beatScraper.compression.Unzipper;
import org.ribcakes.beatScraper.details.DetailIterator;
import org.ribcakes.beatScraper.details.DetailService;
import org.ribcakes.beatScraper.details.model.CreatedDetail;
import org.ribcakes.beatScraper.details.model.SongDetail;
import org.ribcakes.beatScraper.download.SongDownloader;
import org.ribcakes.beatScraper.persist.Chronicler;
import org.ribcakes.beatScraper.persist.model.DownloadRecord;
import org.ribcakes.beatScraper.persist.model.DownloadStatus;
import org.ribcakes.beatScraper.scrape.Scraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FromNewestScraper implements Scraper {

    @NonNull
    private final Chronicler     chronicler;
    @NonNull
    private final DetailService  detailService;
    @NonNull
    private final SongDownloader downloader;
    @NonNull
    private final Unzipper       unzipper;

    @Override
    public void scrape(final String outputDir) {
        File outputFolder = new File(outputDir);
        if (!outputFolder.exists()) {
            String message = String.format("User provided output dir [ %s ] does not exist!", outputFolder.getAbsolutePath());
            throw new RuntimeException(message);
        }
        if (!outputFolder.isDirectory()) {
            String message = String.format("User provided output dir [ %s ] is not a directory!", outputFolder.getAbsolutePath());
            throw new RuntimeException(message);
        }

        Set<DownloadRecord> records = this.chronicler.getRecords(outputDir);
        LocalDateTime newestDate = records.stream()
                                          .map(DownloadRecord::getDetail)
                                          .map(SongDetail::getCreatedAt)
                                          .map(CreatedDetail::getDate)
                                          .max(LocalDateTime::compareTo)
                                          .orElse(null);
        log.info("==================================================");
        log.info("Downloading songs songs uploaded after [ {} ]", newestDate);

        Iterator<SongDetail> iterator = new DetailIterator(this.detailService, newestDate);
        Spliterator<SongDetail> spliterator = Spliterators.spliteratorUnknownSize(iterator, 0);

        Set<DownloadRecord> newRecords = StreamSupport.stream(spliterator, false)
                                                      .map(detail -> this.downloadSong(outputDir, detail))
                                                      .collect(Collectors.toSet());
        records.addAll(newRecords);

        log.info("==================================================");
        log.info("Retrying download of previously failed songs.");
        Set<DownloadRecord> erroredRecords = records.stream()
                                                    .filter(record -> DownloadStatus.ERROR.equals(record.getStatus()))
                                                    .map(DownloadRecord::getDetail)
                                                    .map(detail -> this.downloadSong(outputDir, detail))
                                                    .collect(Collectors.toSet());
        records.addAll(erroredRecords);

        this.chronicler.saveRecords(outputDir, records);
    }

    private DownloadRecord downloadSong(final String outputDir, final SongDetail detail) {
        log.debug("==================================================");
        log.info("Beginning Download of [ {} ].", detail.getName());
        String key = detail.getKey();
        String downloadUrl = detail.getDownloadUrl();
        String outputFolder = String.format("%s/%s", outputDir, key);

        DownloadRecord record = this.downloader.download(downloadUrl, outputFolder)
                                               .stream()
                                               .peek(file -> this.unzipper.unzip(file, outputFolder))
                                               .peek(file -> log.debug("Deleting file [ {} ].", file.getAbsolutePath()))
                                               .peek(File::delete)
                                               .map(file -> DownloadRecord.builder()
                                                                          .status(DownloadStatus.DOWNLOADED)
                                                                          .detail(detail)
                                                                          .build())
                                               .findFirst()
                                               .orElseGet(() -> DownloadRecord.builder()
                                                                              .status(DownloadStatus.ERROR)
                                                                              .detail(detail)
                                                                              .build());
        log.info("Completed Download of [ {} ].", detail.getName());

        return record;
    }
}
