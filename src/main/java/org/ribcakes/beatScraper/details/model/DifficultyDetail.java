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
public class DifficultyDetail {
    private String              difficulty;
    private long                rank;
    private String              audioPath;
    private String              jsonPath;
    private StatDetail          stats;

    @Getter(onMethod = @__(@JsonAnyGetter))
    private Map<String, Object> properties = new HashMap<>();

    @JsonAnySetter
    public void add(String key, Object value) {
        properties.put(key, value);
    }
}
