package com.github.foskel.camden.value;

public interface ReadableValueContainer<T> {
    T getValue();

    T getDefaultValue();
}
