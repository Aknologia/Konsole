package dev.aknologia.konsole.niflheim.arguments;

import com.mojang.brigadier.LiteralMessage;
import dev.aknologia.konsole.niflheim.StringReader;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import dev.aknologia.konsole.niflheim.exceptions.DynamicCommandExceptionType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PlayerArgumentType implements ArgumentType<PlayerListEntry> {
    private static final Collection<String> EXAMPLES = Arrays.asList("Player001", "Player502", "Player13");
    public static final DynamicCommandExceptionType PLAYER_NOT_FOUND_EXCEPTION = new DynamicCommandExceptionType(expected -> new LiteralMessage("Unknown player: '" + expected + "'"));

    private PlayerArgumentType() {
    }

    public static PlayerArgumentType player() { return new PlayerArgumentType(); }

    public static PlayerListEntry getPlayer(final CommandContext context, final String name) {
        return context.getArgument(name, PlayerListEntry.class);
    }

    @Override
    public PlayerListEntry parse(final StringReader reader) throws CommandSyntaxException {
        String playerName = reader.readString();
        PlayerListEntry player = this.getPlayer(playerName);
        if(player == null) throw PLAYER_NOT_FOUND_EXCEPTION.create(playerName);
        return player;
    }

    private @Nullable PlayerListEntry getPlayer(String name) {
        ClientPlayNetworkHandler clientPlayNetworkHandler = MinecraftClient.getInstance().getNetworkHandler();
        List<PlayerListEntry> playerList = clientPlayNetworkHandler.getPlayerList().stream().toList();
        for(int i = 0; i < playerList.size(); i++) {
            PlayerListEntry player = playerList.get(i);
            if(player.getProfile().getName().equals(name)) return player;
        }
        return null;
    }

    @Override
    public String toString() { return "player()"; }

    @Override
    public Collection<String> getExamples() { return EXAMPLES; }

    @Override
    public List<String> getSuggestions() {
        ClientPlayNetworkHandler clientPlayNetworkHandler = MinecraftClient.getInstance().getNetworkHandler();
        List<PlayerListEntry> playerListEntries = (List<PlayerListEntry>) clientPlayNetworkHandler.getPlayerList();

        List<String> playerNames = new ArrayList<>();
        playerListEntries.forEach(pl -> playerNames.add(pl.getProfile().getName()));
        return playerNames;
    }
}
