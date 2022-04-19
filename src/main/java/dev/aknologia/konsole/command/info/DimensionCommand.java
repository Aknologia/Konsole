package dev.aknologia.konsole.command.info;

import dev.aknologia.konsole.KonsoleLogger;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DimensionCommand extends AbstractCommand {
    public DimensionCommand() {
        super("dimension", "Show information about the current dimension.", Category.INFO, List.of());
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        ClientWorld world = MinecraftClient.getInstance().player.clientWorld;
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
        KonsoleLogger.getInstance().info(message);
        return 1;
    }
}
