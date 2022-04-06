package dev.aknologia.konsole.command.utility;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.command.UtilityCategory;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.IntegerArgumentType;
import dev.aknologia.konsole.niflheim.arguments.MultipleArgumentType;
import dev.aknologia.konsole.niflheim.arguments.StringArgumentType;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.MathHelper;

import java.util.*;

public class HelpCommand implements Command {
    public String name = "help";
    public String description = "Shows this help message.";
    public Class<?> category = UtilityCategory.class;
    public List<Argument> arguments = List.of(
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
    );

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
        Command command = KonsoleClient.COMMAND_MANAGER.getDispatcher().getCommand(name.trim().toLowerCase(Locale.ROOT));
        if(command == null) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().create();

        List<String> lines = new ArrayList<>();
        lines.add(String.format("\u00A7lHELP\u00A7r \u00A77> \u00A7b%s", command.getName()));
        lines.add(String.format("\n\u00A7nDescription:\u00A7r\u00A77 %s", command.getDescription()));
        lines.add(String.format("\u00A7nUsage:\u00A7r\u00A77 %s", KonsoleClient.COMMAND_MANAGER.getDispatcher().getUsage(command)));
        KonsoleClient.KONSOLE.addMessage(new LiteralText(String.join("\n", lines)));
        return 1;
    }

    private int showPage(CommandContext context, int page) throws CommandSyntaxException {
        List<Command> commands = KonsoleClient.COMMAND_MANAGER.getDispatcher().getCommands().values().stream().toList();
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

        KonsoleClient.KONSOLE.addMessage(new LiteralText(String.join("\n", lines)));
        return 1;
    }

    private boolean isNumber(String val) {
        try {
            int x =  Integer.parseInt(val.trim());
            return true;
        } catch(NumberFormatException ex) {
            return false;
        }
    }

    @Override
    public List<Argument> getArguments() {
        return this.arguments;
    }

    @Override
    public void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Class<Category> getCategory() { return (Class<Category>) this.category; }
}
