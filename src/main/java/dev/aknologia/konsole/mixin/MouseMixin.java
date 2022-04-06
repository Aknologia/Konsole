package dev.aknologia.konsole.mixin;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.arguments.types.Keys;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow
    MinecraftClient client;

    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if(window != this.client.getWindow().getHandle()) return;
        if(action == 1 && this.client.currentScreen == null && KonsoleClient.BINDS.containsKey(button)) {
            KonsoleClient.getKonsole().run(KonsoleClient.BINDS.get(button));
        }
    }

    @Inject(method = "onMouseScroll", at = @At("HEAD"))
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if(window == this.client.getWindow().getHandle() && this.client.currentScreen == null) {
            if(vertical > 0 && KonsoleClient.BINDS.containsKey(Keys.MWHEELUP))
                KonsoleClient.getKonsole().run(KonsoleClient.BINDS.get(Keys.MWHEELUP));
            else if(vertical < 0 && KonsoleClient.BINDS.containsKey(Keys.MWHEELDOWN))
                KonsoleClient.getKonsole().run(KonsoleClient.BINDS.get(Keys.MWHEELDOWN));
        }
    }
}
