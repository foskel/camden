package com.github.idkp.camden.property.properties;

import com.github.idkp.camden.property.SimpleProperty;
import com.github.idkp.camden.util.parsing.BooleanParser;

import java.util.Optional;

/**
 * @since 2/21/2017
 */
public final class BooleanProperty extends SimpleProperty<Boolean> {
    public BooleanProperty(String name, Boolean value) {
        super(name, value);
    }

    @Override
    public boolean setValueParsingInput(String input) {
        Optional<Boolean> value = BooleanParser.parse(input);

        if (!value.isPresent()) {
            return false;
        }

        super.setValue(value.get());

        return true;
    }

    public boolean toggle() {
        return this.value = !this.value;
    }

    public boolean isEnabled() {
        return this.value;
    }
}