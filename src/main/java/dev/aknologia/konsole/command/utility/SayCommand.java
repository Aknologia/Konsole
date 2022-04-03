package dev.aknologia.konsole.command.utility;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.command.UtilityCategory;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.StringArgumentType;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;

import java.util.Arrays;
import java.util.List;

public class SayCommand implements Command {
    public String name = "say";
    public String description = "Send a message in the chat.";
    public Class<?> category = UtilityCategory.class;
    public List<Argument> arguments = Arrays.asList(
            new Argument("message", StringArgumentType.greedyString(), true)
    );

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        KonsoleClient.CLIENT.player.sendChatMessage(context.getArgument("message", String.class));
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
