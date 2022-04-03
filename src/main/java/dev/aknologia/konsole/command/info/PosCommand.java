package dev.aknologia.konsole.command.info;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.command.InfoCategory;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.property.Property;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.*;
import java.util.stream.Stream;

public class PosCommand implements Command {
    public String name = "pos";
    public String description = "Show informations about the current position.";
    public Class<?> category = InfoCategory.class;
    public List<Argument> arguments = new ArrayList<>();

    private double round2dec(double number) {
        return Math.round(number * 100.0) / 100.0;
    }

    private String propertyToString(Map.Entry<Property<?>, Comparable<?>> propEntry) {
        Property<?> property = propEntry.getKey();
        Comparable<?> comparable = propEntry.getValue();
        Object string = Util.getValueAsString(property, comparable);
        if(Boolean.TRUE.equals(comparable)) {
            string = Formatting.GREEN + (String)string;
        } else if(Boolean.FALSE.equals(comparable)) {
            string = Formatting.RED + (String)string;
        }
        return property.getName() + ": " + (String)string;
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        ClientPlayerEntity player = KonsoleClient.CLIENT.player;
        Vec3d pos = player.getPos(); Vec3d eyePos = player.getEyePos();
        World world = KonsoleClient.CLIENT.world;
        float yaw = player.getYaw(); float pitch = player.getPitch();

        Entity entity = KonsoleClient.CLIENT.getCameraEntity();
        HitResult blockHit = entity.raycast(20.0, 0.0f, false);
        HitResult fluidHit = entity.raycast(20.0, 0.0f, true);

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
            lines.add(String.format("    \u00A77-\u00A7r %s", String.valueOf(Registry.BLOCK.getId(((AbstractBlock.AbstractBlockState)blockState).getBlock()))));
            if(!blockState.getEntries().isEmpty()) {
                lines.add("    \u00A77- Properties:\u00A7r");
                for (Map.Entry entry : blockState.getEntries().entrySet()) {
                    lines.add(String.format("        \u00A7r %s", this.propertyToString(entry)));
                }
            }

            Stream tagStream = blockState.streamTags().map((tagKey) -> String.format("        \u00A7r #%s", tagKey));
            List<String> tagList = new ArrayList();
            tagStream.forEach(tag -> tagList.add((String) tag));
            if(!tagList.isEmpty()){
                lines.add("    \u00A77- Tags:\u00A7r");
                lines.addAll(tagList);
            }
        }


        if(fluidHit.getType() == HitResult.Type.BLOCK) {
            blockPos = ((BlockHitResult)fluidHit).getBlockPos();
            FluidState fluidState = world.getFluidState((BlockPos) blockPos);
            lines.add(String.format("\u00A7nTargeted Fluid:\u00A7r %s, %s, %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            lines.add(String.format("    \u00A77-\u00A7r %s", String.valueOf(Registry.FLUID.getId(((FluidState)fluidState).getFluid()))));
            if(!fluidState.getEntries().isEmpty()) {
                lines.add("    \u00A77- Properties:\u00A7r");
                for (Map.Entry entry : fluidState.getEntries().entrySet()) {
                    lines.add(String.format("       \u00A7r %s", this.propertyToString(entry)));
                }
            }
            Stream fluidTagStream = fluidState.streamTags().map((tagKey) -> String.format("        \u00A7r #%s", tagKey));
            List<String> tagList = new ArrayList();
            fluidTagStream.forEach(tag -> tagList.add((String) tag));
            if(!tagList.isEmpty()){
                lines.add("    \u00A77- Tags:\u00A7r");
                lines.addAll(tagList);
            }
        }

        if(KonsoleClient.CLIENT.targetedEntity != null) {
            lines.add(String.format("\u00A7nTargeted Entity:\u00A7r %s", String.valueOf(Registry.ENTITY_TYPE.getId(((Entity)KonsoleClient.CLIENT.targetedEntity).getType()))));
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

    @Override
    public Class<Category> getCategory() { return (Class<Category>) this.category; }
}
