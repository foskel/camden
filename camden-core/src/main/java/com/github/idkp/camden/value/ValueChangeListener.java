package com.github.idkp.camden.value;

import java.util.function.BiConsumer;

/**
 * @since 4/23/2017
 */
@FunctionalInterface
public interface ValueChangeListener<T> extends BiConsumer<T, T> {

    @Override
    void accept(T previous, T current);
}