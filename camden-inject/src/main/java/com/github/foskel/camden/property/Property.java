package com.github.foskel.camden.property;

import com.github.foskel.camden.property.dependency.Dependency;
import com.github.foskel.camden.value.BoundedValueContainer;

/**
 * @author Fred
 * @since 3/24/2017
 */
public interface Property<T> extends BoundedValueContainer<T> {
    boolean setValueParsingInput(String input);

    String getName();

    String getStringValue();

    Dependency<?> getDependency();

    void setDependency(Dependency<?> dependency);
}