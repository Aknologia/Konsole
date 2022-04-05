package dev.aknologia.konsole.niflheim.arguments;

import com.mojang.brigadier.LiteralMessage;
import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.input.KeyManager;
import dev.aknologia.konsole.niflheim.StringReader;
import dev.aknologia.konsole.niflheim.arguments.types.Keys;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import dev.aknologia.konsole.niflheim.exceptions.DynamicCommandExceptionType;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class KeyArgumentType implements ArgumentType<Keys.Key> {
    private static final Collection<String> EXAMPLES = Arrays.asList("K", "MOUSE1", "SHIFT");
    public static final DynamicCommandExceptionType KEY_NOT_FOUND_EXCEPTION = new DynamicCommandExceptionType(expected -> new LiteralMessage("Invalid key: '" + expected + "'"));

    private KeyArgumentType() {
    }

    public static KeyArgumentType keyArg() { return new KeyArgumentType(); }

    public static Keys.Key getKey(final CommandContext context, final String name) {
        return context.getArgument(name, Keys.Key.class);
    }

    @Override
    public Keys.Key parse(final StringReader reader) throws CommandSyntaxException {
        String keyName = reader.readString();
        int keyCode = KeyManager.KEYS.getKeyCode(keyName);
        if(keyCode == GLFW.GLFW_KEY_UNKNOWN) { throw KEY_NOT_FOUND_EXCEPTION.create(keyName); }
        String offName = KeyManager.KEYS.getKeyName(keyCode);
        return new Keys.Key(offName, keyCode);
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
