package dev.aknologia.konsole.convar;

import dev.aknologia.konsole.interfaces.DebugRendererMixinInterface;
import dev.aknologia.konsole.niflheim.ConsoleVariable;
import net.minecraft.client.MinecraftClient;

public class ChunkBorderConVar extends ConsoleVariable<Boolean> {
    public ChunkBorderConVar() {
        super("chunkborder", "Show/Hide chunk borders.", false, Boolean.class);
    }

    @Override
    public Boolean get() {
        return ((DebugRendererMixinInterface) MinecraftClient.getInstance().debugRenderer).shouldShowChunkborder();
    }

    @Override
    public void set(Object value) {
        ((DebugRendererMixinInterface) MinecraftClient.getInstance().debugRenderer).setShowChunkborder((boolean) value);
    }
}
