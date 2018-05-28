package com.github.foskel.camden.guice;

import com.github.foskel.camden.property.PropertyManager;
import com.github.foskel.camden.property.StandardPropertyManager;
import com.github.foskel.camden.property.locate.PropertyLocatorServiceProvider;
import com.github.foskel.camden.property.locate.SimplePropertyLocatorProvider;
import com.github.foskel.camden.property.registry.PropertyRegistry;
import com.github.foskel.camden.property.registry.StandardPropertyRegistry;
import com.github.foskel.camden.property.scan.FieldPropertyScanningStrategy;
import com.github.foskel.camden.property.scan.PropertyScanningStrategy;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public final class PropertiesModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(PropertyRegistry.class)
                .to(StandardPropertyRegistry.class)
                .in(Singleton.class);

        this.bind(PropertyLocatorServiceProvider.class).to(SimplePropertyLocatorProvider.class);
        this.bind(PropertyScanningStrategy.class).to(FieldPropertyScanningStrategy.class);

        this.bind(PropertyManager.class)
                .to(StandardPropertyManager.class)
                .in(Singleton.class);
    }
}