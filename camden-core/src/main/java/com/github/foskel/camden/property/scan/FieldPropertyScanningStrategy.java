package com.github.foskel.camden.property.scan;

import com.github.foskel.camden.property.Property;
import com.github.foskel.camden.annotations.Exclude;
import com.github.foskel.camden.annotations.Propertied;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Fred
 * @since 7/16/2017
 */
//TODO: Create a separate FieldPropertyExtractor to handle different type of fields/annotations presence?
public enum FieldPropertyScanningStrategy implements PropertyScanningStrategy {
    INSTANCE;

    @Override
    public Collection<Property<?>> scan(Object source) {
        if (!shouldScan(source)) {
            return Collections.emptyList();
        }

        Class<?> sourceType = source.getClass();
        Set<Property<?>> properties = new HashSet<>();

        if (sourceType.isAnnotationPresent(Propertied.class)) {
            properties.addAll(extractProperties(source));
        }

        properties.addAll(recursivelyExtractPropertiesFromFieldsOf(source));

        return Collections.unmodifiableSet(properties);
    }

    private static List<Property<?>> recursivelyExtractPropertiesFromFieldsOf(Object parentSource) {
        List<Object> fieldValues = findAllPropertyContainingFields(parentSource);
        List<Property<?>> properties = extractProperties(fieldValues.stream());

        fieldValues.forEach(fieldSource -> {
            properties.addAll(recursivelyExtractPropertiesFromFieldsOf(fieldSource));
        });

        return properties;
    }

    private static List<Object> findAllPropertyContainingFields(Object source) {
        Class<?> parentSourceType = source.getClass();
        Field[] fields = parentSourceType.getDeclaredFields();

        return Arrays.stream(fields)
                .filter(declaredField -> declaredField.isAnnotationPresent(Propertied.class))
                .peek(FieldPropertyScanningStrategy::ensureAccessibility)
                .map(declaredField -> getValueOf(declaredField, source))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static List<Property<?>> extractProperties(Stream<Object> sources) {
        List<Property<?>> properties = new ArrayList<>();

        sources.forEach(source -> {
            if (source instanceof Collection) {
                Collection<?> collectionFieldSource = (Collection<?>) source;

                properties.addAll(extractProperties((Stream<Object>) collectionFieldSource.stream()));

                return;
            }

            List<Property<?>> scannedPropertiesForFieldSource = extractProperties(source);

            properties.addAll(scannedPropertiesForFieldSource);
        });

        return properties;
    }

    private static List<Property<?>> extractProperties(Object source) {
        Class<?> sourceType = source.getClass();
        Field[] internallyDeclaredFieldsOnSourceElement =
                sourceType.getDeclaredFields();
        Stream<Field> declaredFieldsOnSourceElement = Arrays.stream(
                internallyDeclaredFieldsOnSourceElement);

        return extractProperties(declaredFieldsOnSourceElement, source);
    }

    @SuppressWarnings("unchecked")
    private static List<Property<?>> extractProperties(Stream<Field> fields, Object parent) {
        List<Property> foundProperties = fields
                .filter(FieldPropertyScanningStrategy::shouldAccept)
                .peek(FieldPropertyScanningStrategy::ensureAccessibility)
                .map(field -> getPropertyFrom(field, parent))
                .collect(Collectors.toList());

        return (List<Property<?>>) (List<?>) foundProperties;
    }

    private static Property getPropertyFrom(Field field, Object source) {
        Object fieldValue = getValueOf(field, source);

        //we assume we check for it's nullity and type before.

        return (Property) fieldValue;
    }

    private static Object getValueOf(Field field, Object source) {
        try {
            return field.get(source);
        } catch (IllegalAccessException e) {
            e.printStackTrace();

            return null;
        }
    }
    
    private static void ensureAccessibility(Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    private static boolean shouldAccept(Field field) {
        return Property.class.isAssignableFrom(field.getType()) && !shouldIgnore(field);
    }

    private static boolean shouldScan(Object source) {
        Class<?> sourceType = source.getClass();
        Field[] internallyDeclaredFieldsOnSource = sourceType.getDeclaredFields();
        List<Field> declaredFieldsOnSource = Arrays.asList(internallyDeclaredFieldsOnSource);

        return !declaredFieldsOnSource.isEmpty()
                && !shouldIgnore(sourceType)
                && (sourceType.isAnnotationPresent(Propertied.class) || isPropertiedAnnotationPresentOnAnyField(declaredFieldsOnSource));
    }

    private static boolean isPropertiedAnnotationPresentOnAnyField(List<Field> fields) {
        return fields
                .stream()
                .anyMatch(field -> field.isAnnotationPresent(Propertied.class));
    }

    private static boolean shouldIgnore(Field field) {
        return field.isAnnotationPresent(Exclude.class);
    }

    private static boolean shouldIgnore(Class<?> type) {
        return type.isAnnotationPresent(Exclude.class);
    }
}