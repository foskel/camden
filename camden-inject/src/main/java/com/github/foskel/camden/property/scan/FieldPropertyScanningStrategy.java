package com.github.foskel.camden.property.scan;

import com.github.foskel.camden.annotations.Exclude;
import com.github.foskel.camden.annotations.Propertied;
import com.github.foskel.camden.annotations.ScanDepth;
import com.github.foskel.camden.property.Property;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Fred
 * @since 7/16/2017
 */
//TODO: Create a separate FieldPropertyExtractor to handle different type of fields/annotations presence?
public final class FieldPropertyScanningStrategy implements PropertyScanningStrategy {
    private static final int DEFAULT_DEPTH = 1;

    private static List<Property<?>> extractPropertiesOfFields(Class<?> type, Object parentSource) {
        List<Object> fieldValues = getPropertyFieldsValues(type, parentSource);
        List<Property<?>> properties = extractAllProperties(fieldValues);

        fieldValues.forEach(fieldSource -> properties.addAll(extractPropertiesOfFields(fieldSource.getClass(), fieldSource)));

        return properties;
    }

    private static List<Object> getPropertyFieldsValues(Class<?> type, Object source) {
        Field[] fields = type.getDeclaredFields();
        List<Object> propertyFieldsValues = new ArrayList<>();

        for (Field declaredField : fields) {
            if (declaredField.isAnnotationPresent(Propertied.class)) {
                ensureAccess(declaredField);

                Object value = getValueOf(declaredField, source);

                propertyFieldsValues.add(value);
            }
        }

        return propertyFieldsValues;
    }

    @SuppressWarnings("unchecked")
    private static List<Property<?>> extractAllProperties(Collection<Object> sources) {
        List<Property<?>> properties = new ArrayList<>();

        for (Object source : sources) {
            if (source instanceof Collection) {
                properties.addAll(extractAllProperties((Collection<Object>) source));

                continue;
            }

            properties.addAll(extractProperties(source.getClass(), source));
        }

        return properties;
    }

    private static List<Property<?>> extractProperties(Class<?> type, Object source) {
        List<Property<?>> properties = new ArrayList<>();

        for (Field field : type.getDeclaredFields()) {
            if (!shouldAccept(field)) {
                continue;
            }

            ensureAccess(field);

            properties.add(getPropertyFrom(field, source));
        }

        return properties;
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

    private static void ensureAccess(Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    private static boolean shouldAccept(Field field) {
        return Property.class.isAssignableFrom(field.getType()) && !field.isAnnotationPresent(Exclude.class);
    }

    private static boolean shouldScan(Object source) {
        Class<?> sourceType = source.getClass();
        Field[] fields = sourceType.getDeclaredFields();

        if (fields.length != 0) {
            if (sourceType.isAnnotationPresent(Propertied.class)) {
                return true;
            }

            for (Field field : fields) {
                if (field.isAnnotationPresent(Propertied.class)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static void scanRecursive(Object source, Set<Property<?>> properties, int depth) {
        Class<?> sourceType = source.getClass();

        if (depth == 1) {
            if (sourceType.isAnnotationPresent(Propertied.class)) {
                properties.addAll(extractProperties(sourceType, source));
            }

            properties.addAll(extractPropertiesOfFields(sourceType, source));
        } else {
            Class<?> superclassType = sourceType;

            for (int i = 1; i < depth && (superclassType = superclassType.getSuperclass()) != null; i++) {
                if (superclassType.isAnnotationPresent(Propertied.class)) {
                    properties.addAll(extractProperties(superclassType, source));
                }

                properties.addAll(extractPropertiesOfFields(superclassType, source));
            }
        }
    }

    @Override
    public Collection<Property<?>> scan(Object source) {
        if (!shouldScan(source)) {
            return Collections.emptyList();
        }

        ScanDepth scanDepthAnno = source.getClass().getAnnotation(ScanDepth.class);
        int scanDepth = scanDepthAnno == null ? DEFAULT_DEPTH : scanDepthAnno.value();

        Set<Property<?>> properties = new HashSet<>();

        scanRecursive(source, properties, scanDepth);

        return Collections.unmodifiableSet(properties);
    }
}