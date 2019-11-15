package com.github.idkp.camden.property.io;

import java.util.HashMap;
import java.util.Map;

public final class PropertyReaders {
    private static final Map<String, PropertyReader> PROPERTY_READERS = new HashMap<>();

    public static void register(PropertyReader reader) {
        PROPERTY_READERS.put(reader.getFileExtension(), reader);
    }

    public static void unregister(PropertyReader reader) {
        PROPERTY_READERS.remove(reader.getFileExtension());
    }

    public static PropertyReader find(String extension) {
        return PROPERTY_READERS.get(extension);
    }
}
