package com.github.idkp.camden.property.scan;

import com.github.idkp.camden.property.Property;

import java.util.Collection;

/**
 * @since 5/16/2017
 */
public interface PropertyScanningStrategy {
    Collection<Property<?>> scan(Object source);
}