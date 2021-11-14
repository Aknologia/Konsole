package dev.aknologia.konsole.niflheim.suggestion;

import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.arguments.Argument;

public class SuggestionContext {
    public final Command command;
    public final Argument argument;
    public final int startPos;

    public SuggestionContext(Command command, Argument argument, int startPos) {
        this.command = command;
        this.argument = argument;
        this.startPos = startPos;
    }
}
