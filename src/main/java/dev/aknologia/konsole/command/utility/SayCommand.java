package dev.aknologia.konsole.command.utility;

import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.StringArgumentType;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;

import java.util.List;

public class SayCommand extends AbstractCommand {
    public SayCommand() {
        super("say", "Send a message in the chat.", Category.UTILITY, List.of(
                new Argument("message", StringArgumentType.greedyString(), true)
        ));
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        MinecraftClient.getInstance().player.sendChatMessage(context.getArgument("message", String.class));
        return 1;
    }
}
