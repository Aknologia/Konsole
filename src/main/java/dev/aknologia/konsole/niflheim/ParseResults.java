package dev.aknologia.konsole.niflheim;

import dev.aknologia.konsole.niflheim.context.CommandContextBuilder;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;

import java.util.Collections;
import java.util.Map;

public class ParseResults {
    private final CommandContextBuilder context;
    private final Map<Integer, CommandSyntaxException> exceptions;
    private final ImmutableStringReader reader;

    public ParseResults(final CommandContextBuilder context, final ImmutableStringReader reader, final Map<Integer, CommandSyntaxException> exceptions) {
        this.context = context;
        this.reader = reader;
        this.exceptions = exceptions;
    }

    public ParseResults(final CommandContextBuilder context) {
        this(context, new StringReader(""), Collections.emptyMap());
    }

    public CommandContextBuilder getContext() { return context; }

    public ImmutableStringReader getReader() { return reader; }

    public Map<Integer, CommandSyntaxException> getExceptions() { return exceptions; }
}
