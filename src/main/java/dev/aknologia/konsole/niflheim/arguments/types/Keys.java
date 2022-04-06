package dev.aknologia.konsole.niflheim.arguments.types;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class Keys {

    private final HashMap<String, Integer> keyList;
    private final HashMap<String, Integer> mouseList;

    public static final int MWHEELUP = -2;
    public static final int MWHEELDOWN = -3;

    public Keys() {
        HashMap<String, Integer> _list = new HashMap<>();

        // LETTERS
        _list.put("a", GLFW.GLFW_KEY_A);
        _list.put("b", GLFW.GLFW_KEY_B);
        _list.put("c", GLFW.GLFW_KEY_C);
        _list.put("d", GLFW.GLFW_KEY_D);
        _list.put("e", GLFW.GLFW_KEY_E);
        _list.put("f", GLFW.GLFW_KEY_F);
        _list.put("g", GLFW.GLFW_KEY_G);
        _list.put("h", GLFW.GLFW_KEY_H);
        _list.put("i", GLFW.GLFW_KEY_I);
        _list.put("j", GLFW.GLFW_KEY_J);
        _list.put("k", GLFW.GLFW_KEY_K);
        _list.put("l", GLFW.GLFW_KEY_L);
        _list.put("m", GLFW.GLFW_KEY_M);
        _list.put("n", GLFW.GLFW_KEY_N);
        _list.put("o", GLFW.GLFW_KEY_O);
        _list.put("p", GLFW.GLFW_KEY_P);
        _list.put("q", GLFW.GLFW_KEY_Q);
        _list.put("r", GLFW.GLFW_KEY_R);
        _list.put("s", GLFW.GLFW_KEY_S);
        _list.put("t", GLFW.GLFW_KEY_T);
        _list.put("u", GLFW.GLFW_KEY_U);
        _list.put("v", GLFW.GLFW_KEY_V);
        _list.put("w", GLFW.GLFW_KEY_W);
        _list.put("x", GLFW.GLFW_KEY_X);
        _list.put("y", GLFW.GLFW_KEY_Y);
        _list.put("z", GLFW.GLFW_KEY_Z);

        //NUMBERS
        _list.put("0", GLFW.GLFW_KEY_0);
        _list.put("1", GLFW.GLFW_KEY_1);
        _list.put("2", GLFW.GLFW_KEY_2);
        _list.put("3", GLFW.GLFW_KEY_3);
        _list.put("4", GLFW.GLFW_KEY_4);
        _list.put("5", GLFW.GLFW_KEY_5);
        _list.put("6", GLFW.GLFW_KEY_6);
        _list.put("7", GLFW.GLFW_KEY_7);
        _list.put("8", GLFW.GLFW_KEY_8);
        _list.put("9", GLFW.GLFW_KEY_9);

        _list.put("kp0", GLFW.GLFW_KEY_KP_0);
        _list.put("kp1", GLFW.GLFW_KEY_KP_1);
        _list.put("kp2", GLFW.GLFW_KEY_KP_2);
        _list.put("kp3", GLFW.GLFW_KEY_KP_3);
        _list.put("kp4", GLFW.GLFW_KEY_KP_4);
        _list.put("kp5", GLFW.GLFW_KEY_KP_5);
        _list.put("kp6", GLFW.GLFW_KEY_KP_6);
        _list.put("kp7", GLFW.GLFW_KEY_KP_7);
        _list.put("kp8", GLFW.GLFW_KEY_KP_8);
        _list.put("kp9", GLFW.GLFW_KEY_KP_9);

        // F?
        _list.put("f1", GLFW.GLFW_KEY_F1);
        _list.put("f2", GLFW.GLFW_KEY_F2);
        _list.put("f3", GLFW.GLFW_KEY_F3);
        _list.put("f4", GLFW.GLFW_KEY_F4);
        _list.put("f5", GLFW.GLFW_KEY_F5);
        _list.put("f6", GLFW.GLFW_KEY_F6);
        _list.put("f7", GLFW.GLFW_KEY_F7);
        _list.put("f8", GLFW.GLFW_KEY_F8);
        _list.put("f9", GLFW.GLFW_KEY_F9);
        _list.put("f10", GLFW.GLFW_KEY_F10);
        _list.put("f11", GLFW.GLFW_KEY_F11);
        _list.put("f12", GLFW.GLFW_KEY_F12);
        _list.put("f13", GLFW.GLFW_KEY_F13);
        _list.put("f14", GLFW.GLFW_KEY_F14);
        _list.put("f15", GLFW.GLFW_KEY_F15);
        _list.put("f16", GLFW.GLFW_KEY_F16);
        _list.put("f17", GLFW.GLFW_KEY_F17);
        _list.put("f18", GLFW.GLFW_KEY_F18);
        _list.put("f19", GLFW.GLFW_KEY_F19);
        _list.put("f20", GLFW.GLFW_KEY_F20);
        _list.put("f21", GLFW.GLFW_KEY_F21);
        _list.put("f22", GLFW.GLFW_KEY_F22);
        _list.put("f23", GLFW.GLFW_KEY_F23);
        _list.put("f24", GLFW.GLFW_KEY_F24);
        _list.put("f25", GLFW.GLFW_KEY_F25);

        // CMD
        _list.put("lshift", GLFW.GLFW_KEY_LEFT_SHIFT);
        _list.put("rshift", GLFW.GLFW_KEY_RIGHT_SHIFT);

        _list.put("lalt", GLFW.GLFW_KEY_LEFT_ALT);
        _list.put("ralt", GLFW.GLFW_KEY_RIGHT_ALT);

        _list.put("lctrl", GLFW.GLFW_KEY_LEFT_CONTROL);
        _list.put("rctrl", GLFW.GLFW_KEY_RIGHT_CONTROL);

        _list.put("lsuper", GLFW.GLFW_KEY_LEFT_SUPER);
        _list.put("lwin", GLFW.GLFW_KEY_LEFT_SUPER);
        _list.put("lcmd", GLFW.GLFW_KEY_LEFT_SUPER);

        _list.put("rsuper", GLFW.GLFW_KEY_RIGHT_SUPER);
        _list.put("rwin", GLFW.GLFW_KEY_RIGHT_SUPER);
        _list.put("rcmd", GLFW.GLFW_KEY_RIGHT_SUPER);

        _list.put("backspace", GLFW.GLFW_KEY_BACKSPACE);
        _list.put("bs", GLFW.GLFW_KEY_BACKSPACE);

        _list.put("capslock", GLFW.GLFW_KEY_CAPS_LOCK);
        _list.put("cl", GLFW.GLFW_KEY_CAPS_LOCK);

        _list.put("delete", GLFW.GLFW_KEY_DELETE);
        _list.put("del", GLFW.GLFW_KEY_DELETE);

        _list.put("insert", GLFW.GLFW_KEY_INSERT);
        _list.put("ins", GLFW.GLFW_KEY_DELETE);

        _list.put("down", GLFW.GLFW_KEY_DOWN);
        _list.put("up", GLFW.GLFW_KEY_UP);
        _list.put("right", GLFW.GLFW_KEY_RIGHT);
        _list.put("left", GLFW.GLFW_KEY_LEFT);

        _list.put("pagedown", GLFW.GLFW_KEY_PAGE_DOWN);
        _list.put("pdown", GLFW.GLFW_KEY_PAGE_DOWN);
        _list.put("pageup", GLFW.GLFW_KEY_PAGE_UP);
        _list.put("pup", GLFW.GLFW_KEY_PAGE_UP);

        _list.put("end", GLFW.GLFW_KEY_END);

        _list.put("enter", GLFW.GLFW_KEY_ENTER);

        _list.put("kpenter", GLFW.GLFW_KEY_KP_ENTER);

        _list.put("escape", GLFW.GLFW_KEY_ESCAPE);

        _list.put("home", GLFW.GLFW_KEY_HOME);

        _list.put("last", GLFW.GLFW_KEY_LAST);

        _list.put("menu", GLFW.GLFW_KEY_MENU);

        _list.put("numlock", GLFW.GLFW_KEY_NUM_LOCK);

        _list.put("tab", GLFW.GLFW_KEY_TAB);

        _list.put("pause", GLFW.GLFW_KEY_PAUSE);

        _list.put("printscreen", GLFW.GLFW_KEY_PRINT_SCREEN);

        _list.put("scrolllock", GLFW.GLFW_KEY_SCROLL_LOCK);

        // SPECIAL
        _list.put("/", GLFW.GLFW_KEY_SLASH);
        _list.put("slash", GLFW.GLFW_KEY_SLASH);

        _list.put("\\", GLFW.GLFW_KEY_BACKSLASH);
        _list.put("backslash", GLFW.GLFW_KEY_BACKSLASH);
        _list.put("bslash", GLFW.GLFW_KEY_BACKSLASH);

        _list.put("'", GLFW.GLFW_KEY_APOSTROPHE);
        _list.put("apostrophe", GLFW.GLFW_KEY_APOSTROPHE);

        _list.put("kpplus", GLFW.GLFW_KEY_KP_ADD);
        _list.put("kpadd", GLFW.GLFW_KEY_KP_ADD);

        _list.put("kpminus", GLFW.GLFW_KEY_KP_SUBTRACT);
        _list.put("kpsubtract", GLFW.GLFW_KEY_KP_SUBTRACT);

        _list.put(",", GLFW.GLFW_KEY_COMMA);
        _list.put("comma", GLFW.GLFW_KEY_COMMA);

        _list.put("`", GLFW.GLFW_KEY_GRAVE_ACCENT);
        _list.put("graveaccent", GLFW.GLFW_KEY_GRAVE_ACCENT);

        _list.put("=", GLFW.GLFW_KEY_EQUAL);
        _list.put("equal", GLFW.GLFW_KEY_EQUAL);

        _list.put("kpdecimal", GLFW.GLFW_KEY_KP_DECIMAL);
        _list.put("kppoint", GLFW.GLFW_KEY_KP_DECIMAL);

        _list.put("kpslash", GLFW.GLFW_KEY_KP_DIVIDE);
        _list.put("kpdivide", GLFW.GLFW_KEY_KP_DIVIDE);

        _list.put("kpequal", GLFW.GLFW_KEY_KP_EQUAL);

        _list.put("kpasterisk", GLFW.GLFW_KEY_KP_MULTIPLY);
        _list.put("kpmultiply", GLFW.GLFW_KEY_KP_MULTIPLY);

        _list.put("[", GLFW.GLFW_KEY_LEFT_BRACKET);
        _list.put("lbracket", GLFW.GLFW_KEY_LEFT_BRACKET);

        _list.put("]", GLFW.GLFW_KEY_RIGHT_BRACKET);
        _list.put("rbracket", GLFW.GLFW_KEY_RIGHT_BRACKET);


        _list.put("-", GLFW.GLFW_KEY_MINUS);
        _list.put("minus", GLFW.GLFW_KEY_MINUS);

        _list.put(".", GLFW.GLFW_KEY_PERIOD);
        _list.put("period", GLFW.GLFW_KEY_PERIOD);
        _list.put("dot", GLFW.GLFW_KEY_PERIOD);

        _list.put("space", GLFW.GLFW_KEY_SPACE);

        _list.put(";", GLFW.GLFW_KEY_SEMICOLON);
        _list.put("semicolon", GLFW.GLFW_KEY_SEMICOLON);


        HashMap<String, Integer> _list2 = new HashMap<>();
        // MOUSE

        _list2.put("mouse1", GLFW.GLFW_MOUSE_BUTTON_1);
        _list2.put("mouse2", GLFW.GLFW_MOUSE_BUTTON_2);
        _list2.put("mouse3", GLFW.GLFW_MOUSE_BUTTON_3);
        _list2.put("mouse4", GLFW.GLFW_MOUSE_BUTTON_4);
        _list2.put("mouse5", GLFW.GLFW_MOUSE_BUTTON_5);
        _list2.put("mouse6", GLFW.GLFW_MOUSE_BUTTON_6);
        _list2.put("mouse7", GLFW.GLFW_MOUSE_BUTTON_7);
        _list2.put("mouse8", GLFW.GLFW_MOUSE_BUTTON_8);

        _list2.put("mlast", GLFW.GLFW_MOUSE_BUTTON_LAST);
        _list2.put("mleft", GLFW.GLFW_MOUSE_BUTTON_LEFT);
        _list2.put("mmiddle", GLFW.GLFW_MOUSE_BUTTON_MIDDLE);
        _list2.put("mright", GLFW.GLFW_MOUSE_BUTTON_RIGHT);

        _list2.put("mwheelup", MWHEELUP);
        _list2.put("mwheeldown", MWHEELDOWN);

        this.mouseList = _list2;
        this.keyList = _list;
    }

    public HashMap<String, Integer> getKeyList() {
        return this.keyList;
    }

    public HashMap<String, Integer> getMouseList() {
        return this.mouseList;
    }

    public int getKeyCode(String keyName) {
        if(keyName == null) return GLFW.GLFW_KEY_UNKNOWN;
        String name = keyName.toLowerCase(Locale.ROOT).trim();
        if(this.keyList.containsKey(name)) return this.keyList.get(name);
        if(this.mouseList.containsKey(name)) return this.mouseList.get(name);
        return GLFW.GLFW_KEY_UNKNOWN;
    }

    public String getKeyName(int keyCode) {
        if(this.keyList.containsValue(keyCode)) return this.keyList.keySet().stream().filter(entry -> this.keyList.get(entry) == keyCode).toList().get(0).toUpperCase(Locale.ROOT);
        else if(this.mouseList.containsValue(keyCode)) return this.mouseList.keySet().stream().filter(entry -> this.mouseList.get(entry) == keyCode).toList().get(0).toUpperCase(Locale.ROOT);
        return null;
    }

    public static class Key {
        private final String keyName;
        private final int keyCode;

        public Key(String keyName, int keyCode) {
            this.keyName = keyName;
            this.keyCode = keyCode;
        }

        @Override
        public boolean equals(final Object o) {
            if(this == o) return true;
            if(!(o instanceof Key)) return false;

            final Key that = (Key) o;
            return that.keyCode == this.keyCode && that.keyName == this.keyName;
        }

        public String getKeyName() { return this.keyName; }

        public int getKeyCode() { return this.keyCode; }
    }
}
