package dev.aknologia.konsole.convar;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.ConsoleVariable;

public class ScaleConVar extends ConsoleVariable<Float> {
    public ScaleConVar() {
        super("scale", "The scale of the console.", 1f, Float.class);
    }

    @Override
    public Object[] getArgumentParams() {
        return new Object[]{0};
    }

    @Override
    public void set(Object value) {
        super.set(value);
        KonsoleClient.getKonsoleScreen().onScaleChanged();
    }
}
