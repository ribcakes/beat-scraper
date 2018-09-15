package org.ribcakes.beatScraper.details.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class SongDetail {
    private String uploader;
    private LocalDateTime uploadedAt;
    private String title;
    private String version;
    private String author;
    private List<Difficulty> difficulties;
    private long downloads;
    private long finished;
    private long thumbsUp;
    private long thumbsDown;
    private long overallScore;
    private boolean lightingEvents;
}
