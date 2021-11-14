package dev.aknologia.konsole.niflheim.arguments;

import dev.aknologia.konsole.niflheim.StringReader;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BoolArgumentType implements ArgumentType<Boolean> {
    private static final Collection<String> EXAMPLES = Arrays.asList("true", "false");
    private static final List<String> SUGGESTIONS = Arrays.asList("true", "false");

    private BoolArgumentType() {
    }

    public static BoolArgumentType bool() {
        return new BoolArgumentType();
    }

    public static boolean getBool(final CommandContext context, final String name) {
        return context.getArgument(name, Boolean.class);
    }

    @Override
    public Boolean parse(final StringReader reader) throws CommandSyntaxException {
        return reader.readBoolean();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @Override
    public List<String> getSuggestions() { return SUGGESTIONS; }
}
