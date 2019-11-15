package com.github.idkp.camden.property.io;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class PropertyWriters {
    private final Map<String, PropertyWriter> propertyWriters;

    @Inject
    PropertyWriters(Set<PropertyWriter> propertyWriters) {
        this.propertyWriters = new HashMap<>();

        for (PropertyWriter writer : propertyWriters) {
            this.propertyWriters.put(writer.getFileExtension(), writer);
        }
    }

    public PropertyWriter find(String extension) {
        return propertyWriters.get(extension);
    }
}
