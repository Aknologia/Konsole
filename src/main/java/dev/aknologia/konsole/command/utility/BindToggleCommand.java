package dev.aknologia.konsole.command.utility;

import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.KeyArgumentType;
import dev.aknologia.konsole.niflheim.arguments.StringArgumentType;
import dev.aknologia.konsole.niflheim.arguments.types.Keys;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;

import java.util.List;

public class BindToggleCommand extends AbstractCommand {
    public BindToggleCommand() {
        super("bindtoggle",
                "Bind a key to 2 commands and switch between them.",
                Category.UTILITY,
                List.of(
                    new Argument("key", KeyArgumentType.keyArg()),
                    new Argument("enabled", StringArgumentType.string()),
                    new Argument("disabled", StringArgumentType.string())
            )
        );
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        Keys.Key key = KeyArgumentType.getKey(context, "key");

        return 1;
    }
}
