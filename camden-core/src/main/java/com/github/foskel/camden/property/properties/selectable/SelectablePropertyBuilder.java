package com.github.foskel.camden.property.properties.selectable;

import com.github.foskel.camden.value.ValueChangeListener;
import com.github.foskel.douglas.core.traits.Named;

import java.util.*;

public final class SelectablePropertyBuilder<T extends Named> {
    private final Set<ValueChangeListener<T>> listeners = new HashSet<>();
    private final Set<T> options = new HashSet<>();
    private final String name;
    private InputMatchType matchType = InputMatchType.EQUALITY;
    private T defaultValue;

    public SelectablePropertyBuilder(String name) {
        this.name = name;
    }

    public SelectablePropertyBuilder<T> withListener(ValueChangeListener<T> listener) {
        Objects.requireNonNull(listener, "listener");

        this.listeners.add(listener);

        return this;
    }

    public SelectablePropertyBuilder<T> withMatchType(InputMatchType matchType) {
        Objects.requireNonNull(matchType, "matchType");

        this.matchType = matchType;

        return this;
    }

    public SelectablePropertyBuilder<T> withDefault(String defaultName) {
        if (this.defaultValue != null) {
            throw new IllegalStateException("The default element was already set " +
                    "(tried to call " +
                    "SelectablePropertyBuilder#withDefault twice?).");
        }

        Objects.requireNonNull(defaultName, "defaultName");

        T foundOption = this.options.stream()
                .filter(option -> option.getName().equals(defaultName))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Unable to set default option. No options " +
                        "matching " +
                        "\"" + defaultName + "\""));

        this.withDefault(foundOption);

        return this;
    }

    private void withDefault(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public SelectablePropertyBuilder<T> copyOptionsFrom(Collection<T> options) {
        if (!options.isEmpty()) {
            this.options.addAll(options);
        }

        return this;
    }

    public SelectablePropertyBuilder<T> registerOption(T option) {
        Objects.requireNonNull(option, "option");

        this.options.add(option);

        return this;
    }

    public SelectableProperty<T> build() {
        if (this.options.isEmpty() || this.defaultValue == null) {
            throw new IllegalStateException("Please finish building the property before invoking SelectablePropertyBuilder#build.");
        }

        SelectableProperty<T> builtProperty = new SelectableProperty<>(this.name,
                this.defaultValue,
                this.matchType);

        this.options.forEach(builtProperty::registerOption);
        this.listeners.forEach(builtProperty::addListener);

        return builtProperty;
    }
}