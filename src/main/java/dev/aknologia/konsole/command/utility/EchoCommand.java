package dev.aknologia.konsole.command.utility;

import dev.aknologia.konsole.KonsoleLogger;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.StringArgumentType;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;

import java.util.List;

public class EchoCommand extends AbstractCommand {
    public EchoCommand() {
        super("echo", "Send a message in the console.", Category.UTILITY, List.of(new Argument("message", StringArgumentType.greedyString(), true)));
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        KonsoleLogger.getInstance().info(context.getArgument("message", String.class));
        return 1;
    }
}
