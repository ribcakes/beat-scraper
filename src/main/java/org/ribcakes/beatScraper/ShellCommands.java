package org.ribcakes.beatScraper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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

    @ShellMethod("scrape beatsaver")
    public void run() throws Exception {
        Map<String, DownloadRecord> records = this.chronicler.getRecords();
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
                               .map(this::downloadSong)
                               .filter(Optional::isPresent)
                               .map(Optional::get)
                               .collect(Collectors.toMap(record -> record.getDetail().getKey(), Function.identity()));
        records.putAll(newRecords);

        this.chronicler.saveRecords(records);
    }

    private Optional<DownloadRecord> downloadSong(final SongDetail detail) {
        String key = detail.getKey();
        String downloadUrl = detail.getDownloadUrl();

        Optional<DownloadRecord> record = this.downloader.download(downloadUrl, key)
                                                         .map(file -> DownloadRecord.builder()
                                                                                    .status(DownloadStatus.DOWNLOADED)
                                                                                    .detail(detail)
                                                                                    .build());

        return record;
    }
}
