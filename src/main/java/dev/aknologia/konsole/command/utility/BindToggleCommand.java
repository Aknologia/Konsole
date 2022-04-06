package dev.aknologia.konsole.command.utility;

import dev.aknologia.konsole.command.UtilityCategory;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.KeyArgumentType;
import dev.aknologia.konsole.niflheim.arguments.StringArgumentType;
import dev.aknologia.konsole.niflheim.arguments.types.Keys;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;

import java.util.List;

public class BindToggleCommand implements Command {
    public String name = "bindtoggle";
    public String description = "Bind a key to 2 commands and switch between them.";
    public Class<?> category = UtilityCategory.class;
    public List<Argument> arguments = List.of(
            new Argument("key", KeyArgumentType.keyArg()),
            new Argument("enabled", StringArgumentType.string()),
            new Argument("disabled", StringArgumentType.string())
    );

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        Keys.Key key = KeyArgumentType.getKey(context, "key");

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

    @Override
    public Class<Category> getCategory() { return (Class<Category>) this.category; }
}
