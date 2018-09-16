package org.ribcakes.beatScraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class Config {
    @Value("${recordFile}")
    private String       recordFile;
    @Value("${apiUrl}")
    private String       apiUrl;
    @Value("${downloadUrl}")
    private String       downloadUrl;
    @Value("#{'${charsets}'.split(',')}")
    private List<String> charsets;

    @Bean
    public String recordFile() {
        return this.recordFile;
    }

    @Bean
    public String apiUrl() {
        return this.apiUrl;
    }

    @Bean
    public String downloadUrl() {
        return this.downloadUrl;
    }

    @Bean
    public List<Charset> charsets() {
        return this.charsets.stream()
                            .map(Charset::forName)
                            .collect(Collectors.toList());
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }
}
