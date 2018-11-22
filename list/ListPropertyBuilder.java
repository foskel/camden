package com.github.foskel.camden.property.properties.list;

import java.util.ArrayList;
import java.util.List;

public final class ListPropertyBuilder<E> {
    private final String name;
    private final List<String> elements;

    public ListPropertyBuilder(String name) {
        this.name = name;
        this.elements = new ArrayList<>();
    }

    public ListPropertyBuilder(String name,
                               List<String> elements) {
        this.name = name;
        this.elements = elements;
    }

    public ListPropertyBuilder<E> withElement(E element) {
        if (!this.elements.contains(element.toString())) {
            this.elements.add(element.toString());
        }

        return this;
    }

    public ListProperty build() {
        return new ListProperty(this.name, this.elements);
    }
}