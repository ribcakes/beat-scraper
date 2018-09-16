package org.ribcakes.beatScraper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ribcakes.beatScraper.compression.Unzipper;
import org.ribcakes.beatScraper.details.DetailIterator;
import org.ribcakes.beatScraper.details.DetailService;
import org.ribcakes.beatScraper.details.model.CreatedDetail;
import org.ribcakes.beatScraper.details.model.SongDetail;
import org.ribcakes.beatScraper.download.SongDownloader;
import org.ribcakes.beatScraper.persist.Chronicler;
import org.ribcakes.beatScraper.persist.model.DownloadRecord;
import org.ribcakes.beatScraper.persist.model.DownloadStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ShellComponent
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShellCommands {

    @NonNull
    private final Chronicler chronicler;
    @NonNull
    private final DetailService detailService;
    @NonNull
    private final SongDownloader downloader;
    @NonNull
    private final Unzipper unzipper;

    @ShellMethod("scrape beatsaver")
    public void scrape(final String outputDir) throws Exception {
        Map<String, DownloadRecord> records = this.chronicler.getRecords(outputDir);
        LocalDateTime newestDate = records.values()
                                          .stream()
                                          .max(Comparator.comparing(record -> record.getDetail()
                                                                                    .getCreatedAt()
                                                                                    .getDate()))
                                          .map(DownloadRecord::getDetail)
                                          .map(SongDetail::getCreatedAt)
                                          .map(CreatedDetail::getDate)
                                          .orElse(null);

        Iterator<SongDetail> iterator = new DetailIterator(this.detailService, newestDate);
        Spliterator<SongDetail> spliterator = Spliterators.spliteratorUnknownSize(iterator, 0);

        Map<String, DownloadRecord> newRecords
                = StreamSupport.stream(spliterator, false)
                               .map(detail -> this.downloadSong(outputDir, detail))
                               .filter(Optional::isPresent)
                               .map(Optional::get)
                               .collect(Collectors.toMap(record -> record.getDetail().getKey(), Function.identity()));
        records.putAll(newRecords);

        this.chronicler.saveRecords(outputDir, records);
    }

    private Optional<DownloadRecord> downloadSong(final String outputDir, final SongDetail detail) {
        String key = detail.getKey();
        String downloadUrl = detail.getDownloadUrl();
        String outputFolder = String.format("%s/%s", outputDir, key);

        Optional<DownloadRecord> record = this.downloader.download(downloadUrl, outputFolder)
                                                         .stream()
                                                         .peek(file -> this.unzipper.unzip(file, outputFolder))
                                                         .map(file -> DownloadRecord.builder()
                                                                                    .status(DownloadStatus.DOWNLOADED)
                                                                                    .detail(detail)
                                                                                    .build())
                                                         .findFirst();

        return record;
    }
}
