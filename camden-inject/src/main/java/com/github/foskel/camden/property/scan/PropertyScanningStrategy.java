package com.github.foskel.camden.property.scan;

import com.github.foskel.camden.property.Property;

import java.util.Collection;

/**
 * @author Fred
 * @since 5/16/2017
 */
public interface PropertyScanningStrategy {
    Collection<Property<?>> scan(Object source);
}