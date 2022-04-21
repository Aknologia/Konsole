package dev.aknologia.konsole.niflheim.arguments;

import dev.aknologia.konsole.input.KeyManager;
import dev.aknologia.konsole.niflheim.StringReader;
import dev.aknologia.konsole.niflheim.arguments.types.Keys;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;

import java.util.*;

public class KeyArgumentType implements ArgumentType<Keys.Key> {
    private static final Collection<String> EXAMPLES = Arrays.asList("K", "MOUSE1", "SHIFT");

    private KeyArgumentType() {
    }

    public static KeyArgumentType keyArg() { return new KeyArgumentType(); }

    public static Keys.Key getKey(final CommandContext context, final String name) throws CommandSyntaxException {
        return context.getArgument(name, Keys.Key.class);
    }

    @Override
    public Keys.Key parse(final StringReader reader) throws CommandSyntaxException {
        return reader.readKey();
    }

    @Override
    public String toString() { return "key()"; }

    @Override
    public Collection<String> getExamples() { return EXAMPLES; }

    @Override
    public List<String> getSuggestions() {
        List<String> keyList = new ArrayList<>();
        keyList.addAll(KeyManager.KEYS.getKeyList().keySet().stream().toList());
        keyList.addAll(KeyManager.KEYS.getMouseList().keySet().stream().toList());
        return keyList;
    }
}
