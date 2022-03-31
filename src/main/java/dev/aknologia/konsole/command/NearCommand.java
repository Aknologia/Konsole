package dev.aknologia.konsole.command;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.CommandDispatcher;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.IntegerArgumentType;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class NearCommand implements Command {
    public String name = "near";
    public String description = "Show all living entities in the specified radius (default: 50).";
    public List<Argument> arguments = new ArrayList<>();

    @Override
    public void register(CommandDispatcher dispatcher) {
        arguments.add(new Argument("radius", IntegerArgumentType.integer(1, 300)));
        dispatcher.register(this);
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        int radius = 50;
        try { radius = context.getArgument("radius", Integer.class); } catch(IllegalArgumentException ignored) {}
        List<LivingEntity> entities = MinecraftClient.getInstance().player.world.getNonSpectatingEntities(LivingEntity.class, Box.of(MinecraftClient.getInstance().player.getPos(), radius, radius, radius));
        HashMap<LivingEntity, Integer> entityMap = new HashMap<>();
        entities.forEach(le -> this.addOrIncrement(entityMap, le));

        List<String> entityNames = new ArrayList<>();
        entityMap.keySet().stream().toList().forEach(le -> {
            String name = (le.hasCustomName() ? le.getCustomName() : le.getDisplayName().toString().isEmpty() || le.getDisplayName().toString().isBlank() ? le.getName() : le.getDisplayName()).asString();
            String quantity = " \u00A7rx\u00A7b" + entityMap.get(le).toString();
            if(entityMap.get(le) < 2) quantity = "";
            entityNames.add(name + quantity);
        });
        KonsoleClient.KONSOLE.addMessage(new LiteralText(String.join(", ", entityNames)));
        return 1;
    }

    private boolean addOrIncrement(HashMap<LivingEntity, Integer> entityMap, LivingEntity livingEntity) {
        if(livingEntity.getId() == MinecraftClient.getInstance().player.getId()) return false;
        List<LivingEntity> keyList = entityMap.keySet().stream().toList();
        for(int i = 0; i < keyList.size(); i++) {
            LivingEntity le = keyList.get(i);
            if(this.isEqual(livingEntity, le)) {
                entityMap.put(le, entityMap.get(le) + 1);
                return true;
            }
        }
        entityMap.put(livingEntity, 1);
        return false;
    }

    private boolean isEqual(LivingEntity e1, LivingEntity e2) {
        if(e1.getDisplayName() != e2.getDisplayName()) return false;
        if(e1.getName() != e2.getName()) return false;
        if(e1.hasCustomName() != e2.hasCustomName() || e1.getCustomName() != e2.getCustomName()) return false;
        if(e1.getType() != e2.getType()) return false;
        return true;
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
