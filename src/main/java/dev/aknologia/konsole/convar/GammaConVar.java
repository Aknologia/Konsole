package dev.aknologia.konsole.convar;

import dev.aknologia.konsole.niflheim.ConsoleVariable;
import net.minecraft.client.MinecraftClient;

public class GammaConVar extends ConsoleVariable<Integer> {
    public GammaConVar() {
        super("gamma", "Set the brightness setting.", 0, Integer.class);
    }

    @Override
    public Integer get() {
        return (int) MinecraftClient.getInstance().options.gamma;
    }

    @Override
    public void set(Object value) {
        MinecraftClient.getInstance().options.gamma = (double) value;
    }
}
