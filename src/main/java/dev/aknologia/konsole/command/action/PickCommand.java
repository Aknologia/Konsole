package dev.aknologia.konsole.command.action;

import dev.aknologia.konsole.interfaces.MinecraftClientMixinInterface;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;

import java.util.List;

public class PickCommand extends AbstractCommand {
    public PickCommand() {
        super("+pick", "Pick an item.", Category.ACTION, List.of());
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        ((MinecraftClientMixinInterface) MinecraftClient.getInstance()).doItemPickMixed();
        return 1;
    }
}
