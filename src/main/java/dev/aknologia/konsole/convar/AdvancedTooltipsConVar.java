package dev.aknologia.konsole.convar;

import dev.aknologia.konsole.niflheim.ConsoleVariable;
import net.minecraft.client.MinecraftClient;

public class AdvancedTooltipsConVar extends ConsoleVariable<Boolean> {
    public AdvancedTooltipsConVar() {
        super("advanced_tooltips", "Show/Hide advanced tooltips.", false, Boolean.class);
    }

    @Override
    public Boolean get() {
        return MinecraftClient.getInstance().options.advancedItemTooltips;
    }

    @Override
    public void set(Object value) {
        MinecraftClient.getInstance().options.advancedItemTooltips = (boolean) value;
    }
}
