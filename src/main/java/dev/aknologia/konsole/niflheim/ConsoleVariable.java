package dev.aknologia.konsole.niflheim;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.arguments.*;
import dev.aknologia.konsole.niflheim.context.CommandContext;

import java.util.function.Function;

public class ConsoleVariable<T> {
    private String name;
    private String description;
    private Class<T> type;
    private T value;

    private Function callback;

    public ConsoleVariable(String name, T defaultValue, Class<T> type) {
        this(name, "No description available.", defaultValue, type);
    }

    public ConsoleVariable(String name, String description, T defaultValue, Class<T> type) {
        this.name = name;
        this.description = description;
        this.value = defaultValue;
        this.type = type;
    }

    public static <T> void register(CommandDispatcher dispatcher, String name, T defaultValue, Class<T> type) {
        dispatcher.register(new ConsoleVariable<T>(name, defaultValue, type));
    }

    public static <T> void register(CommandDispatcher dispatcher, String name, String description, T defaultValue, Class<T> type) {
        dispatcher.register(new ConsoleVariable<T>(name, description, defaultValue, type));
    }

    public void register(CommandDispatcher dispatcher) {
        dispatcher.register(this);
    }

    public void set(Object value) {
        this.value = (T) value;
    }

    public T get() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Object[] getArgumentParams() { return new Object[]{}; }

    public ConVarType getType() {
        return ConVarType.getEnumFromType(this.type);
    }

    public T getArgumentValue(CommandContext context) {
        return context.getArgument("value", this.type);
    }

    public boolean hasCallback() { return this.callback != null; }
    public Function getCallback() { return this.callback; }
    public void setCallback(Function callback) { this.callback = callback; }

    public String toString() {
        ConVarType type = this.getType();
        String rl = ""; String lt = "";
        switch(type) {
            case STRING:
                rl = "\""; lt = "\"";
                break;
            case BOOLEAN:
                if((boolean) this.get()) rl = "\u00A7a";
                else rl = "\u00A7c";
        }
        return String.format("\u00A73%s \u00A7r= \u00A77%s%s%s\n    \u00A7o%s", this.getName(), rl, this.get(), lt, this.getDescription());
    }
}

enum ConVarType {
    STRING(StringArgumentType.class),
    INTEGER(IntegerArgumentType.class),
    BOOLEAN(BoolArgumentType.class),
    DOUBLE(DoubleArgumentType.class),
    LONG(LongArgumentType.class);

    public final Class<?> ARGUMENT_TYPE;

    ConVarType(Class<?> argumentType) {
        this.ARGUMENT_TYPE = argumentType;
    }

    public static ConVarType getEnumFromType(Class<?> varType) {
        switch(varType.getSimpleName()) {
            case "String":
                return ConVarType.STRING;
            case "Integer":
                return ConVarType.INTEGER;
            case "Boolean":
                return ConVarType.BOOLEAN;
            case "Double":
                return ConVarType.DOUBLE;
            case "Long":
                return ConVarType.LONG;
            default:
                KonsoleClient.LOG.warn("Found unknown type for ConVar: %s", varType.getSimpleName());
                return ConVarType.STRING;
        }
    }

    public ArgumentType getArgument(Object[] argumentParams) {
        Class pkg = this.ARGUMENT_TYPE;
        if(pkg.isAssignableFrom(StringArgumentType.class)) return StringArgumentType.string();
        if(pkg.isAssignableFrom(BoolArgumentType.class)) return BoolArgumentType.bool();
        if(pkg.isAssignableFrom(IntegerArgumentType.class)) {
            if(argumentParams.length == 1) return IntegerArgumentType.integer((int) argumentParams[0]);
            else if(argumentParams.length > 1) return IntegerArgumentType.integer((int) argumentParams[0], (int) argumentParams[1]);
            else return IntegerArgumentType.integer();
        }
        if(pkg.isAssignableFrom(DoubleArgumentType.class)) {
            if(argumentParams.length == 1) return DoubleArgumentType.doubleArg((int) argumentParams[0]);
            else if(argumentParams.length > 1) return DoubleArgumentType.doubleArg((int) argumentParams[0], (int) argumentParams[1]);
            else return DoubleArgumentType.doubleArg();
        }
        if(pkg.isAssignableFrom(LongArgumentType.class)) {
            if(argumentParams.length == 1) return LongArgumentType.longArg((int) argumentParams[0]);
            else if(argumentParams.length > 1) return LongArgumentType.longArg((int) argumentParams[0], (int) argumentParams[1]);
            else return LongArgumentType.longArg();
        }
        KonsoleClient.LOG.warn("Could not find a suitable argument type for ConVarType: %s\nReversing to default StringArgumentType.", this.name());
        return StringArgumentType.string();
    }
}
