package com.github.idkp.camden.util;

import java.util.HashMap;
import java.util.Map;

public enum PrimitiveWrapper {
    INSTANCE;

    private static final Map<Class<?>, Class<?>> WRAPPEDS;

    static {
        WRAPPEDS = new HashMap<>();

        WRAPPEDS.put(Boolean.TYPE, Boolean.class);
        WRAPPEDS.put(Character.TYPE, Character.class);
        WRAPPEDS.put(Byte.TYPE, Byte.class);
        WRAPPEDS.put(Short.TYPE, Short.class);
        WRAPPEDS.put(Integer.TYPE, Integer.class);
        WRAPPEDS.put(Long.TYPE, Long.class);
        WRAPPEDS.put(Float.TYPE, Float.class);
        WRAPPEDS.put(Double.TYPE, Double.class);
        WRAPPEDS.put(Void.TYPE, Void.class);
    }

    public Class<?> get(Class<?> unwrapped) {
        Class<?> candidate = WRAPPEDS.get(unwrapped);

        return candidate == null
                ? unwrapped
                : candidate;//Optional.ofNullable(candidate).orElse(unwrapped);
    }
}