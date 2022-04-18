package dev.aknologia.konsole.command.utility;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.KeyArgumentType;
import dev.aknologia.konsole.niflheim.arguments.StringArgumentType;
import dev.aknologia.konsole.niflheim.arguments.types.Keys;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class BindCommand extends AbstractCommand {
    public BindCommand() {
        super("bind",
                "Bind a key to a specific command.",
                Category.UTILITY,
                List.of(
                    new Argument("key", KeyArgumentType.keyArg(), true),
                    new Argument("command", StringArgumentType.string(), true)
                )
        );
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        Keys.Key key = KeyArgumentType.getKey(context, "key");
        if(key.getKeyName() == null || key.getKeyCode() == GLFW.GLFW_KEY_UNKNOWN) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().create("key");
        String command = StringArgumentType.getString(context, "command");
        if(command == null || command.trim().length() < 1) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().create("command");
        KonsoleClient.BINDS.put(key.getKeyCode(), command.trim());
        return 1;
    }
}
