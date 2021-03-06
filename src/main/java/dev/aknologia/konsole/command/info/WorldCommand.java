package dev.aknologia.konsole.command.info;

import dev.aknologia.konsole.KonsoleLogger;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.border.WorldBorder;

import java.util.List;

public class WorldCommand extends AbstractCommand {
    public WorldCommand() {
        super("world", "Show informations about the current world.", Category.INFO, List.of());
    }

    private String formatTickTime(long time) {
        float sec = Math.round(time/2.4e4*86400);
        int days = MathHelper.floor(sec / 86400);
        int hours = MathHelper.floor((sec - days * 86400) / 3600);
        int minutes = MathHelper.floor((sec - days * 86400 - hours * 3600) / 60);
        String result;
        if(days < 1 && hours < 1) result = String.format("%sm", minutes);
        else if(days < 1) result = String.format("%sh%sm", hours, minutes);
        else result = String.format("%sd%sh%sm", days, hours, minutes);
        return result;
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        assert MinecraftClient.getInstance().player != null;
        ClientWorld world = MinecraftClient.getInstance().player.clientWorld;
        ClientWorld.Properties properties = world.getLevelProperties();
        int spawnX = properties.getSpawnX(); int spawnY = properties.getSpawnY(); int spawnZ = properties.getSpawnZ();
        String totalTime = formatTickTime(properties.getTime()); String dayTime = formatTickTime(properties.getTimeOfDay());
        String difficulty = properties.getDifficulty().getName();
        long totalPlayers = world.getPlayers().size(); long totalEntities = world.getRegularEntityCount();
        WorldBorder border = world.getWorldBorder();
        double borderSize = border.getSize();
        double borderEast = border.getBoundEast(); double borderWest = border.getBoundWest(); double borderNorth = border.getBoundNorth(); double borderSouth = border.getBoundSouth();
        double borderX = border.getCenterX(); double borderZ = border.getCenterZ();
        String message = String.format("""
\u00A7nSpawn:\u00A7r X:%s Y:%s Z:%s
\u00A7nTime:\u00A7r
    \u00A77- Total:\u00A7r %s
    \u00A77- Day:\u00A7r %s
\u00A7nDifficulty:\u00A7r %s
\u00A7nEntities:\u00A7r
    \u00A77- Total:\u00A7r %s
    \u00A77- Players:\u00A7r %s
\u00A7nWorld Border:\u00A7r
    \u00A77- Center:\u00A7r X:%s Z:%s
    \u00A77- Size:\u00A7r %s
    \u00A77- Bound:\u00A7r N:%s S:%s E:%s W:%s""",
                spawnX, spawnY, spawnZ,
                totalTime, dayTime,
                difficulty,
                totalEntities, totalPlayers,
                borderX, borderZ,
                borderSize,
                borderNorth, borderSouth, borderEast, borderWest);

        KonsoleLogger.getInstance().info(message);
        return 1;
    }
}
