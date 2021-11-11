package dev.aknologia.konsole.niflheim.context;

import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.CommandDispatcher;
import dev.aknologia.konsole.niflheim.ParsedArgument;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandContextBuilder {
    private final Map<String, ParsedArgument<?>> arguments = new LinkedHashMap<>();
    private final CommandDispatcher dispatcher;
    private Command command;
    private StringRange range;

    public CommandContextBuilder(final CommandDispatcher dispatcher, final int start) {
        this.dispatcher = dispatcher;
        this.range = StringRange.at(start);
    }

    public CommandContextBuilder withArgument(final String name, final ParsedArgument<?> argument) {
        this.arguments.put(name, argument);
        return this;
    }

    public Map<String, ParsedArgument<?>> getArguments() { return arguments; }

    public CommandContextBuilder withCommand(final Command command) {
        this.command = command;
        return this;
    }

    public CommandContextBuilder copy() {
        final CommandContextBuilder copy = new CommandContextBuilder(dispatcher, range.getStart());
        copy.arguments.putAll(arguments);
        copy.range = range;
        return copy;
    }

    public Command getCommand() { return command; }

    public CommandContext build(final String input) {
        return new CommandContext(input, arguments, command, range);
    }

    public CommandDispatcher getDispatcher() { return dispatcher; }

    public StringRange getRange() { return range; }
}
