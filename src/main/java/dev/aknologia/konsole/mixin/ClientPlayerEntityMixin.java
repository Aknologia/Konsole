package dev.aknologia.konsole.mixin;

import dev.aknologia.konsole.command.action.SneakCommand;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(method = "isSneaking", at = @At("HEAD"), cancellable = true)
    public void isSneaking(CallbackInfoReturnable<Boolean> cir) {
        if(SneakCommand.toggledSneak) cir.setReturnValue(true);
    }
}
