package dev.aknologia.konsole.mixin;

import dev.aknologia.konsole.interfaces.MinecraftClientMixinInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements MinecraftClientMixinInterface {
    @Shadow protected int attackCooldown;

    @Shadow protected abstract boolean doAttack();

    @Shadow protected abstract void doItemUse();

    @Shadow protected abstract void doItemPick();

    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow @Nullable public ClientPlayerInteractionManager interactionManager;

    public boolean doAttackMixed() {
        return this.doAttack();
    }

    public void doAttackOrCooldown() {
        if(this.attackCooldown > 0 || this.player.getAttackCooldownProgress(0.5F) < 1.0F) return;
        this.doAttack();
    }

    public void doItemUseMixed() {
        this.doItemUse();
    }

    public void doItemPickMixed() {
        this.doItemPick();
    }
}
