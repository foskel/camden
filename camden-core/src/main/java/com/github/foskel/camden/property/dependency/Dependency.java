package com.github.foskel.camden.property.dependency;

import com.github.foskel.camden.property.Property;

public final class Dependency<T extends Property<?>> {
    private final T target;
    private final DependencyFilter<T> filter;

    private Dependency(T target, DependencyFilter<T> filter) {
        this.target = target;
        this.filter = filter;
    }

    public static <T extends Property<?>> Dependency<T> of(T target, DependencyFilter<T> filter) {
        return new Dependency<>(target, filter);
    }

    public Property<?> getTarget() {
        return this.target;
    }

    public DependencyFilter<?> getFilter() {
        return this.filter;
    }

    public boolean matches() {
        return this.filter.test(this.target);
    }
}