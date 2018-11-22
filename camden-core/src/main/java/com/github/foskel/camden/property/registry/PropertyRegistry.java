package com.github.foskel.camden.property.registry;

import com.github.foskel.camden.property.Property;
import com.github.foskel.camden.property.scan.PropertyScanningStrategy;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Fred
 * @since 5/16/2017
 */
public interface PropertyRegistry {
    boolean registerWith(Object container, PropertyScanningStrategy scanningStrategy);

    boolean unregister(Object container);

    boolean unregisterIf(Object container, Predicate<Property<?>> condition);

    boolean unregisterIf(Predicate<Property<?>> condition);

    boolean has(Object container);

    Collection<Property<?>> findProperties(Object container);
    
    Map<Object, Collection<Property<?>>> findAllProperties();

    void clear();
}