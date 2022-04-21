package dev.aknologia.konsole.niflheim.context;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.CommandManager;
import dev.aknologia.konsole.niflheim.ConsoleVariable;
import dev.aknologia.konsole.niflheim.ParsedArgument;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;

import java.util.HashMap;
import java.util.Map;

public class CommandContext {
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = new HashMap<>();

    static {
        PRIMITIVE_TO_WRAPPER.put(boolean.class, Boolean.class);
        PRIMITIVE_TO_WRAPPER.put(byte.class, Byte.class);
        PRIMITIVE_TO_WRAPPER.put(short.class, Short.class);
        PRIMITIVE_TO_WRAPPER.put(char.class, Character.class);
        PRIMITIVE_TO_WRAPPER.put(int.class, Integer.class);
        PRIMITIVE_TO_WRAPPER.put(long.class, Long.class);
        PRIMITIVE_TO_WRAPPER.put(float.class, Float.class);
        PRIMITIVE_TO_WRAPPER.put(double.class, Double.class);
    }

    private final String input;
    private Object command;
    private final Map<String, ParsedArgument<?>> arguments;
    private final StringRange range;

    public CommandContext(final String input, final Map<String, ParsedArgument<?>> arguments, final Command command, final StringRange range) {
        this.input = input;
        this.arguments = arguments;
        this.range = range;
        if(command == null) {
            this.command = KonsoleClient.getCommandManager().getDispatcher().getCommand(this.input);
            if(this.command == null) this.command = KonsoleClient.getCommandManager().getDispatcher().getConVar(this.input);
        } else this.command = command;
    }

    public Command getCommand() {
        return command instanceof ConsoleVariable<?> ? CommandManager.fromConVar((ConsoleVariable<?>) this.command) : (Command) this.command;
    }

    public <V> V getArgument(final String name, final Class<V> clazz) throws CommandSyntaxException {
        final ParsedArgument<?> argument = arguments.get(name);

        if(argument == null) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherMissingArgument().create(name, clazz.getSimpleName());
        }

        final Object result = argument.getResult();
        if(PRIMITIVE_TO_WRAPPER.getOrDefault(clazz, clazz).isAssignableFrom(result.getClass())) {
            return (V) result;
        } else {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherInvalidArgument().create(name, result.getClass().getSimpleName(), clazz.getSimpleName());
        }
    }

    @Override
    public boolean equals(final Object o) {
        if(this == o) return true;
        if(!(o instanceof final CommandContext that)) return false;

        if(!arguments.equals(that.arguments)) return false;
        return command.equals(that.command);
    }

    @Override
    public int hashCode() {
        int result = command.hashCode();
        result = 31 * result + arguments.hashCode();

        return result;
    }

    public StringRange getRange() { return range; }

    public String getInput() { return input; }
}
