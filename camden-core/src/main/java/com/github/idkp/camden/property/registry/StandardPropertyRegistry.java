package com.github.idkp.camden.property.registry;

import com.github.idkp.camden.property.Property;
import com.github.idkp.camden.property.io.PropertyReader;
import com.github.idkp.camden.property.io.PropertyReaders;
import com.github.idkp.camden.property.io.PropertyWriter;
import com.github.idkp.camden.property.io.PropertyWriters;
import com.github.idkp.camden.property.scan.PropertyScanningStrategy;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by F on 7/16/2017.
 */
public final class StandardPropertyRegistry implements PropertyRegistry {
    private final Map<Object, Collection<Property<?>>> properties = new HashMap<>();

    public StandardPropertyRegistry(List<PropertyReader> readers, List<PropertyWriter> writers) {
        for (PropertyReader reader : readers) {
            PropertyReaders.register(reader);
        }

        for (PropertyWriter writer : writers) {
            PropertyWriters.register(writer);
        }
    }

    public StandardPropertyRegistry() {
    }

    @Override
    public boolean registerWith(Object container, PropertyScanningStrategy scanningStrategy) {
        Objects.requireNonNull(container, "container");
        Objects.requireNonNull(scanningStrategy, "scanningStrategy");

        if (this.has(container)) {
            return false;
        }

        Collection<Property<?>> scannedProperties = scanningStrategy.scan(container);

        if (!scannedProperties.isEmpty()) {
            this.properties.put(container, scannedProperties);
        }

        return true;
    }

    @Override
    public boolean unregister(Object container) {
        Objects.requireNonNull(container, "container");

        if (!this.has(container)) {
            return false;
        }

        return this.properties.remove(container) != null;
    }

    @Override
    public boolean unregisterIf(Object container, Predicate<Property<?>> condition) {
        Collection<Property<?>> properties = this.properties.get(container);

        if (properties == null) {
            return false;
        }

        return properties.removeIf(condition);
    }

    @Override
    public boolean unregisterIf(Predicate<Property<?>> condition) {
        Objects.requireNonNull(condition, "condition");

        for (Collection<Property<?>> propertyCollection : this.properties.values()) {
            if (!propertyCollection.removeIf(condition)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean has(Object container) {
        return this.properties.containsKey(container);
    }

    @Override
    public Property<?> findProperty(Object container, String name) {
        Collection<Property<?>> properties = this.properties.get(container);

        if (properties == null) {
            return null;
        }

        for (Property<?> property : properties) {
            if (property.getName().equals(name)) {
                return property;
            }
        }

        return null;
    }

    @Override
    public Collection<Property<?>> findProperties(Object container) {
        return this.properties.get(container);
    }

    @Override
    public Map<Object, Collection<Property<?>>> findAllProperties() {
        return Collections.unmodifiableMap(this.properties);
    }

    @Override
    public void clear() {
        this.properties.clear();
    }
}
