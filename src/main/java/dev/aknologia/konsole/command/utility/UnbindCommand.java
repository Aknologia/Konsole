package dev.aknologia.konsole.command.utility;

import com.mojang.brigadier.LiteralMessage;
import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.KeyArgumentType;
import dev.aknologia.konsole.niflheim.arguments.types.Keys;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import dev.aknologia.konsole.niflheim.exceptions.DynamicCommandExceptionType;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class UnbindCommand extends AbstractCommand {
    public static final DynamicCommandExceptionType KEY_NOT_BOUND_EXCEPTION = new DynamicCommandExceptionType(expected -> new LiteralMessage("Key not bound: '" + expected + "'"));

    public UnbindCommand() {
        super("unbind", "Unbind a specific key.", Category.UTILITY, List.of(
                new Argument("key", KeyArgumentType.keyArg(), true)
        ));
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        Keys.Key key = KeyArgumentType.getKey(context, "key");
        if(key.getKeyName() == null || key.getKeyCode() == GLFW.GLFW_KEY_UNKNOWN) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().create("key");
        if(!KonsoleClient.BINDS.containsKey(key.getKeyCode())) throw KEY_NOT_BOUND_EXCEPTION.create(key.getKeyName());
        KonsoleClient.BINDS.remove(key.getKeyCode());
        return 1;
    }
}
