package dev.aknologia.konsole.niflheim.arguments.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Settings {
    public final HashMap<String, Boolean> SETTINGS = new HashMap<>();
    public final HashMap<String, Boolean> LIST;

    public Settings() {
        HashMap<String, Boolean> list = new HashMap<>();

        list.put("doBindsBypassKeybinds", false);
        list.put("showTimestamps", true);

        LIST = list;
    }

    public boolean get(final String key) {
        if(SETTINGS.get(key) != null) return SETTINGS.get(key);
        else return LIST.get(key);
    }

    public HashMap<String, Boolean> getSettings() {
        return SETTINGS;
    }

    public void set(String key, boolean value) {
        SETTINGS.put(key, value);
    }

    public HashMap<String, Boolean> getList() {
        return LIST;
    }

    public static class Setting {
        private final String name;
        private final boolean value;

        public Setting(String name, boolean value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public boolean equals(final Object o) {
            if(this == o) return true;
            if(!(o instanceof  Setting)) return false;

            final Setting that = (Setting) o;
            return that.name == this.name && that.value == this.value;
        }

        public String getName() {
            return this.name;
        }

        public boolean getValue() {
            return this.value;
        }
    }
}
