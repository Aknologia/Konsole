package dev.aknologia.konsole.command.utility;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;

import java.util.List;

public class ClearCommand extends AbstractCommand {
    public ClearCommand() {
        super("clear", "Clear the console.", Category.UTILITY, List.of());
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        KonsoleClient.getKonsole().clear(false);
        return 1;
    }
}
