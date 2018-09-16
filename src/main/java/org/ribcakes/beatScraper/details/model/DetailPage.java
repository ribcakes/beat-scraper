package org.ribcakes.beatScraper.details.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DetailPage {
    private List<SongDetail> songs;
    private long             total;
}
