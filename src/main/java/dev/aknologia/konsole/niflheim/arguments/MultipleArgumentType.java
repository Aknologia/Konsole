package dev.aknologia.konsole.niflheim.arguments;

import dev.aknologia.konsole.niflheim.StringReader;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultipleArgumentType implements ArgumentType<Object> {
    private final Argument[] arguments;

    private MultipleArgumentType(Argument[] arguments) {
        this.arguments = arguments;
    }

    public static MultipleArgumentType multiple(Argument[] arguments) {
        return new MultipleArgumentType(arguments);
    }

    public static Object getMultiple(final CommandContext context, final String name) throws CommandSyntaxException {
        return context.getArgument(name, Object.class);
    }

    public Argument[] getArguments() {
        return this.arguments;
    }

    @Override
    public Object parse(final StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        Object result = null;
        for(int i = 0; i < this.arguments.length; i++) {
            try {
                result = this.arguments[i].getArgumentType().parse(reader);
            } catch(final CommandSyntaxException ex) {
                continue;
            }
        }

        if(result == null) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().create();
        }
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof MultipleArgumentType)) return false;

        final MultipleArgumentType that = (MultipleArgumentType) o;
        return arguments == that.arguments;
    }

    @Override
    public String toString() {
        return "Multiple(" + String.join(", ", this.getArgumentsNames()) + ")";
    }

    @Override
    public Collection<String> getExamples() {
        List<String> examples = new ArrayList<>();
        for(int i = 0; i < this.arguments.length; i++) {
            examples.addAll(this.arguments[i].getArgumentType().getExamples());
        }
        return examples;
    }

    @Override
    public List<String> getSuggestions() {
        List<String> suggestions = new ArrayList<>();
        for(int i = 0; i < this.arguments.length; i++) {
            suggestions.addAll(this.arguments[i].getArgumentType().getSuggestions());
        }
        return suggestions;
    }

    private List<String> getArgumentsNames() {
        List<String> argumentsNames = new ArrayList<>();
        for(int i = 0; i < this.arguments.length; i++) {
            argumentsNames.add(this.arguments[i].getName());
        }
        return argumentsNames;
    }
}
