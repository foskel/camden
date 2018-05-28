package com.github.foskel.camden.property.io;

import com.github.foskel.camden.property.Property;
import com.github.foskel.camden.property.PropertyManager;
import com.github.foskel.camden.property.locate.PropertyLocatorService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class JsonPropertyReader implements PropertyReader {
    private final PropertyManager propertyManager;

    public JsonPropertyReader(PropertyManager propertyManager) {
        this.propertyManager = propertyManager;
    }

    @Override
    public void read(Object container, Path source) {
        Optional<JsonElement> elementResult = getJsonElement(source);

        if (!elementResult.isPresent()) {
            return;
        }

        JsonElement element = elementResult.get();

        if (!element.isJsonObject()) {
            return;
        }

        JsonObject root = element.getAsJsonObject();
        Iterator<Map.Entry<String, JsonElement>> entryIterator = root.entrySet().iterator();

        while (entryIterator.hasNext()) {
            Map.Entry<String, JsonElement> entry = entryIterator.next();
            String propertyIdentifier = entry.getKey();
            PropertyLocatorService propertyLocator = this.propertyManager.getLocator();
            Optional<Property<?>> propertyResult = propertyLocator.findProperty(container, propertyIdentifier);

            if (!propertyResult.isPresent()) {
                entryIterator.remove();

                return;
            }

            Property property = propertyResult.get();

            property.setValueParsingInput(entry.getValue().getAsString());
        }
    }

    //TODO: Replace with Gson deserializer?
    private static Optional<JsonElement> getJsonElement(Path source) {
        Objects.requireNonNull(source, "source");

        if (Files.notExists(source) || Files.isDirectory(source)) {
            return Optional.empty();
        }

        FileReader reader;

        try {
            reader = new FileReader(source.toFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            return Optional.empty();
        }

        JsonElement root = new JsonParser().parse(reader);

        return Optional.of(root);
    }
}
