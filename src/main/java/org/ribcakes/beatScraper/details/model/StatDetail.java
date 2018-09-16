package org.ribcakes.beatScraper.details.model;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class StatDetail {
    private long time;
    private Map<String, Long> slashstat;
    private long events;
    private long notes;
    private long obstacles;
}
