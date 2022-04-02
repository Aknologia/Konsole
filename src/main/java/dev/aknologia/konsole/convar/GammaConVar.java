package dev.aknologia.konsole.convar;

import dev.aknologia.konsole.niflheim.ConsoleVariable;
import net.minecraft.client.MinecraftClient;

public class GammaConVar extends ConsoleVariable<Double> {
    public GammaConVar() {
        super("gamma", "Set the brightness setting.", 0.0, Double.class);
    }

    @Override
    public Object[] getArgumentParams() {
        return new Object[]{0}; // Set minimum to 0.
    }

    @Override
    public Double get() {
        return MinecraftClient.getInstance().options.gamma;
    }

    @Override
    public void set(Object value) {
        MinecraftClient.getInstance().options.gamma = (double) value;
    }
}
