package dev.aknologia.konsole.command.info;

import dev.aknologia.konsole.KonsoleLogger;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

import java.util.List;
import java.util.Objects;

public class PingCommand extends AbstractCommand {
    public PingCommand() {
        super("ping", "Show your current latency.", Category.INFO, List.of());
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        ClientPlayNetworkHandler clientPlayNetworkHandler = MinecraftClient.getInstance().getNetworkHandler();
        assert clientPlayNetworkHandler != null; assert MinecraftClient.getInstance().player != null;
        KonsoleLogger.getInstance().info(String.format("\u00A76\u00A7nLatency:\u00A7r %sms", Objects.requireNonNull(clientPlayNetworkHandler.getPlayerListEntry(MinecraftClient.getInstance().player.getUuid())).getLatency()));
        return 1;
    }
}
