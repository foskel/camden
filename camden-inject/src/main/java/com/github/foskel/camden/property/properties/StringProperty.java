package com.github.foskel.camden.property.properties;

import com.github.foskel.camden.property.AbstractProperty;

/**
 * @author Fred
 * @since 2/21/2017
 */
public final class StringProperty extends AbstractProperty<String> {
    public StringProperty(String label, String initialValue) {
        super(label, initialValue);
    }

    @Override
    public boolean setValueParsingInput(String input) {
        return super.setValue(input);
    }
}