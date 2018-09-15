package org.ribcakes.beatScraper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ribcakes.beatScraper.details.DetailScraper;
import org.ribcakes.beatScraper.details.model.SongDetail;
import org.ribcakes.beatScraper.download.SongDownloader;
import org.ribcakes.beatScraper.persist.Chronicler;
import org.ribcakes.beatScraper.persist.model.DownloadRecord;
import org.ribcakes.beatScraper.persist.model.DownloadStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.File;
import java.util.Map;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShellCommands {
    private static final int DOWNLOAD_OFFSET = 524;

    @NonNull
    private final Chronicler chronicler;
    @NonNull
    private final DetailScraper detailScraper;
    @NonNull
    private final SongDownloader downloader;

    @ShellMethod("scrape beatsaver")
    public void run(final int stop) throws Exception {
        Map<Integer, DownloadRecord> records = this.chronicler.getRecords();
        Integer start = records.keySet()
                               .stream()
                               .max(Integer::compareTo)
                               .map(integer -> integer + 1)
                               .orElse(DOWNLOAD_OFFSET);

        for (int index = start; index <= stop; index++) {
            int downloadNum = index - DOWNLOAD_OFFSET;
            String fileName = String.format("%d-%d", index, downloadNum);
            Optional<File> maybeFile = this.downloader.download(index, downloadNum, fileName);

            DownloadStatus status = DownloadStatus.NOT_FOUND;
            if (maybeFile.isPresent()) {
                status = DownloadStatus.DOWNLOADED;
            }
            DownloadRecord record = DownloadRecord.builder()
                                                  .songNum(index)
                                                  .status(status)
                                                  .details(SongDetail.builder().build())
                                                  .build();
            records.put(index, record);
        }
        this.chronicler.saveRecords(records);
    }
}
