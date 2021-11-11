package dev.aknologia.konsole.niflheim.arguments;

import dev.aknologia.konsole.niflheim.StringReader;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;

import java.util.Collection;
import java.util.Collections;

public interface ArgumentType<T> {
    T parse(StringReader reader) throws CommandSyntaxException;

    default Collection<String> getExamples() {
        return Collections.emptyList();
    }
}
