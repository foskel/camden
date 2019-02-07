package com.github.foskel.camden.property.properties.number;

import com.github.foskel.camden.property.dependency.Dependency;
import com.github.foskel.camden.property.properties.BooleanProperty;
import com.github.foskel.camden.value.ValueChangeListener;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Fred
 * @since 4/23/2017
 */
public final class NumberPropertyBuilder<T extends Number & Comparable<T>> {
    private final Set<ValueChangeListener<T>> valueChangeListeners = new HashSet<>();
    private final String propertyName;
    private T minimumValue;
    private T maximalValue;
    private Dependency<?> dependency;
    private T defaultValue;

    public NumberPropertyBuilder(String propertyName) {
        this.propertyName = propertyName;
    }

    public NumberPropertyBuilder<T> defaultValue(T defaultValue) {
        this.defaultValue = defaultValue;

        return this;
    }

    public NumberPropertyBuilder<T> minValue(T minimumValue) {
        this.minimumValue = minimumValue;

        return this;
    }

    public NumberPropertyBuilder<T> maxValue(T maximalValue) {
        this.maximalValue = maximalValue;

        return this;
    }

    public NumberPropertyBuilder<T> listener(ValueChangeListener<T> listener) {
        this.valueChangeListeners.add(listener);

        return this;
    }

    public NumberPropertyBuilder<T> dependency(Dependency<?> dependency) {
        this.dependency = dependency;

        return this;
    }

    public NumberProperty<T> build() {
        Objects.requireNonNull(this.defaultValue, "defaultValue");
        Objects.requireNonNull(this.minimumValue, "minimumValue");
        Objects.requireNonNull(this.maximalValue, "maximalValue");

        NumberProperty<T> result = new NumberProperty<>(
                this.propertyName,
                this.defaultValue,
                this.minimumValue,
                this.maximalValue
        );

        if (this.dependency != null) {
            result.setDependency(this.dependency);
        }

        this.valueChangeListeners.forEach(result::addListener);

        return result;
    }
}