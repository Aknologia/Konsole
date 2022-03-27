package dev.aknologia.konsole.command;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.CommandDispatcher;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DimensionCommand implements Command {
    public String name = "dimension";
    public String description = "Show informations about the current dimension.";
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
        String result = null;
        if(days < 1 && hours < 1) result = String.format("%sm", minutes);
        else {
            if(days < 1) result = String.format("%sh%sm", hours, minutes);
            else String.format("%sd%sh%sm", days, hours, minutes);
        }
        return result;
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        ClientWorld world = KonsoleClient.CLIENT.player.clientWorld;
        DimensionType dimension = world.getDimension();
        DimensionEffects effects = world.getDimensionEffects();

        String weather = "clear";
        if(world.isThundering()) weather = "thunder";
        else if(world.isRaining()) weather = "rain";

        String dimensionName = "UNKNOWN";
        if(world.getRegistryKey().equals(World.NETHER)) dimensionName = "NETHER";
        if(world.getRegistryKey().equals(World.END)) dimensionName = "END";
        if(world.getRegistryKey().equals(World.OVERWORLD)) dimensionName = "OVERWORLD";

        double scale = dimension.getCoordinateScale(); double height = dimension.getHeight(); double minY = dimension.getMinimumY();

        HashMap<String, Boolean> parameters = new HashMap<>();
        parameters.put("hasCeiling", dimension.hasCeiling()); parameters.put("hasFixedTime", dimension.hasFixedTime());
        parameters.put("hasEnderDragonFight", dimension.hasEnderDragonFight()); parameters.put("hasRaids", dimension.hasRaids());
        parameters.put("hasSkyLight", dimension.hasSkyLight()); parameters.put("isBedWorking", dimension.isBedWorking());
        parameters.put("isNatural", dimension.isNatural()); parameters.put("isPiglinSafe", dimension.isPiglinSafe());
        parameters.put("isRespawnAnchorWorking", dimension.isRespawnAnchorWorking()); parameters.put("isUltrawarm", dimension.isUltrawarm());
        String message = String.format("""
\u00A7l\u00A7b%s
\u00A7nWeather:\u00A7r %s
\u00A7nCoordinates:\u00A7r
    \u00A77- Scale:\u00A7r %s
    \u00A77- Height:\u00A7r %s
    \u00A77- Min Y:\u00A7r %s
\u00A7nParameters:\u00A7r\n""",
                dimensionName,
                weather,
                scale, height, minY);

        List<String> listParam = new ArrayList<>();
        parameters.keySet().forEach(k -> listParam.add(String.format("    \u00A77- %s:\u00A7%s %s", k, parameters.get(k) ? "a" : "c", parameters.get(k))));
        message = message.concat(String.join("\n", listParam));
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
