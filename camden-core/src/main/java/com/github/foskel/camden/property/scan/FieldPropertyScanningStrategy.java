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
public enum FieldPropertyScanningStrategy implements PropertyScanningStrategy {
    INSTANCE;

    private static final int DEFAULT_DEPTH = 1;

    private static void extractPropertiesOfFields(Class<?> type, Object parentSource, Set<Property<?>> properties) {
        // extract all fields annotated with @Propertied
        List<PropertyFieldData> fieldDatas = getPropertyFields(type, parentSource);

        extractAllProperties(fieldDatas, properties);
    }

    private static List<PropertyFieldData> getPropertyFields(Class<?> type, Object source) {
        Field[] fields = type.getDeclaredFields();
        List<PropertyFieldData> result = new ArrayList<>();

        for (Field declaredField : fields) {
            if (declaredField.isAnnotationPresent(Propertied.class)) {
                ensureAccess(declaredField);

                Object value = getValueOf(declaredField, source);

                result.add(new PropertyFieldData(value, declaredField.getAnnotation(ScanDepth.class)));
            }
        }

        return result;
    }
    
    private static void extractAllProperties(Collection<PropertyFieldData> sources, Set<Property<?>> properties) {
        for (PropertyFieldData data : sources) {
            Object source = data.value;

            if (source instanceof Collection) {
                for (Object sourceElement : (Collection) source) {
                    sources.add(new PropertyFieldData(sourceElement, sourceElement.getClass().getAnnotation(ScanDepth.class)));
                }

                continue;
            }

            scanRecursive(source, properties, data.depthAnno);
        }
    }

    private static void extractProperties(Class<?> type, Object source, Set<Property<?>> properties) {
        for (Field field : type.getDeclaredFields()) {
            if (!shouldAccept(field)) {
                continue;
            }

            ensureAccess(field);
            properties.add(getPropertyFrom(field, source));
        }
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

    private static void scanRecursive(Object source, Set<Property<?>> properties, ScanDepth depthAnno) {
        int depth = depthAnno == null ? DEFAULT_DEPTH : depthAnno.value();
        Class<?> sourceType = source.getClass();

        if (depth == 1) {
            if (sourceType.isAnnotationPresent(Propertied.class)) {
                extractProperties(sourceType, source, properties);
                // extract all fields extending Property.class
            }

            extractPropertiesOfFields(sourceType, source, properties);
        } else {
            Class<?> superclassType = sourceType;

            for (int i = 1; i < depth && (superclassType = superclassType.getSuperclass()) != null; i++) {
                if (sourceType.isAnnotationPresent(Propertied.class)) {
                    extractProperties(sourceType, source, properties);
                }

                extractPropertiesOfFields(sourceType, source, properties);
            }
        }
    }

    @Override
    public Collection<Property<?>> scan(Object source) {
        if (!shouldScan(source)) {
            return Collections.emptyList();
        }

        Set<Property<?>> properties = new HashSet<>();

        scanRecursive(source, properties, source.getClass().getAnnotation(ScanDepth.class));

        return Collections.unmodifiableSet(properties);
    }

    private static class PropertyFieldData {
        final Object value;
        final ScanDepth depthAnno;

        private PropertyFieldData(Object value, ScanDepth depthAnno) {
            this.value = value;
            this.depthAnno = depthAnno;
        }
    }
}