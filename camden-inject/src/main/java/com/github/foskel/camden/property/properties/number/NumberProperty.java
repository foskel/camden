package com.github.foskel.camden.property.properties.number;

import com.github.foskel.camden.property.SimpleProperty;
import com.github.foskel.camden.util.parsing.NumberParser;

/**
 * @author Fred
 * @since 2/21/2017
 */

public final class NumberProperty<T extends Number & Comparable<T>> extends SimpleProperty<T> implements Comparable<NumberProperty<T>> {
    private T minimumValue;
    private T maximalValue;

    protected NumberProperty(String name, T value, T minimumValue, T maximalValue) {
        super(name, value);

        this.minimumValue = minimumValue;
        this.maximalValue = maximalValue;
    }

    public static <OT extends Number & Comparable<OT>> NumberProperty<OT> create(String name, OT value, OT minimumValue, OT maximalValue) {
        return new NumberProperty<>(name, value, minimumValue, maximalValue);
    }

    public T getMinimumValue() {
        return this.minimumValue;
    }

    public T getMaximalValue() {
        return this.maximalValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean setValueParsingInput(String input) {
        try {
            Number parsedNumber = NumberParser.parse(input, this.value.getClass());

            this.setValue((T) parsedNumber);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean setValue(T input) {
        if (value.compareTo(maximalValue) > 0) {
            this.value = maximalValue;

            return true;
        } else if (value.compareTo(minimumValue) < 0) {
            this.value = minimumValue;

            return true;
        }

        return super.setValue(input);
    }

    @Override
    public int compareTo(NumberProperty<T> o) {
        return this.value.compareTo(o.getValue());
    }
}