package dev.aknologia.konsole.mixin;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.interfaces.KeyboardMixinInterface;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin implements KeyboardMixinInterface {
    boolean ctrlPressed = false;
    @Shadow
    MinecraftClient client;

    @Inject(method = "onKey", at = @At("HEAD"))
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if(window != this.client.getWindow().getHandle()) return;

        if(key == GLFW.GLFW_KEY_LEFT_CONTROL) ctrlPressed = action > 0;

        if(this.client.currentScreen == null && KonsoleClient.BINDS.containsKey(key) && InputUtil.isKeyPressed(this.client.getWindow().getHandle(), key)) {
            KonsoleClient.KONSOLE.run(KonsoleClient.BINDS.get(key));
        }
    }

    public boolean isCtrlPressed() {
        return this.ctrlPressed;
    }
}
