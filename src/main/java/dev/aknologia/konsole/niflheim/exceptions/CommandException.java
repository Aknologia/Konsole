package dev.aknologia.konsole.niflheim.exceptions;

import net.minecraft.text.Text;

public class CommandException extends RuntimeException {
    private final Text message;

    public CommandException(Text text) {
        super(text.getString(), null, CommandSyntaxException.ENABLE_COMMAND_STACK_TRACES, CommandSyntaxException.ENABLE_COMMAND_STACK_TRACES);
        this.message = text;
    }

    public Text getTextMessage() {
        return this.message;
    }
}
