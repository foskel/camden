package com.github.foskel.camden.property.dependency;

import com.github.foskel.camden.property.Property;

import java.util.function.Predicate;

@FunctionalInterface
public interface DependencyFilter<T extends Property<?>> extends Predicate<T> {

    /**
     * Applies this function to the given argument.
     *
     * @param property the property
     * @return the function result
     */
    @Override
    boolean test(T property);
}