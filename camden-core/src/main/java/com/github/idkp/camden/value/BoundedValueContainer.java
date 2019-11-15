package com.github.idkp.camden.value;

public interface BoundedValueContainer<T> extends ObservableValueContainer<T> {
    void bind(ObservableValueContainer<T> valueContainer);

    void unbind();

    boolean isBound();
}