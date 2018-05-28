package com.github.foskel.camden.value;

import java.util.function.BiConsumer;

/**
 * @author Fred
 * @since 4/23/2017
 */
@FunctionalInterface
public interface ValueChangeListener<T> extends BiConsumer<T, T> {

    @Override
    void accept(T previous, T current);
}