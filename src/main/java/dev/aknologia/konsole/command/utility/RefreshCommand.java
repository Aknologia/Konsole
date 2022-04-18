package dev.aknologia.konsole.command.utility;

import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;

import java.util.List;

public class RefreshCommand extends AbstractCommand {
    public RefreshCommand() {
        super("refresh", "Refresh Chunks around you.", Category.UTILITY, List.of());
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        MinecraftClient.getInstance().worldRenderer.reload();
        return 1;
    }
}
