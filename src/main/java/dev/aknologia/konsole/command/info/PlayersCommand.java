package dev.aknologia.konsole.command.info;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.LiteralText;
import net.minecraft.world.GameMode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlayersCommand extends AbstractCommand {
    private final Ordering<PlayerListEntry> ENTRY_ORDERING = Ordering.from(new EntryOrderComparator());

    public PlayersCommand() {
        super("players", "Show the list the connected players and their ping.", Category.INFO, List.of());
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        ClientPlayNetworkHandler clientPlayNetworkHandler = MinecraftClient.getInstance().getNetworkHandler();
        assert clientPlayNetworkHandler != null;
        List<PlayerListEntry> list = ENTRY_ORDERING.sortedCopy(clientPlayNetworkHandler.getPlayerList());
        List<String> players = new ArrayList<>();
        list.forEach(pl -> players.add(String.format("-  \u00A7r%s \u00A7r\u00A77\u00A7o%sms", pl.getProfile().getName(), pl.getLatency())));
        KonsoleClient.getKonsole().addMessage(new LiteralText(String.format("\u00A76\u00A7nPlayers:\u00A7r %s\n%s", clientPlayNetworkHandler.getPlayerList().size(), String.join("\n", players))));
        return 1;
    }

    // "borrowed"
    @Environment(EnvType.CLIENT)
    static class EntryOrderComparator implements Comparator<PlayerListEntry> {
        EntryOrderComparator() {
        }

        public int compare(PlayerListEntry playerListEntry, PlayerListEntry playerListEntry2) {
            Team team = playerListEntry.getScoreboardTeam();
            Team team2 = playerListEntry2.getScoreboardTeam();
            return ComparisonChain.start().compareTrueFirst(playerListEntry.getGameMode() != GameMode.SPECTATOR, playerListEntry2.getGameMode() != GameMode.SPECTATOR).compare(team != null ? team.getName() : "", team2 != null ? team2.getName() : "").compare(playerListEntry.getProfile().getName(), playerListEntry2.getProfile().getName(), String::compareToIgnoreCase).result();
        }
    }
}
