package dev.aknologia.konsole.niflheim;

import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.ArgumentType;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public interface Command {
    int run(CommandContext context) throws CommandSyntaxException;

    List<Argument> getArguments();

    void setArguments(List<Argument> arguments);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    Class<Category> getCategory();
}
