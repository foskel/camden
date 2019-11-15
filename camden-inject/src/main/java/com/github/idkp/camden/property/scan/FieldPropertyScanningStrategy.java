package com.github.idkp.camden.property.scan;

import com.github.idkp.camden.annotations.Exclude;
import com.github.idkp.camden.annotations.Propertied;
import com.github.idkp.camden.annotations.ScanDepth;
import com.github.idkp.camden.property.Property;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @since 7/16/2017
 */
//TODO: Create a separate FieldPropertyExtractor to handle different type of fields/annotations presence?
public final class FieldPropertyScanningStrategy implements PropertyScanningStrategy {
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

    private static void extractAllProperties(List<PropertyFieldData> sources, Set<Property<?>> properties) {
        for (int i = 0; i < sources.size(); i++) {
            PropertyFieldData data = sources.get(i);
            Object source = data.value;

            if (source instanceof Collection) {
                for (Object sourceElement : (Collection) source) {
                    sources.add(new PropertyFieldData(sourceElement, sourceElement.getClass().getAnnotation(ScanDepth.class)));
                }

                continue;
            }

            scanRecursive(source, properties, data.scanDepth, false);
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

    private static void scanRecursive(Object source, Set<Property<?>> properties, int depth, boolean force) {
        Class<?> sourceType = source.getClass();

        scan(source, sourceType, force, properties);

        if (depth > 1) {
            Class<?> superclassType = sourceType;

            for (int i = 1; i < depth && (superclassType = superclassType.getSuperclass()) != Object.class; i++) {
                scan(source, superclassType, force, properties);
            }
        }
    }

    private static void scan(Object source, Class<?> sourceType, boolean force, Set<Property<?>> properties) {
        if (force || sourceType.isAnnotationPresent(Propertied.class)) {
            extractProperties(sourceType, source, properties);
        }

        extractPropertiesOfFields(sourceType, source, properties);
    }

    @Override
    public Collection<Property<?>> scan(Object source) {
        if (!shouldScan(source)) {
            return Collections.emptyList();
        }

        Set<Property<?>> properties = new HashSet<>();

        scanRecursive(source, properties, getScanDepth(
                source.getClass().getAnnotation(ScanDepth.class)), false);

        return Collections.unmodifiableSet(properties);
    }

    private static int getScanDepth(ScanDepth anno) {
        return anno == null ? DEFAULT_DEPTH : anno.value();
    }

    private static class PropertyFieldData {
        final Object value;
        final int scanDepth;

        private PropertyFieldData(Object value, ScanDepth depthAnno) {
            this.value = value;
            this.scanDepth = getScanDepth(depthAnno);
        }
    }
}