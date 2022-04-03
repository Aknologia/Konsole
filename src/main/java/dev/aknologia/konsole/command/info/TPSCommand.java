package dev.aknologia.konsole.command.info;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.command.InfoCategory;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class TPSCommand implements Command {
    public String name = "tps";
    public String description = "Show the server's ticks per second. (Singleplayer Only)";
    public Class<?> category = InfoCategory.class;
    public List<Argument> arguments = new ArrayList<>();

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        IntegratedServer server = MinecraftClient.getInstance().getServer();
        ClientConnection clientConnection = MinecraftClient.getInstance().getNetworkHandler().getConnection();
        if(server == null) {
            KonsoleClient.KONSOLE.addMessage(new LiteralText("This command is only available in singleplayer.").formatted(Formatting.RED));
            return 1;
        }
        KonsoleClient.KONSOLE.addMessage(new LiteralText(String.format("\u00A76\u00A7nTicks:\u00A7r \u00A7b%.0f\u00A7rms per tick \u00A77(%s total ticks)\n\u00A76\u00A7nPackets Average:\u00A7r \u00A7b%.0f\u00A7r sent, \u00A7b%.0f\u00A7r received", server.getTickTime(), server.getTicks(), clientConnection.getAveragePacketsSent(), clientConnection.getAveragePacketsReceived())));
        return 1;
    }

    @Override
    public List<Argument> getArguments() {
        return this.arguments;
    }

    @Override
    public void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Class<Category> getCategory() { return (Class<Category>) this.category; }
}
