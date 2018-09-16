package org.ribcakes.beatScraper.details.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DifficultyDetail {
    private Difficulty difficulty;
    private long rank;
    private String audioPath;
    private String jsonPath;
    private StatDetail stats;
}
