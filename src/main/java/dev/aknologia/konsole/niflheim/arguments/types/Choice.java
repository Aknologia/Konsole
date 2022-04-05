package dev.aknologia.konsole.niflheim.arguments.types;

public class Choice<T> {
    private final String name;
    private final T value;

    public Choice(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }
}
