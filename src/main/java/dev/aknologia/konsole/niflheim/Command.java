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
    int SINGLE_SUCCESS = 1;

    void register(CommandDispatcher dispatcher);

    int run(CommandContext context) throws CommandSyntaxException;

    public List<Argument> getArguments();

    void setArguments(List<Argument> arguments);

    public String getName();

    void setName(String name);

    public String getDescription();

    void setDescription(String description);
}
