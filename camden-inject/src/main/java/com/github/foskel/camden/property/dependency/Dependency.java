package com.github.foskel.camden.property.dependency;

import com.github.foskel.camden.property.Property;

import java.util.function.Predicate;

public final class Dependency<T extends Property<?>> {
    private final T target;
    private final Predicate<T> filter;

    public Dependency(T target, Predicate<T> filter) {
        this.target = target;
        this.filter = filter;
    }

    public T getTarget() {
        return this.target;
    }

    public Predicate<T> getFilter() {
        return this.filter;
    }

    public boolean matches() {
        return this.filter.test(this.target);
    }
}