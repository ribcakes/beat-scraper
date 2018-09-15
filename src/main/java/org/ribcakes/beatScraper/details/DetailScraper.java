package org.ribcakes.beatScraper.details;

import org.ribcakes.beatScraper.details.model.SongDetail;

import java.util.List;

public interface DetailScraper {
    List<SongDetail> scrape(int page);
}
