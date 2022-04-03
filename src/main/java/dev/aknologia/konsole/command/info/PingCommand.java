package dev.aknologia.konsole.command.info;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.command.InfoCategory;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.List;

public class PingCommand implements Command {
    public String name = "ping";
    public String description = "Show your current latency.";
    public Class<?> category = InfoCategory.class;
    public List<Argument> arguments = new ArrayList<>();

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        ClientPlayNetworkHandler clientPlayNetworkHandler = MinecraftClient.getInstance().getNetworkHandler();
        KonsoleClient.KONSOLE.addMessage(new LiteralText(String.format("\u00A76\u00A7nLatency:\u00A7r %sms", clientPlayNetworkHandler.getPlayerListEntry(MinecraftClient.getInstance().player.getUuid()).getLatency())));
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
