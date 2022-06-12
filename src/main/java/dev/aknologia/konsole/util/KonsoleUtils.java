package dev.aknologia.konsole.util;

import net.minecraft.util.Language;

import java.util.ArrayList;
import java.util.List;

public class KonsoleUtils {
    public static String getTranslated(String key) {
        return Language.getInstance().get(key);
    }

    public static String getTranslated(String key, Object... args) {
        return String.format(getTranslated(key), args);
    }

    public static Integer[] getUnquotedSymbolIndex(String text, char symbol) {
        boolean inQuote = false; List<Integer> indexes = new ArrayList<>();
        for(int i = 0; i < text.length(); i++) {
            if(text.charAt(i) == '\'' || text.charAt(i) == '"') inQuote = !inQuote;
            if(!inQuote && text.charAt(i) == symbol) indexes.add(i);
        }
        return indexes.toArray(new Integer[0]);
    }
}
