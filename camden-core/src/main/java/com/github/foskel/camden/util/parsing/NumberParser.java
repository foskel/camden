package com.github.foskel.camden.util.parsing;

import com.github.foskel.camden.util.PrimitiveWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class NumberParser {
    private static final Map<Class<? extends Number>, Function<Number, Number>> BINDINGS;

    static {
        BINDINGS = new HashMap<>();

        BINDINGS.put(Integer.class, Number::intValue);
        BINDINGS.put(Long.class, Number::longValue);
        BINDINGS.put(Float.class, Number::floatValue);
        BINDINGS.put(Double.class, Number::doubleValue);
        BINDINGS.put(Byte.class, Number::byteValue);
        BINDINGS.put(Short.class, Number::shortValue);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number> T parse(String input, Class<T> type) {
        double fromInput = Double.parseDouble(input);

        Class<T> wrappedType = (Class<T>) PrimitiveWrapper.INSTANCE.get(type);
        Function<Number, Number> parsedResult = BINDINGS.get(wrappedType);

        return (T) parsedResult.apply(fromInput);
    }
}