package org.ribcakes.beatScraper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ribcakes.beatScraper.scrape.Scraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@Slf4j
@ShellComponent
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShellCommands {

    @NonNull
    private final Scraper scraper;

    @ShellMethod("Scrape the BeatSaver website.")
    public void scrape(final String outputDir) throws Exception {
        log.info("Using output directory [ {} ].", outputDir);
        this.scraper.scrape(outputDir);
    }
}
