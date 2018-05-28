package com.github.foskel.camden.property;

import com.github.foskel.camden.property.locate.PropertyLocatorService;
import com.github.foskel.camden.property.locate.SimplePropertyLocator;
import com.github.foskel.camden.property.registry.PropertyRegistry;
import com.github.foskel.camden.property.registry.StandardPropertyRegistry;
import com.github.foskel.camden.property.scan.PropertyScanningStrategy;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * @author Fred
 * @since 7/16/2017
 */
public final class StandardPropertyManager implements PropertyManager {
    private final PropertyRegistry registry;
    private final PropertyLocatorService locator;
    private final PropertyScanningStrategy scanningStrategy;

    public StandardPropertyManager(PropertyScanningStrategy scanningStrategy) {
        this.registry = new StandardPropertyRegistry();
        this.locator = new SimplePropertyLocator(this.registry.findAllProperties());
        this.scanningStrategy = scanningStrategy;
    }

    @Override
    public boolean register(Object container) {
        Objects.requireNonNull(container, "container");

        return this.registry.registerWith(container, this.scanningStrategy);
    }

    @Override
    public boolean unregister(Object container) {
        Objects.requireNonNull(container, "container");

        return this.registry.unregister(container);
    }

    @Override
    public PropertyRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public PropertyLocatorService getLocator() {
        return this.locator;
    }

    @Override
    public Collection<Property<?>> findProperties(Object container) {
        Objects.requireNonNull(container, "container");

        Collection<Property<?>> result = this.registry.findAllProperties().get(container);

        return result == null
                ? Collections.emptyList()
                : result;
    }
}