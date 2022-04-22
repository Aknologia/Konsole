package dev.aknologia.konsole.command.action;

import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;

import java.util.List;

public class SneakCommand extends AbstractCommand {

    public SneakCommand() {
        super("+sneak", "Toggle sneaking.", Category.ACTION, List.of());
    }

    public static boolean toggledSneak = false;

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        toggledSneak = !toggledSneak;
        return 1;
    }
}
