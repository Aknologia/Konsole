package dev.aknologia.konsole.command.info;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.IntegerArgumentType;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class NearCommand extends AbstractCommand {
    public NearCommand() {
        super("near", "Show all living entities in the specified radius (default: 50).", Category.INFO, List.of(
                new Argument("box_radius", IntegerArgumentType.integer(1, 300))
        ));
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        int radius = 50;
        try { radius = context.getArgument("box_radius", Integer.class); } catch(IllegalArgumentException ignored) {}
        assert MinecraftClient.getInstance().player != null;
        List<LivingEntity> entities = MinecraftClient.getInstance().player.world.getNonSpectatingEntities(LivingEntity.class, Box.of(MinecraftClient.getInstance().player.getPos(), radius, radius, radius));
        HashMap<LivingEntity, Integer> entityMap = new HashMap<>();
        List<LivingEntity> players = new ArrayList<>();
        entities.forEach(le -> {
            if(le instanceof PlayerEntity) {
                if (le.getId() != MinecraftClient.getInstance().player.getId()) players.add(le);
            } else this.addOrIncrement(entityMap, le);
        });


        List<String> entityNames = new ArrayList<>();
        entityMap.keySet().stream().toList().forEach(le -> {
            String name = le.getName().getString();
            if(le.hasCustomName()) name = Objects.requireNonNull(le.getCustomName()).getString();
            String quantity = " \u00A7rx\u00A7b" + entityMap.get(le).toString() + "\u00A7r";
            if(entityMap.get(le) < 2) quantity = "";
            entityNames.add(name + quantity);
        });
        List<String> playerNames = new ArrayList<>();
        players.forEach(pl -> playerNames.add(pl.getDisplayName().asString()));
        KonsoleClient.getKonsole().addMessage(new LiteralText(String.format("\u00A76\u00A7nBox Radius:\u00A7r\u00A7b %s\n\u00A76\u00A7nPlayers:\u00A7r %s\n\u00A76\u00A7nLiving Entities:\u00A7r %s", radius, playerNames.isEmpty() ? "\u00A77\u00A7onone\u00A7r" : String.join(", ", playerNames), entityNames.isEmpty() ? "\u00A77\u00A7onone\u00A7r" : String.join(", ", entityNames))));
        return 1;
    }

    private void addOrIncrement(HashMap<LivingEntity, Integer> entityMap, LivingEntity livingEntity) {
        List<LivingEntity> keyList = entityMap.keySet().stream().toList();
        for(int i = 0; i < keyList.size(); i++) {
            LivingEntity le = keyList.get(i);
            if(le.getType() == livingEntity.getType() && (!le.hasCustomName() || !livingEntity.hasCustomName() || le.getCustomName() == livingEntity.getCustomName())) {
                entityMap.put(le, entityMap.get(le) + 1);
                return;
            }
        }
        entityMap.put(livingEntity, 1);
    }
}
