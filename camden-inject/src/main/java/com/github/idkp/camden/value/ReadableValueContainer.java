package com.github.idkp.camden.value;

public interface ReadableValueContainer<T> {
    T getValue();

    T getDefaultValue();
}
