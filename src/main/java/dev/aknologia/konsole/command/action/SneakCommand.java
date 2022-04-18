package dev.aknologia.konsole.command.action;

import dev.aknologia.konsole.command.ActionCategory;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class SneakCommand extends AbstractCommand {
    public SneakCommand() {
        super("+sneak", "Toggle sneaking.", Category.ACTION, List.of());
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.setSneaking(!MinecraftClient.getInstance().player.isSneaking());
        return 1;
    }
}
