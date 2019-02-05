package com.github.foskel.camden.guice;

import com.github.foskel.camden.property.io.PropertyReader;
import com.github.foskel.camden.property.io.PropertyWriter;
import com.github.foskel.camden.property.registry.PropertyRegistry;
import com.github.foskel.camden.property.registry.StandardPropertyRegistry;
import com.github.foskel.camden.property.scan.FieldPropertyScanningStrategy;
import com.github.foskel.camden.property.scan.PropertyScanningStrategy;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

import java.util.Collections;
import java.util.List;

public final class PropertiesModule extends AbstractModule {
    private final List<Class<? extends PropertyReader>> readerTypes;
    private final List<Class<? extends PropertyWriter>> writerTypes;

    public PropertiesModule(List<Class<? extends PropertyReader>> readerTypes, List<Class<? extends PropertyWriter>> writerTypes) {
        this.readerTypes = readerTypes;
        this.writerTypes = writerTypes;
    }

    public PropertiesModule() {
        this(Collections.emptyList(), Collections.emptyList());
    }

    @Override
    protected void configure() {
        this.bind(PropertyRegistry.class)
                .to(StandardPropertyRegistry.class)
                .in(Singleton.class);

        this.bind(PropertyScanningStrategy.class).to(FieldPropertyScanningStrategy.class);

        this.bindReaders();
        this.bindWriters();
    }

    private void bindReaders() {
        Multibinder<PropertyReader> readersMultibinder = Multibinder.newSetBinder(binder(),
                PropertyReader.class);

        for (Class<? extends PropertyReader> readerType : readerTypes) {
            readersMultibinder.addBinding().to(readerType);
        }
    }

    private void bindWriters() {
        Multibinder<PropertyWriter> writersMultibinder = Multibinder.newSetBinder(binder(),
                PropertyWriter.class);

        for (Class<? extends PropertyWriter> writerType : writerTypes) {
            writersMultibinder.addBinding().to(writerType);
        }
    }
}