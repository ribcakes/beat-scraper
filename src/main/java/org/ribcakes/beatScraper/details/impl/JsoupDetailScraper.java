package org.ribcakes.beatScraper.details.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ribcakes.beatScraper.details.DetailScraper;
import org.ribcakes.beatScraper.details.model.SongDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JsoupDetailScraper implements DetailScraper {

    @NonNull
    private final String browseUrl;

    @Override
    public List<SongDetail> scrape(final int page) {
        String pageUrl = String.format("https://beatsaver.com/browse/newest/%d", page);

        try {
            // TODO wrap to avoid static
            Document document = Jsoup.connect(pageUrl).get();

            Elements songBlocks = document.select("div.container > div.row > table");
            for (Element songBlock : songBlocks) {
                Elements tableRows = songBlock.select("tbody > tr");

                // e.g. Uploaded by: moistwang (2018-05-08 21:13:18)
                String uploadBlock = tableRows.select("tbody > tr > th > small").text();

                /*
                result = {ArrayList@7364}  size = 7
                0 = "Song: Mii Channel Theme -"
                1 = "Version: 34-12"
                2 = "Author: Nintendo"
                3 = "Difficulties: Hard"
                4 = "Downloads: 2356 || Finished: 495 || 3 / 1"
                5 = "Lighting Events: No"
                6 = "Download File view at bsaber.com"
                 */
                List<String> blocks = tableRows.select("tbody > tr > td").eachText();

                // e.g. Song: Mii Channel Theme -
                String titleBlock = blocks.get(0);
                // e.g. Version: 34-12
                String versionBlock = blocks.get(1);
                //e.g. Author: Nintendo
                String authorBlock = blocks.get(2);
                // e.g Difficulties: Hard, Expert, Normal
                String difficultiesBlock = blocks.get(3);
                // e.g. Downloads: 2356 || Finished: 495 || 3 / 1
                String songInfoBlock = blocks.get(4);
                // e.g. Lighting Events: No
                String lightingEffectBlock = blocks.get(5);

                // TODO finish parsing this
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
