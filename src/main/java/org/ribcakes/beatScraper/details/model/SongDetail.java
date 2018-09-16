package org.ribcakes.beatScraper.details.model;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class SongDetail {
    private long id;
    private String key;
    private String name;
    private String description;
    private String uploader;
    private long uploaderId;
    private String songName;
    private String songSubName;
    private String authorName;
    private long bpm;
    private Map<Difficulty, DifficultyDetail> difficulties;
    private long downloadCount;
    private long playedCount;
    private long upVotes;
    private long upVotesTotal;
    private long downVotes;
    private long downVotesTotal;
    private String version;
    private CreatedDetail createdAt;
    private String linkUrl;
    private String downloadUrl;
    private String coverUrl;
    private String hashMd5;
    private String hashSha1;
}
