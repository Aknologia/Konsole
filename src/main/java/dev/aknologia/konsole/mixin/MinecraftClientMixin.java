package dev.aknologia.konsole.mixin;

import dev.aknologia.konsole.interfaces.MinecraftClientMixinInterface;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SleepingChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.toast.TutorialToast;
import net.minecraft.client.tutorial.TutorialManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.ClientConnection;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements MinecraftClientMixinInterface {
    @Shadow protected int attackCooldown;

    @Shadow protected abstract boolean doAttack();

    @Shadow protected abstract void doItemUse();

    @Shadow protected abstract void doItemPick();

    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow private int itemUseCooldown;

    @Shadow private Profiler profiler;

    @Shadow @Final public InGameHud inGameHud;

    @Shadow private volatile boolean paused;

    @Shadow @Final public GameRenderer gameRenderer;

    @Shadow @Final private TutorialManager tutorialManager;

    @Shadow @Nullable public ClientWorld world;

    @Shadow @Nullable public HitResult crosshairTarget;

    @Shadow @Nullable public ClientPlayerInteractionManager interactionManager;

    @Shadow @Nullable public Screen currentScreen;

    @Shadow @Final private TextureManager textureManager;

    @Shadow public abstract void setScreen(@Nullable Screen screen);

    @Shadow @Final public GameOptions options;

    @Shadow @Nullable private Overlay overlay;

    @Shadow protected abstract void handleInputEvents();

    @Shadow @Final public WorldRenderer worldRenderer;

    @Shadow @Final private MusicTracker musicTracker;

    @Shadow @Final private SoundManager soundManager;

    @Shadow protected abstract boolean isConnectedToServer();

    @Shadow @Nullable private TutorialToast socialInteractionsToast;

    @Shadow @Final public ParticleManager particleManager;

    @Shadow @Nullable private ClientConnection integratedServerConnection;

    @Shadow @Final public Keyboard keyboard;

    public boolean doAttackMixed() {
        System.out.println(this.attackCooldown);
        System.out.println(this.player.isRiding());
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

    /**@author*/
    // Bullshit overwriting (That's a very bad solution, need to fix later)
    @Overwrite
    public void tick() {
        if (this.itemUseCooldown > 0) {
            --this.itemUseCooldown;
        }

        this.profiler.push("gui");
        this.inGameHud.tick(this.paused);
        this.profiler.pop();
        this.gameRenderer.updateTargetedEntity(1.0F);
        this.tutorialManager.tick(this.world, this.crosshairTarget);
        this.profiler.push("gameMode");
        if (!this.paused && this.world != null) {
            this.interactionManager.tick();
        }

        this.profiler.swap("textures");
        if (this.world != null) {
            this.textureManager.tick();
        }

        if (this.currentScreen == null && this.player != null) {
            if (this.player.isDead() && !(this.currentScreen instanceof DeathScreen)) {
                this.setScreen((Screen)null);
            } else if (this.player.isSleeping() && this.world != null) {
                this.setScreen(new SleepingChatScreen());
            }
        } else {
            Screen var2 = this.currentScreen;
            if (var2 instanceof SleepingChatScreen) {
                SleepingChatScreen sleepingChatScreen = (SleepingChatScreen)var2;
                if (!this.player.isSleeping()) {
                    sleepingChatScreen.closeChatIfEmpty();
                }
            }
        }

        if (this.currentScreen != null) {
            Screen.wrapScreenError(() -> {
                this.currentScreen.tick();
            }, "Ticking screen", this.currentScreen.getClass().getCanonicalName());
        }

        if (!this.options.debugEnabled) {
            this.inGameHud.resetDebugHudChunk();
        }

        if (this.overlay == null && (this.currentScreen == null || this.currentScreen.passEvents)) {
            this.profiler.swap("Keybindings");
            this.handleInputEvents();
            if (this.attackCooldown > 0) {
                --this.attackCooldown;
            }
        }

        if (this.world != null) {
            this.profiler.swap("gameRenderer");
            if (!this.paused) {
                this.gameRenderer.tick();
            }

            this.profiler.swap("levelRenderer");
            if (!this.paused) {
                this.worldRenderer.tick();
            }

            this.profiler.swap("level");
            if (!this.paused) {
                if (this.world.getLightningTicksLeft() > 0) {
                    this.world.setLightningTicksLeft(this.world.getLightningTicksLeft() - 1);
                }

                this.world.tickEntities();
            }
        } else if (this.gameRenderer.getShader() != null) {
            this.gameRenderer.disableShader();
        }

        if (!this.paused) {
            this.musicTracker.tick();
        }

        this.soundManager.tick(this.paused);
        if (this.world != null) {
            if (!this.paused) {
                if (!this.options.joinedFirstServer && this.isConnectedToServer()) {
                    Text sleepingChatScreen = new TranslatableText("tutorial.socialInteractions.title");
                    Text text = new TranslatableText("tutorial.socialInteractions.description", new Object[]{TutorialManager.keyToText("socialInteractions")});
                    this.socialInteractionsToast = new TutorialToast(net.minecraft.client.toast.TutorialToast.Type.SOCIAL_INTERACTIONS, sleepingChatScreen, text, true);
                    this.tutorialManager.add(this.socialInteractionsToast, 160);
                    this.options.joinedFirstServer = true;
                    this.options.write();
                }

                this.tutorialManager.tick();

                try {
                    this.world.tick(() -> {
                        return true;
                    });
                } catch (Throwable var4) {
                    CrashReport text = CrashReport.create(var4, "Exception in world tick");
                    if (this.world == null) {
                        CrashReportSection crashReportSection = text.addElement("Affected level");
                        crashReportSection.add("Problem", "Level is null!");
                    } else {
                        this.world.addDetailsToCrashReport(text);
                    }

                    throw new CrashException(text);
                }
            }

            this.profiler.swap("animateTick");
            if (!this.paused && this.world != null) {
                this.world.doRandomBlockDisplayTicks(this.player.getBlockX(), this.player.getBlockY(), this.player.getBlockZ());
            }

            this.profiler.swap("particles");
            if (!this.paused) {
                this.particleManager.tick();
            }
        } else if (this.integratedServerConnection != null) {
            this.profiler.swap("pendingConnection");
            this.integratedServerConnection.tick();
        }

        this.profiler.swap("keyboard");
        this.keyboard.pollDebugCrash();
        this.profiler.pop();
    }
}
