package com.github.foskel.camden.property.locate;

import com.github.foskel.camden.property.Property;

import java.util.Collection;
import java.util.Map;

public interface PropertyLocatorServiceProvider {
    PropertyLocatorService createPropertyLocator(Map<Object, Collection<Property<?>>> properties);
}