package org.ribcakes.beatScraper.persist.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import org.ribcakes.beatScraper.details.model.SongDetail;

@Value
@Builder
@EqualsAndHashCode(exclude = "status")
public class DownloadRecord {
    @NonNull
    private DownloadStatus status;
    @NonNull
    private SongDetail     detail;
}
