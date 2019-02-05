package com.github.foskel.camden.property.io;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class PropertyReaders {
    private final Map<String, PropertyReader> propertyReaders;

    @Inject
    PropertyReaders(Set<PropertyReader> propertyReaders) {
        this.propertyReaders = new HashMap<>();

        for (PropertyReader reader : propertyReaders) {
            this.propertyReaders.put(reader.getFileExtension(), reader);
        }
    }

    public PropertyReader find(String extension) {
        return propertyReaders.get(extension);
    }
}
