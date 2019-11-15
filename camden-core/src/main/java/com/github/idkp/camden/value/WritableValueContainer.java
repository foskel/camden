package com.github.idkp.camden.value;

public interface WritableValueContainer<T> {
    boolean setValue(T value);

    void reset();
}
