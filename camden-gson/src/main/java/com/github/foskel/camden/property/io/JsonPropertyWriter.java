package com.github.foskel.camden.property.io;

import com.github.foskel.camden.property.Property;
import com.github.foskel.camden.property.registry.PropertyRegistry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

public final class JsonPropertyWriter implements PropertyWriter {
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private final PropertyRegistry propertyRegistry;

    public JsonPropertyWriter(PropertyRegistry propertyRegistry) {
        this.propertyRegistry = propertyRegistry;
    }

    @Override
    public void write(Object container, Path destination) throws IOException {
        JsonObject root = new JsonObject();
        Collection<Property<?>> properties = this.propertyRegistry.findProperties(container);

        properties.forEach(property -> root.addProperty(property.getName(), property.getStringValue()));

        Files.write(destination, this.gson.toJson(root).getBytes("UTF-8"));
    }
}
