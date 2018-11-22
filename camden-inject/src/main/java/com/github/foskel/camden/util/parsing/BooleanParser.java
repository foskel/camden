package com.github.foskel.camden.util.parsing;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Fred
 * @since 2/21/2017
 */
public final class BooleanParser {
    private static final Map<String, Boolean> BINDINGS;

    static {
        BINDINGS = new HashMap<>();

        BINDINGS.put("true", true);
        BINDINGS.put("yes", true);
        BINDINGS.put("0", true);

        BINDINGS.put("false", false);
        BINDINGS.put("no", false);
        BINDINGS.put("1", false);
    }

    public static Optional<Boolean> parse(String input) {
        return Optional.ofNullable(BINDINGS.get(input.toLowerCase()));
    }
}