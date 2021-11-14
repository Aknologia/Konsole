package dev.aknologia.konsole.niflheim.context;

import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.CommandDispatcher;
import dev.aknologia.konsole.niflheim.ParsedArgument;
import dev.aknologia.konsole.niflheim.StringReader;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import dev.aknologia.konsole.niflheim.suggestion.SuggestionContext;

import java.util.LinkedHashMap;
import java.util.ListIterator;
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

    public SuggestionContext findSuggestionContext(final String input, final int cursor) {
        String[] split = input.split(" ");
        if(split[0].length() >= cursor) return null;

        ListIterator<Argument> listIterator = this.command.getArguments().listIterator();

        Argument suggestedArg = null;

        StringReader reader = new StringReader(input);
        int lastStart = Integer.MIN_VALUE;

        while (listIterator.hasNext()) {
            Argument arg = listIterator.next();
            try {
                int st = reader.getCursor();
                Object resultArg = arg.getArgumentType().parse(reader);
                int end = reader.getCursor();
                if(st >= cursor) {
                    suggestedArg = listIterator.previous();
                    break;
                }
                lastStart = st;
            } catch(final CommandSyntaxException ex) {
                continue;
            }
        }
        return new SuggestionContext(this.command, suggestedArg, lastStart);
    }
}
