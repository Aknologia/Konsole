package dev.aknologia.konsole.command.utility;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;

import java.util.List;

public class UnbindAllCommand extends AbstractCommand {
    public UnbindAllCommand() {
        super("unbindall", "Unbind all keys.", Category.UTILITY, List.of());
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        KonsoleClient.BINDS.clear();
        return 1;
    }
}
