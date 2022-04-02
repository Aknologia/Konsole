package dev.aknologia.konsole.mixin;

import dev.aknologia.konsole.interfaces.DebugRendererMixinInterface;
import net.minecraft.client.render.debug.DebugRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin implements DebugRendererMixinInterface {
    @Shadow private boolean showChunkBorder;

    public boolean shouldShowChunkborder() { return this.showChunkBorder; }

    public void setShowChunkborder(boolean value) { this.showChunkBorder = value; }

}
