package org.ribcakes.beatScraper.persist.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.ribcakes.beatScraper.details.model.SongDetail;

@Value
@Builder
public class DownloadRecord {
    @NonNull
    private int songNum;
    @NonNull
    private DownloadStatus status;
    @NonNull
    private SongDetail details;
}
