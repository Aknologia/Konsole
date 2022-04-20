package dev.aknologia.konsole.util;

import net.minecraft.util.Language;

public class KonsoleUtils {
    public static String getTranslated(String key) {
        return Language.getInstance().get(key);
    }

    public static String getTranslated(String key, Object... args) {
        return String.format(getTranslated(key), args);
    }
}
