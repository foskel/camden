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
import com.google.inject.name.Names;

import java.util.Collections;
import java.util.List;

public final class PropertiesModule extends AbstractModule {
    private final List<Class<? extends PropertyReader>> readerTypes;
    private final List<Class<? extends PropertyWriter>> writerTypes;
    private final Class<? extends PropertyWriter> defaultWriter;

    public PropertiesModule(List<Class<? extends PropertyReader>> readerTypes, List<Class<? extends PropertyWriter>> writerTypes, Class<? extends PropertyWriter> defaultWriter) {
        this.readerTypes = readerTypes;
        this.writerTypes = writerTypes;
        this.defaultWriter = defaultWriter;
    }

    public PropertiesModule() {
        this(Collections.emptyList(), Collections.emptyList(), null);
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
        if (!this.readerTypes.isEmpty()) {
            Multibinder<PropertyReader> readersMultibinder = Multibinder.newSetBinder(binder(),
                    PropertyReader.class);

            for (Class<? extends PropertyReader> readerType : readerTypes) {
                readersMultibinder.addBinding().to(readerType);
            }
        }
    }

    private void bindWriters() {
        if (this.defaultWriter != null) {
            this.bind(PropertyWriter.class).to(defaultWriter);
        }

        if (!this.writerTypes.isEmpty()) {
            Multibinder<PropertyWriter> writersMultibinder = Multibinder.newSetBinder(binder(),
                    PropertyWriter.class);

            for (Class<? extends PropertyWriter> writerType : writerTypes) {
                writersMultibinder.addBinding().to(writerType);
            }
        }
    }
}