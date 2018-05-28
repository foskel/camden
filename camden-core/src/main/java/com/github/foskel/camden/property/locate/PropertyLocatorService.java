package com.github.foskel.camden.property.locate;

import com.github.foskel.camden.property.Property;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by F on 7/16/2017.
 */
public interface PropertyLocatorService {
    Optional<Property<?>> findProperty(Object container, String identifier);

    Collection<Property<?>> findProperties(Object container,
                                           Class<?> propertyType,
                                           boolean strict);
}