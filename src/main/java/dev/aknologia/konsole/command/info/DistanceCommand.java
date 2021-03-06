package dev.aknologia.konsole.command.info;

import dev.aknologia.konsole.KonsoleLogger;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.DoubleArgumentType;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class DistanceCommand extends AbstractCommand {
    public DistanceCommand() {
        super("distance", "Calculates the distance between 2 positions.", Category.UTILITY, List.of(
                new Argument("x1", DoubleArgumentType.doubleArg(), true),
                new Argument("y1", DoubleArgumentType.doubleArg(), true),
                new Argument("z1", DoubleArgumentType.doubleArg(), true),
                new Argument("x2", DoubleArgumentType.doubleArg()),
                new Argument("y2", DoubleArgumentType.doubleArg()),
                new Argument("z2", DoubleArgumentType.doubleArg())
        ));
    }

    private String vec3dToString(Vec3d vec3d) {
        return String.format("X:\u00A7b%.2f\u00A7r Y:\u00A7b%.2f\u00A7r Z:\u00A7b%.2f\u00A7r", vec3d.getX(), vec3d.getY(), vec3d.getZ());
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        Vec3d firstPos = new Vec3d(DoubleArgumentType.getDouble(context, "x1"), DoubleArgumentType.getDouble(context, "y1"), DoubleArgumentType.getDouble(context, "z1"));
        assert MinecraftClient.getInstance().player != null;
        Vec3d secondPos = MinecraftClient.getInstance().player.getPos();
        try {
            secondPos = new Vec3d(DoubleArgumentType.getDouble(context, "x2"), DoubleArgumentType.getDouble(context, "y2"), DoubleArgumentType.getDouble(context, "z2"));
        } catch(IllegalArgumentException ignored) { }
        double distance = Math.sqrt(Math.pow(secondPos.getX() - firstPos.getX(), 2) + Math.pow(secondPos.getY() - firstPos.getY(), 2) + Math.pow(secondPos.getZ() - firstPos.getZ(), 2));
        KonsoleLogger.getInstance().info(String.format("(%s) to (%s)\n\u00A76\u00A7nDistance:\u00A7r %.2f block(s)", this.vec3dToString(firstPos), this.vec3dToString(secondPos), distance));
        return 1;
    }
}
