package com.github.foskel.camden.property.properties.selectable;

import com.github.foskel.camden.property.SimpleProperty;
import com.github.foskel.camden.util.Strings;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public final class SelectableProperty<T> extends SimpleProperty<T> {
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
        return this.getNameOf(this.value);
    }

    @Override
    public boolean setValueParsingInput(String input) {
        for (T option : options) {
            if (this.filterByMatchType(option, input, this.matchType)) {
                if (!this.setValue(option)) {
                    return false;
                }
            }
        }

        return true;

        /*return this.options.stream()
                .filter(this.filterByMatchType(input, this.matchType))
                .anyMatch(this::setValue);*/
    }

    public boolean registerOption(T option) {
        return this.options.add(option);
    }

    public boolean unregisterOption(T option) {
        return this.options.remove(option);
    }

    public Optional<T> find(String optionName) {
        for (T option : options) {
            if (this.filterByMatchType(option, optionName, InputMatchType.EQUALS)) {
                return Optional.of(option);
            }
        }

        return Optional.empty();

        /*return this.options.stream()
                .filter(this.filterByMatchType(optionName, InputMatchType.EQUALS))
                .findFirst();*/
    }

    public Set<T> findAllOptions() {
        return Collections.unmodifiableSet(this.options);
    }

    public void clearOptions() {
        this.options.clear();
    }

    private boolean filterByMatchType(T option, String input, InputMatchType matchType) {
        switch (matchType) {
            case EQUALS:
                return this.getNameOf(option).equals(input);
            case EQUALS_CASE_INSENSITIVE:
                return this.getNameOf(option).equalsIgnoreCase(input);
            case STARTS_WITH_CASE_SENSITIVE:
                return this.getNameOf(option).startsWith(input);
            case STARTS_WITH:
                return Strings.startsWithIgnoreCase(this.getNameOf(option), input);
            default:
                return false;
        }
    }

    public String getNameOf(T option) {
        return this.nameSupplier.apply(option);
    }
}