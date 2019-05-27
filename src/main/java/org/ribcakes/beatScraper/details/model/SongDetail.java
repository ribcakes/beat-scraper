package org.ribcakes.beatScraper.details.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;

@Value
@Builder
public class SongDetail {
    // Required Fields
    private long          id;
    private String        key;
    private String        name;
    private String        downloadUrl;
    private CreatedDetail createdAt;

    private String description;
    private String uploader;
    private long   uploaderId;
    private String songName;
    private String songSubName;
    private String authorName;
    private long   bpm;
    private long   downloadCount;
    private long   playedCount;
    private long   upVotes;
    private long   upVotesTotal;
    private long   downVotes;
    private long   downVotesTotal;
    private long   rating;
    private String version;
    private String linkUrl;
    private String coverUrl;
    private String hashMd5;
    private String hashSha1;

    private Map<String, DifficultyDetail> difficulties;

    @Getter(onMethod = @__(@JsonAnyGetter))
    private Map<String, Object> properties = new HashMap<>();

    @JsonAnySetter
    public void add(String key, Object value) {
        properties.put(key, value);
    }
}
