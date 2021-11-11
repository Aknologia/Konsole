package dev.aknologia.konsole.command;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.CommandDispatcher;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.border.WorldBorder;

import java.time.Duration;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorldCommand implements Command {
    public String name = "world";
    public String description = "Show informations about the current world.";
    public List<Argument> arguments = new ArrayList<>();

    @Override
    public void register(CommandDispatcher dispatcher) {
        dispatcher.register(this);
    }

    private String formatTickTime(long time) {
        long sec = Math.round(time/2.4e4*86400);
        int days = MathHelper.floor(sec / 86400);
        int hours = MathHelper.floor((sec - days * 86400) / 3600);
        int minutes = MathHelper.floor((sec - days * 86400 - hours * 3600) / 60);
        String result = "N/A";
        if(days < 1 && hours < 1) result = String.format("%sm", minutes);
        else if(days < 1) result = String.format("%sh%sm", hours, minutes);
        else String.format("%sd%sh%sm", days, hours, minutes);
        return result;
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        ClientWorld world = KonsoleClient.CLIENT.player.clientWorld;
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

        KonsoleClient.KONSOLE.addMessage(new LiteralText(message));
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
}
