package dev.aknologia.konsole.interfaces;

public interface MinecraftClientMixinInterface {
    boolean doAttackMixed();
    void doAttackOrCooldown();
    void doItemUseMixed();
    void doItemPickMixed();
}
