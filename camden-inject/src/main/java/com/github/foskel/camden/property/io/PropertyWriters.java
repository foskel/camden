package com.github.foskel.camden.property.io;

import java.util.HashMap;
import java.util.Map;

public final class PropertyWriters {
    private static final Map<String, PropertyWriter> PROPERTY_WRITERS = new HashMap<>();

    public static void register(PropertyWriter writer) {
        PROPERTY_WRITERS.put(writer.getFileExtension(), writer);
    }

    public static void unregister(PropertyWriter writer) {
        PROPERTY_WRITERS.remove(writer.getFileExtension());
    }

    public static PropertyWriter find(String extension) {
        return PROPERTY_WRITERS.get(extension);
    }
}
