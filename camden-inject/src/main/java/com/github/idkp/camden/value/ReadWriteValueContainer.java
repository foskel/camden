package com.github.idkp.camden.value;

public interface ReadWriteValueContainer<T> extends ReadableValueContainer<T>, WritableValueContainer<T> {

    @Override
    T getValue();

    @Override
    T getDefaultValue();

    @Override
    boolean setValue(T value);

    @Override
    void reset();
}