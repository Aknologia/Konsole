package dev.aknologia.konsole.convar;

import dev.aknologia.konsole.niflheim.ConsoleVariable;

public class ChatLogLevelConVar extends ConsoleVariable<Integer> {
    public ChatLogLevelConVar() {
        super("chat_log_level", "Set the level of the Chat logger.", 3, Integer.class);
    }

    @Override
    public Object[] getArgumentParams() {
        return new Object[] {0, 3};
    }
}
