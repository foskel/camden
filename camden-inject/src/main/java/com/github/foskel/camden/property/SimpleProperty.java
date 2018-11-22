package com.github.foskel.camden.property;

import com.github.foskel.camden.property.dependency.Dependency;
import com.github.foskel.camden.value.ObservableValueContainer;
import com.github.foskel.camden.value.ValueChangeListener;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Abstract implementation of {@link Property}
 */
public class SimpleProperty<T> implements Property<T> {
    private final Queue<ValueChangeListener<T>> valueChangeListeners = new LinkedBlockingDeque<>();
    private final String name;
    private final T defaultValue;
    protected T value;

    private Dependency<?> dependency;

    private final ValueChangeListener<T> bindingListener;
    private ObservableValueContainer<T> binding;

    public SimpleProperty(String name, T initialValue, Dependency<?> dependency) {
        this(name, initialValue);

        this.dependency = dependency;
    }

    public SimpleProperty(String name, T initialValue) {
        this.name = name;
        this.value = initialValue;
        this.defaultValue = initialValue;
        this.bindingListener = (oldValue, newValue) -> this.setValue(newValue);
    }

    @Override
    public boolean setValue(T value) {
        T oldValue = this.value;

        this.value = value;

        while (!this.valueChangeListeners.isEmpty()) {
            ValueChangeListener<T> valueChangeListener = this.valueChangeListeners.poll();

            valueChangeListener.accept(oldValue, this.value);
        }

        return true;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public String getStringValue() {
        return String.valueOf(this.value);
    }

    @Override
    public T getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public void reset() {
        this.value = this.defaultValue;

        this.clearListeners();
        this.unbind();
    }

    @Override
    public boolean setValueParsingInput(String input) {
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Dependency<?> getDependency() {
        return this.dependency;
    }

    @Override
    public void setDependency(Dependency<?> dependency) {
        this.dependency = dependency;
    }

    @Override
    public void bind(ObservableValueContainer<T> valueContainer) {
        this.binding = valueContainer;

        this.binding.addListener(this.bindingListener);
    }

    @Override
    public void unbind() {
        if (!this.isBound()) {
            return;
        }

        this.binding.removeListener(this.bindingListener);
    }

    @Override
    public boolean isBound() {
        return this.binding != null;
    }

    @Override
    public boolean addListener(ValueChangeListener<T> listener) {
        return this.valueChangeListeners.add(listener);
    }

    @Override
    public boolean removeListener(ValueChangeListener<T> listener) {
        return this.valueChangeListeners.remove(listener);
    }

    @Override
    public void clearListeners() {
        this.valueChangeListeners.clear();
    }

    @Override
    public String toString() {
        return getClass().getName() + "{name=" + this.name +
                ", defaultValue=" + this.defaultValue +
                ", value=" + this.value +
                ", binding=" + this.binding +
                ", dependency=" + this.dependency +
                ", changeListeners=" + this.valueChangeListeners + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Property)) {
            return false;
        }

        Property otherProperty = (Property) obj;

        return otherProperty.getName().equals(this.name) && otherProperty.getValue().equals(this.value);
        //&& Objects.equals(otherProperty.getDefaultValue(), this.defaultValue)
        //&& Objects.equals(otherProperty.getValue(), this.value);
    }

    @Override
    public int hashCode() {
        int hash = 1;

        hash = 31 * hash + this.name.hashCode();
        hash = 31 * hash + this.value.hashCode();
        //hash = 31 * hash + this.defaultValue.hashCode();
        //hash = 31 * hash + this.value.hashCode();

        return hash;
    }
}