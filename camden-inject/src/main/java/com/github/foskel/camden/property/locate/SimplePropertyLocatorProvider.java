package com.github.foskel.camden.property.locate;

import com.github.foskel.camden.property.Property;

import java.util.Collection;
import java.util.Map;

public final class SimplePropertyLocatorProvider implements PropertyLocatorServiceProvider {

    @Override
    public PropertyLocatorService createPropertyLocator(Map<Object, Collection<Property<?>>> properties) {
        return new SimplePropertyLocator(properties);
    }
}
