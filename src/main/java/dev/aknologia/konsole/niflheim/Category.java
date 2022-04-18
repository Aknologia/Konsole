package dev.aknologia.konsole.niflheim;

import dev.aknologia.konsole.command.ActionCategory;
import dev.aknologia.konsole.command.InfoCategory;
import dev.aknologia.konsole.command.UtilityCategory;

import java.util.List;

public class Category {
    private final String name;
    private final List<Command> commands;

    public Category(String name, List<Command> commands) {
        this.name = name;
        this.commands = commands;
    }

    public String getName() { return this.name; }
    public List<Command> getCommands() { return this.commands; }

    public static final Class<?> UTILITY = UtilityCategory.class;
    public static final Class<?> ACTION = ActionCategory.class;
    public static final Class<?> INFO = InfoCategory.class;
}
