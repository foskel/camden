package com.github.foskel.camden.property.properties;

import com.github.foskel.camden.property.AbstractProperty;
import com.github.foskel.camden.util.Strings;

/**
 * Created by F on 6/26/2017.
 */
public final class EnumProperty<T extends Enum<T>> extends AbstractProperty<T> {
    public EnumProperty(String name, T initialValue) {
        super(name, initialValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean setValueParsingInput(String input) {
        for (Enum constant : this.value.getDeclaringClass().getEnumConstants()) {
            if (Strings.startsWithIgnoreCase(constant.name(), input)) {
                return super.setValue((T) constant);
            }
        }

        return false;
    }
}