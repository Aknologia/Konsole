package dev.aknologia.konsole.convar;

import dev.aknologia.konsole.niflheim.ConsoleVariable;
import net.minecraft.client.MinecraftClient;

public class HUDHiddenConVar extends ConsoleVariable<Boolean> {
    public HUDHiddenConVar() {
        super("hud_hidden", "Show/Hide the HUD.", false, Boolean.class);
    }

    @Override
    public Boolean get() {
        return MinecraftClient.getInstance().options.hudHidden;
    }

    @Override
    public void set(Object value) {
        MinecraftClient.getInstance().options.hudHidden = (boolean) value;
    }
}
