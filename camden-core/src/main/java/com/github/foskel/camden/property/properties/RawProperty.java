package com.github.foskel.camden.property.properties;

import com.github.foskel.camden.property.AbstractProperty;
import com.github.foskel.camden.property.dependency.Dependency;

public final class RawProperty<T> extends AbstractProperty<T> {
    public RawProperty(String name, T initialValue, Dependency<?> dependency) {
        super(name, initialValue, dependency);
    }

    @Override
    public boolean setValueParsingInput(String input) {
        return false;
    }
}
