package com.github.foskel.camden.property.locate;

import com.github.foskel.camden.property.Property;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by F on 7/16/2017.
 */
public final class SimplePropertyLocator implements PropertyLocatorService {
    private final Map<Object, Collection<Property<?>>> properties;

    SimplePropertyLocator(Map<Object, Collection<Property<?>>> properties) {
        this.properties = properties;
    }

    @Override
    public Optional<Property<?>> findProperty(Object container, String identifier) {
        Objects.requireNonNull(identifier, "identifier");

        Collection<Property<?>> properties = this.properties.get(container);

        if (properties == null) {
            return Optional.empty();
        }

        return findProperty(properties, identifier);
    }

    //TODO: Use a Map?
    private static Optional<Property<?>> findProperty(Collection<Property<?>> properties, String identifier) {
        return properties.stream()
                .filter(property -> property.getName().equalsIgnoreCase(identifier))
                .findFirst();
    }

    @Override
    public List<Property<?>> findProperties(Object container,
                                            Class<?> propertyType,
                                            boolean strict) {
        Objects.requireNonNull(container, "container");
        Objects.requireNonNull(propertyType, "propertyType");

        Collection<Property<?>> properties = this.properties.get(container);

        if (properties == null) {
            return Collections.emptyList();
        }

        return properties
                .stream()
                .filter(property -> strict
                        ? property.getClass() == propertyType
                        : property.getClass().isAssignableFrom(propertyType))
                .collect(Collectors.toList());
    }
}