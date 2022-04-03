package dev.aknologia.konsole.niflheim;

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
}
