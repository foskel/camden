package com.github.idkp.camden.value;

public interface ObservableValueContainer<T> extends ReadWriteValueContainer<T> {
    boolean addListener(ValueChangeListener<T> listener);

    boolean removeListener(ValueChangeListener<T> listener);

    void clearListeners();
}