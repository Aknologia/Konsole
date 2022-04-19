package dev.aknologia.konsole.command.utility;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.KonsoleLogger;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.IntegerArgumentType;
import dev.aknologia.konsole.niflheim.arguments.MultipleArgumentType;
import dev.aknologia.konsole.niflheim.arguments.StringArgumentType;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.util.math.MathHelper;

import java.util.*;

public class HelpCommand extends AbstractCommand {
    public HelpCommand() {
        super("help",
                "Shows this help message.",
                Category.UTILITY,
                List.of(
                    new Argument(
                            "command|page",
                            MultipleArgumentType.multiple(
                                    new Argument[]
                                            {
                                                    new Argument("page", IntegerArgumentType.integer(0)),
                                                    new Argument("command", StringArgumentType.word())
                                            }
                            )
                    )
            )
        );
    }

    private int commandsPerPage = 8;

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        Object arg = null;
        try {
            arg = MultipleArgumentType.getMultiple(context, "command|page");
        } catch(Exception ex) {}
        int page = 0;
        if(arg == null) return this.showPage(context, page);
        if(arg instanceof Integer) return this.showPage(context, (int) arg);
        else return this.showCommandInfo(context, (String) arg);
    }

    private int showCommandInfo(CommandContext context, String name) throws CommandSyntaxException {
        Command command = KonsoleClient.getCommandManager().getDispatcher().getCommand(name.trim().toLowerCase(Locale.ROOT));
        if(command == null) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().create();

        List<String> lines = new ArrayList<>();
        lines.add(String.format("\u00A7lHELP\u00A7r \u00A77> \u00A7b%s", command.getName()));
        lines.add(String.format("\n\u00A7nDescription:\u00A7r\u00A77 %s", command.getDescription()));
        lines.add(String.format("\u00A7nUsage:\u00A7r\u00A77 %s", KonsoleClient.getCommandManager().getDispatcher().getUsage(command)));
        KonsoleLogger.getInstance().info(String.join("\n", lines));
        return 1;
    }

    private int showPage(CommandContext context, int page) throws CommandSyntaxException {
        List<Command> commands = KonsoleClient.getCommandManager().getDispatcher().getCommands().values().stream().toList();
        int maxPage = MathHelper.floor(commands.size()/commandsPerPage);
        int start = page * commandsPerPage;
        if(start >= commands.size()) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooHigh().create(page+1, maxPage+1);
        int end = start + commandsPerPage;
        if(end >= commands.size()) end = commands.size()-1;
        List<Command> visibles = commands.subList(start, end);
        ListIterator<Command> iterator = visibles.listIterator();

        List<String> lines = new ArrayList<>();
        lines.add(String.format("\u00A7lHELP\u00A7r \u00A77> \u00A7o\u00A77Page %s/%s", page+1, maxPage+1));
        while(iterator.hasNext()) {
            Command command = iterator.next();
            lines.add(String.format("  \u00A73%s \u00A7f- \u00A77%s", command.getName(), command.getDescription()));
        }

        KonsoleLogger.getInstance().info(String.join("\n", lines));
        return 1;
    }
}
