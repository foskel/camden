package com.github.idkp.camden.property;

import com.github.idkp.camden.property.dependency.Dependency;
import com.github.idkp.camden.value.BoundedValueContainer;

/**
 * @since 3/24/2017
 */
public interface Property<T> extends BoundedValueContainer<T> {
    boolean setValueParsingInput(String input);

    String getName();

    String getStringValue();

    Dependency<?> getDependency();

    void setDependency(Dependency<?> dependency);
}