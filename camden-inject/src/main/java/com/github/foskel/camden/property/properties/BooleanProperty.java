package com.github.foskel.camden.property.properties;

import com.github.foskel.camden.property.AbstractProperty;
import com.github.foskel.camden.util.parsing.BooleanParser;

import java.util.Optional;

/**
 * @author Fred
 * @since 2/21/2017
 */
public final class BooleanProperty extends AbstractProperty<Boolean> {
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