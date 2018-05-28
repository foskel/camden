package com.github.foskel.camden.property;

import com.github.foskel.camden.property.locate.PropertyLocatorService;
import com.github.foskel.camden.property.registry.PropertyRegistry;

import java.util.Collection;

/**
 * @author Fred
 * @since 7/16/2017
 */
public interface PropertyManager {
    boolean register(Object container);

    boolean unregister(Object container);

    PropertyRegistry getRegistry();

    PropertyLocatorService getLocator();

    Collection<Property<?>> findProperties(Object container);
}