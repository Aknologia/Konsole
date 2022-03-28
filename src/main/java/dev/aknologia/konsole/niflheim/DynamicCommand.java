package dev.aknologia.konsole.niflheim;

import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DynamicCommand implements Command {
    public String name;
    public String description;
    public List<Argument> arguments;

    private Function callback;

    public DynamicCommand(String name, String description, List<Argument> arguments, Function callback) {
        this.name = name;
        this.description = description;
        this.arguments = arguments;
        this.callback = callback;
    }

    @Override
    public void register(CommandDispatcher dispatcher) {
        dispatcher.register(this);
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        return (int) this.callback.apply(context);
    }

    @Override
    public List<Argument> getArguments() { return this.arguments; }

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
