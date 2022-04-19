package dev.aknologia.konsole.command.info;

import dev.aknologia.konsole.KonsoleLogger;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.integrated.IntegratedServer;

import java.util.List;
import java.util.Objects;

public class TPSCommand extends AbstractCommand {
    public TPSCommand() {
        super("tps", "Show the server's ticks per second. (Singleplayer Only)", Category.INFO, List.of());
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        IntegratedServer server = MinecraftClient.getInstance().getServer();
        ClientConnection clientConnection = Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).getConnection();
        if(server == null) {
            KonsoleLogger.getInstance().error("This command is only available in singleplayer.");
            return 1;
        }
        KonsoleLogger.getInstance().info(String.format("\u00A76\u00A7nTicks:\u00A7r \u00A7b%.0f\u00A7rms per tick \u00A77(%s total ticks)\n\u00A76\u00A7nPackets Average:\u00A7r \u00A7b%.0f\u00A7r sent, \u00A7b%.0f\u00A7r received", server.getTickTime(), server.getTicks(), clientConnection.getAveragePacketsSent(), clientConnection.getAveragePacketsReceived()));
        return 1;
    }
}
