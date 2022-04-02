package dev.aknologia.konsole.convar;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.ConsoleVariable;
import net.minecraft.client.MinecraftClient;

public class HitboxConVar extends ConsoleVariable<Boolean> {
    public HitboxConVar() {
        super("hitbox", "Show/Hide hitboxes around entities.", false, Boolean.class);
    }

    @Override
    public Boolean get() {
        return KonsoleClient.CLIENT.getEntityRenderDispatcher().shouldRenderHitboxes();
    }

    @Override
    public void set(Object value) {
        KonsoleClient.CLIENT.getEntityRenderDispatcher().setRenderHitboxes((boolean) value);
    }
}
