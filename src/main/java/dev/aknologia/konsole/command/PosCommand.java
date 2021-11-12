package dev.aknologia.konsole.command;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.CommandDispatcher;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.text.LiteralText;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;

import java.util.ArrayList;
import java.util.List;

public class PosCommand implements Command {
    public String name = "pos";
    public String description = "Show informations about the current position.";
    public List<Argument> arguments = new ArrayList<>();

    @Override
    public void register(CommandDispatcher dispatcher) {
        dispatcher.register(this);
    }

    public double round2dec(double number) {
        return Math.round(number * 100.0) / 100.0;
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        ClientPlayerEntity player = KonsoleClient.CLIENT.player;
        Vec3d pos = player.getPos(); Vec3d eyePos = player.getEyePos();
        World world = KonsoleClient.CLIENT.world;
        float yaw = player.getYaw(); float pitch = player.getPitch();

        Entity entity = KonsoleClient.CLIENT.getCameraEntity();
        HitResult blockHit = entity.raycast(20.0, 0.0f, false);
        HitResult fluitHit = entity.raycast(20.0, 0.0f, true);

        BlockPos blockPos = entity.getBlockPos();
        Direction direction = entity.getHorizontalFacing();
        String string2 = switch (direction) {
            case NORTH -> "Towards negative Z";
            case SOUTH -> "Towards positive Z";
            case WEST -> "Towards negative X";
            case EAST -> "Towards positive X";
            default -> "Invalid";
        };

        List<String> lines = new ArrayList<>();

        lines.add(String.format("\u00A7nXYZ:\u00A7r %s / %s / %s", round2dec(entity.getX()), round2dec(entity.getY()), round2dec(entity.getZ())));
        lines.add(String.format("\u00A7nBlock:\u00A7r %s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()));
        lines.add(String.format("\u00A7nFacing:\u00A7r %s (%s) (%s / %s)", direction, string2, round2dec(entity.getYaw()), round2dec(entity.getPitch())));
        if(blockHit.getType() == HitResult.Type.BLOCK) {
            blockPos = ((BlockHitResult)blockHit).getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);
            lines.add(String.format("\u00A7nTargeted Block:\u00A7r %s, %s, %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            lines.add(String.format("   \u00A77-\u00A7r %s", String.valueOf(Registry.BLOCK.getId(((AbstractBlock.AbstractBlockState)blockState).getBlock()))));
        }

        if(fluitHit.getType() == HitResult.Type.BLOCK) {
            blockPos = ((BlockHitResult)fluitHit).getBlockPos();
            FluidState fluidState = world.getFluidState((BlockPos) blockPos);
            lines.add(String.format("\u00A7nTargeted Fluid:\u00A7r %s, %s, %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            lines.add(String.format("   \u00A77-\u00A7r %s", String.valueOf(Registry.FLUID.getId(((FluidState)fluidState).getFluid()))));
        }

        KonsoleClient.KONSOLE.addMessage(new LiteralText(String.join("\n", lines)));
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
