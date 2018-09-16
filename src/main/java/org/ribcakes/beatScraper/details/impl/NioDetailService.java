package org.ribcakes.beatScraper.details.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ribcakes.beatScraper.details.DetailService;
import org.ribcakes.beatScraper.details.model.DetailPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NioDetailService implements DetailService {

    @NonNull
    private final String       apiUrl;
    @NonNull
    private final ObjectMapper mapper;

    @Override
    public Optional<DetailPage> getPage(int offset) {
        String pageUrl = String.format(this.apiUrl, offset);

        try {
            URL url = new URL(pageUrl);
            DetailPage detailPage = this.mapper.readValue(url, DetailPage.class);

            return Optional.ofNullable(detailPage);
        } catch (IOException e) {
            if (FileNotFoundException.class.isInstance(e)) {
                log.info("Unable to get page for url [ {} ].", pageUrl);
                return Optional.empty();
            }

            String message = String.format("Unable to get song detail page at url [ %s ].", pageUrl);
            throw new RuntimeException(message, e);
        }
    }
}
