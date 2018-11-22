package com.github.foskel.camden.property.scan;

import com.github.foskel.camden.annotations.Exclude;
import com.github.foskel.camden.annotations.Propertied;
import com.github.foskel.camden.property.Property;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Fred
 * @since 7/16/2017
 */
//TODO: Create a separate FieldPropertyExtractor to handle different type of fields/annotations presence?
public enum FieldPropertyScanningStrategy implements PropertyScanningStrategy {
    INSTANCE;

    private static List<Property<?>> extractPropertiesOfFields(Object parentSource) {
        List<Object> fieldValues = getPropertyFieldsValues(parentSource);
        List<Property<?>> properties = extractAllProperties(fieldValues);

        fieldValues.forEach(fieldSource -> properties.addAll(extractPropertiesOfFields(fieldSource)));

        return properties;
    }

    private static List<Object> getPropertyFieldsValues(Object source) {
        Field[] fields = source.getClass().getDeclaredFields();
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

            properties.addAll(extractProperties(source));
        }

        return properties;
    }

    private static List<Property<?>> extractProperties(Object source) {
        List<Property<?>> properties = new ArrayList<>();

        for (Field field : source.getClass().getDeclaredFields()) {
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

        properties.addAll(extractPropertiesOfFields(source));

        return Collections.unmodifiableSet(properties);
    }
}