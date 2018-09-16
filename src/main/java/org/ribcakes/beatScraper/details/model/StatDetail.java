package org.ribcakes.beatScraper.details.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StatDetail {
    private long   time;
    private Object slashstat;
    private long   events;
    private long   notes;
    private long   obstacles;
}
