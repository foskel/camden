package com.github.idkp.camden.property.properties;

import com.github.idkp.camden.property.SimpleProperty;

/**
 * @since 2/21/2017
 */
public final class StringProperty extends SimpleProperty<String> {
    public StringProperty(String label, String initialValue) {
        super(label, initialValue);
    }

    @Override
    public boolean setValueParsingInput(String input) {
        return super.setValue(input);
    }
}