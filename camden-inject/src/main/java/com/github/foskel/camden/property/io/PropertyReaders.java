package com.github.foskel.camden.property.io;

import javax.inject.Inject;
import java.util.Map;

public final class PropertyReaders {
    private final Map<String, PropertyReader> propertyReaders;

    @Inject
    PropertyReaders(Map<String, PropertyReader> propertyReaders) {
        this.propertyReaders = propertyReaders;
    }

    public PropertyReader find(String extension) {
        return propertyReaders.get(extension);
    }
}
