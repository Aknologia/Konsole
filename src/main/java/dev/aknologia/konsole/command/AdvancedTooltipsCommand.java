package dev.aknologia.konsole.command;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.CommandDispatcher;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.BoolArgumentType;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;

import java.util.ArrayList;
import java.util.List;

public class AdvancedTooltipsCommand implements Command {
    public String name = "advanced_tooltips";
    public String description = "Show/Hide advanced tooltips.";
    public List<Argument> arguments = new ArrayList<>();

    @Override
    public void register(CommandDispatcher dispatcher) {
        arguments.add(new Argument("value", BoolArgumentType.bool()));
        dispatcher.register(this);
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        KonsoleClient.CLIENT.options.advancedItemTooltips = BoolArgumentType.getBool(context, "value");
        return 1;
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
}
