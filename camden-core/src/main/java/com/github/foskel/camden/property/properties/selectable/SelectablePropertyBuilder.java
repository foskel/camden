package com.github.foskel.camden.property.properties.selectable;

import com.github.foskel.camden.value.ValueChangeListener;

import java.util.*;
import java.util.function.Function;

public final class SelectablePropertyBuilder<T> {
    private final Set<ValueChangeListener<T>> listeners = new HashSet<>();
    private final Set<T> options = new HashSet<>();
    private final String name;
    private Function<T, String> nameSupplier;
    private InputMatchType matchType = InputMatchType.EQUALS;
    private T defaultValue;

    public SelectablePropertyBuilder(String name) {
        this.name = name;
    }

    public SelectablePropertyBuilder<T> listener(ValueChangeListener<T> listener) {
        Objects.requireNonNull(listener, "listener");

        this.listeners.add(listener);

        return this;
    }

    public SelectablePropertyBuilder<T> matchType(InputMatchType matchType) {
        Objects.requireNonNull(matchType, "matchType");

        this.matchType = matchType;

        return this;
    }

    public SelectablePropertyBuilder<T> nameSupplier(Function<T, String> nameSupplier) {
        Objects.requireNonNull(nameSupplier, "nameSupplier");

        this.nameSupplier = nameSupplier;

        return this;
    }

    public SelectablePropertyBuilder<T> defaultOption(String name) {
        if (this.defaultValue != null) {
            throw new IllegalStateException("The default element was already set " +
                    "(tried to call " +
                    "SelectablePropertyBuilder#defaultOption twice?).");
        }

        Objects.requireNonNull(name, "name");

        for (T option : options) {
            if (this.nameSupplier.apply(option).equals(name)) {
                return this.defaultOption(option);
            }
        }

        throw new NoSuchElementException("Unable to set default option. No options matching \"" + name + "\"");

        /*T foundOption = this.options.stream()
                .filter(option -> this.nameSupplier.apply(option).equals(name))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Unable to set default option. No options " +
                        "matching " +
                        "\"" + name + "\""));*/

        //return this.defaultOption(foundOption);
    }

    public SelectablePropertyBuilder<T> defaultOption(T defaultValue) {
        this.defaultValue = defaultValue;

        return this;
    }

    public SelectablePropertyBuilder<T> addOptions(Collection<T> options) {
        if (!options.isEmpty()) {
            this.options.addAll(options);
        }

        return this;
    }

    public SelectablePropertyBuilder<T> addOption(T option) {
        Objects.requireNonNull(option, "option");

        this.options.add(option);

        return this;
    }

    public SelectableProperty<T> build() {
        if (this.options.isEmpty() || this.defaultValue == null) {
            throw new IllegalStateException("You must finish building the property before invoking SelectablePropertyBuilder#build.");
        }

        SelectableProperty<T> builtProperty = new SelectableProperty<>(this.name,
                this.defaultValue,
                this.nameSupplier,
                this.matchType);

        this.options.forEach(builtProperty::registerOption);
        this.listeners.forEach(builtProperty::addListener);

        return builtProperty;
    }
}