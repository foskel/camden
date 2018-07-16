package com.github.foskel.camden.property.properties.selectable;

import com.github.foskel.camden.property.AbstractProperty;
import com.github.foskel.camden.util.Strings;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public final class SelectableProperty<T> extends AbstractProperty<T> {
    private final Set<T> options;
    private final Function<T, String> nameSupplier;
    private final InputMatchType matchType;

    SelectableProperty(String name,
                       T initialElement,
                       Function<T, String> nameSupplier,
                       InputMatchType matchType) {
        super(name, initialElement);

        this.nameSupplier = nameSupplier;
        this.options = new HashSet<>();
        this.matchType = matchType;
    }

    @Override
    public String getStringValue() {
        return this.getName(this.value);
    }

    @Override
    public boolean setValueParsingInput(String input) {
        return this.options.stream()
                .filter(this.filterByMatchType(input, this.matchType))
                .anyMatch(this::setValue);
    }

    public boolean registerOption(T option) {
        return this.options.add(option);
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
                return option -> this.getName(option).equals(input);
            case CASE_INSENSITIVE_EQUALITY:
                return option -> this.getName(option).equalsIgnoreCase(input);
            case STARTS_WITH_CASE_SENSITIVE:
                return option -> this.getName(option).startsWith(input);
            case STARTS_WITH:
                return option -> Strings.startsWithIgnoreCase(this.getName(option), input);
            default:
                return option -> false;
        }
    }

    private String getName(T option) {
        return this.nameSupplier.apply(option);
    }
}