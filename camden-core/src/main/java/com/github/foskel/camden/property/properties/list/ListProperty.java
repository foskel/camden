package com.github.foskel.camden.property.properties.list;

import com.github.foskel.camden.property.SimpleProperty;

import java.util.Arrays;
import java.util.List;

//TODO: Remove?
public final class ListProperty extends SimpleProperty<List<String>> {
    private static final String LIST_TO_STRING_PREFIX = "[";
    private static final String LIST_TO_STRING_SUFFIX = "]";

    ListProperty(String name, List<String> initialValue) {
        super(name, initialValue);
    }

    @Override
    public String getStringValue() {
        String stringValue = this.value.toString();

        if (stringValue.startsWith(LIST_TO_STRING_PREFIX)) {
            stringValue = stringValue.substring(1);
        }

        if (stringValue.endsWith(LIST_TO_STRING_SUFFIX)) {
            stringValue = stringValue.substring(0, stringValue.length() - 1);
        }

        return stringValue;
    }

    @Override
    public boolean setValueParsingInput(String input) {
        return super.setValue(Arrays.asList(input.split("\\s*,\\s*")));
    }
}