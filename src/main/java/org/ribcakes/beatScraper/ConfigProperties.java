package org.ribcakes.beatScraper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
public class ConfigProperties {
    @Value("${outputDir}")
    private String outputDir;
    @Value("${recordFile}")
    private String recordFile;
    @Value("${browseUrl}")
    private String browseUrl;
    @Value("${downloadUrl}")
    private String downloadUrl;

    @Bean
    public String outputDir() {
        return this.outputDir;
    }

    @Bean
    public String recordFile() {
        return this.recordFile;
    }

    @Bean
    public String browseUrl() {
        return this.browseUrl;
    }

    @Bean
    public String downloadUrl() {
        return this.downloadUrl;
    }
}
