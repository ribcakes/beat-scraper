package org.ribcakes.beatScraper.persist.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ModelVersion {

    V2("%s/record_set.json", 2),
    V1("%s/records.json", 1);

    @NonNull
    private final String fileTemplate;
    private final int version;
}
