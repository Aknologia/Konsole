package dev.aknologia.konsole.niflheim.exceptions;

import com.mojang.brigadier.Message;
import dev.aknologia.konsole.niflheim.ImmutableStringReader;

public class SimpleCommandExceptionType implements CommandExceptionType {
    private final Message message;

    public SimpleCommandExceptionType(final Message message) { this.message = message; }

    public CommandSyntaxException create() {
        return new CommandSyntaxException(this, message);
    }

    public CommandSyntaxException createWithContext(final ImmutableStringReader reader) {
        return new CommandSyntaxException(this, message, reader.getString(), reader.getCursor());
    }

    @Override
    public String toString() {
        return message.getString();
    }
}
