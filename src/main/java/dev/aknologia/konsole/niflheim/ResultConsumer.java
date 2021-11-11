package dev.aknologia.konsole.niflheim;

import dev.aknologia.konsole.niflheim.context.CommandContext;

public interface ResultConsumer {
    void onCommandComplete(CommandContext context, boolean success, int result);
}
