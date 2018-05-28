package com.github.foskel.camden.property.properties.number;

import com.github.foskel.camden.property.dependency.Dependency;
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
    private final T minimumValue;
    private final T maximalValue;
    private Dependency<?> dependency;
    private T defaultValue;

    public NumberPropertyBuilder(String propertyName,
                                 T minimumValue,
                                 T maximalValue) {
        this.propertyName = propertyName;
        this.minimumValue = minimumValue;
        this.maximalValue = maximalValue;
    }

    public NumberPropertyBuilder<T> withDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;

        return this;
    }

    public NumberPropertyBuilder<T> withListener(ValueChangeListener<T> listener) {
        this.valueChangeListeners.add(listener);

        return this;
    }

    public NumberPropertyBuilder<T> withDependency(Dependency<?> dependency) {
        this.dependency = dependency;

        return this;
    }

    public NumberProperty<T> build() {
        Objects.requireNonNull(this.defaultValue, "defaultValue");

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