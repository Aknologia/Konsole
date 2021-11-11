package dev.aknologia.konsole.niflheim.arguments;

public class Argument {
    final String name;
    final ArgumentType<?> argumentType;
    final boolean required;

    public Argument(final String name, final ArgumentType<?> argumentType) {
        this(name, argumentType, false);
    }

    public Argument(final String name, final ArgumentType<?> argumentType, final boolean required) {
        this.name = name;
        this.argumentType = argumentType;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public ArgumentType<?> getArgumentType() {
        return argumentType;
    }

    public boolean isRequired() {
        return required;
    }
}
