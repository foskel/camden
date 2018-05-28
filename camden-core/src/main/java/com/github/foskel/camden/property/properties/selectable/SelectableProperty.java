package com.github.foskel.camden.property.properties.selectable;

import com.github.foskel.camden.property.AbstractProperty;
import com.github.foskel.camden.util.Strings;
import com.github.foskel.douglas.core.traits.Named;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public final class SelectableProperty<T extends Named> extends AbstractProperty<T> {
    private final Set<T> options;
    private final InputMatchType matchType;

    SelectableProperty(String name, T initialElement, InputMatchType matchType) {
        super(name, initialElement);

        this.options = new HashSet<>();
        this.matchType = matchType;
    }

    @Override
    public String getStringValue() {
        return this.value.getName();
    }

    @Override
    public boolean setValueParsingInput(String input) {
        return this.options.stream()
                .filter(this.filterByMatchType(input, this.matchType))
                .anyMatch(this::setValue);
    }

    public boolean registerOption(T option) {
        return !this.find(option.getName()).isPresent()
                && this.options.add(option);
    }

    public boolean unregisterOption(T option) {
        return this.options.remove(option);
    }

    public Optional<T> find(String optionName) {
        return this.options.stream()
                .filter(this.filterByMatchType(optionName, InputMatchType.EQUALITY))
                .findFirst();
    }

    public Set<T> findAllOptions() {
        return Collections.unmodifiableSet(this.options);
    }

    public void clearOptions() {
        this.options.clear();
    }

    private Predicate<T> filterByMatchType(String input, InputMatchType matchType) {
        switch (matchType) {
            case EQUALITY:
                return option -> option.getName().equals(input);
            case CASE_INSENSITIVE_EQUALITY:
                return option -> option.getName().equalsIgnoreCase(input);
            case STARTS_WITH_CASE_SENSITIVE:
                return option -> option.getName().startsWith(input);
            case STARTS_WITH:
                return option -> Strings.startsWithIgnoreCase(option.getName(), input);
        }

        return option -> false;
    }
}