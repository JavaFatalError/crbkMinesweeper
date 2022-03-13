package de.crbk.minesweeper.util;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.StringJoiner;

public final class StringUtil {

    private StringUtil() {
    
    }
    
    public static <T> String commonToString(final Class<T> pClass, final T object) {
        final StringBuilder builder = new StringBuilder(pClass.getName());
        
        final Field[] fields = pClass.getDeclaredFields();
        final StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (final Field field : fields) {
            field.setAccessible(true);
            try {
                final String fieldName = field.getName();
                final Object fieldValue = field.get(object);
                joiner.add(
                        MessageFormat.format(
                                "{0}={1}",
                                fieldName,
                                fieldValue
                        )
                );
            } catch (final IllegalAccessException pE) {
                throw new RuntimeException("error building toString for given object", pE);
            }
            field.setAccessible(false);
        }
        builder.append(joiner);
        return builder.toString();
    }
    
}
