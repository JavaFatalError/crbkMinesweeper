package de.crbk.minesweeper.util;

import java.lang.reflect.Field;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                joiner.add(fieldName + "=" + fieldValue);
            } catch (final IllegalAccessException pE) {
                Logger.getLogger(StringUtil.class.getName()).log(
                        Level.WARNING,
                        "exception building string",
                        pE
                );
            }
            field.setAccessible(false);
        }
        builder.append(joiner);
        return builder.toString();
    }
    
}
