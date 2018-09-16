package org.ribcakes.beatScraper.details;

import org.ribcakes.beatScraper.details.model.DetailPage;

import java.util.Optional;

public interface DetailService {
    Optional<DetailPage> getPage(int offset);
}
