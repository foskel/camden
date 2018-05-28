package com.github.foskel.camden.value;

public interface WritableValueContainer<T> {
    boolean setValue(T value);

    void reset();
}
