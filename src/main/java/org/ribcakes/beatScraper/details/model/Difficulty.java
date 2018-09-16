package org.ribcakes.beatScraper.details.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter(onMethod = @__(@JsonValue))
public enum Difficulty {
    EASY("Easy"),
    NORMAL("Normal"),
    HARD("Hard"),
    EXPERT("Expert"),
    EXPERT_PLUS("ExpertPlus");

    private static final Map<String, Difficulty> DISPLAY_NAME_MAP
            = Arrays.stream(Difficulty.values())
                    .collect(Collectors.collectingAndThen(
                            Collectors.toMap(Difficulty::getDisplayName, Function.identity()),
                            Collections::unmodifiableMap));

    @NonNull
    private final String displayName;

    public static Optional<Difficulty> fromString(final String name) {
        Difficulty difficulty = DISPLAY_NAME_MAP.get(name);
        return Optional.ofNullable(difficulty);
    }

    @JsonCreator
    public static Difficulty fromStringUnsafe(final String name) {
        Difficulty difficulty = fromString(name).orElseThrow(() -> {
            String message = String.format("Unknown Difficulty [ %s ].", name);
            throw new IllegalArgumentException(message);
        });

        return difficulty;
    }
}
