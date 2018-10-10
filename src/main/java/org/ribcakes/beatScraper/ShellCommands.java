package org.ribcakes.beatScraper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ribcakes.beatScraper.scrape.Scraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@Slf4j
@ShellComponent
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShellCommands {

    @NonNull
    private final Scraper scraper;

    @ShellMethod("Scrape the BeatSaver website.")
    public void scrape(@ShellOption(defaultValue = "C:/Program Files (x86)/Steam/steamapps/common/Beat Saber/CustomSongs") final String outputDir)
            throws Exception {
        log.info("Using output directory [ {} ].", outputDir);
        this.scraper.scrape(outputDir);
    }
}
