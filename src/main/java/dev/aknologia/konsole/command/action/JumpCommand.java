package dev.aknologia.konsole.command.action;

import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;

import java.util.List;

public class JumpCommand extends AbstractCommand {
    public JumpCommand() {
        super("jump", "Jump once.", Category.ACTION, List.of());
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        assert MinecraftClient.getInstance().player != null;
        if(MinecraftClient.getInstance().player.isOnGround()) MinecraftClient.getInstance().player.jump();
        return 1;
    }
}
