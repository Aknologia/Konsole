package dev.aknologia.konsole.mixin;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.console.Konsole;
import net.minecraft.client.gui.hud.ChatHudListener;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ChatHudListener.class)
public class ChatHudListenerMixin {
    @Inject(
            method = "onChatMessage",
            at = @At("HEAD")
    )
    public void onChatMessage(MessageType messageType, Text message, UUID sender, CallbackInfo ci) {
        if(messageType == MessageType.CHAT) {
            KonsoleClient.getKonsole().addMessage(new LiteralText("\u00A7a[CHAT]\u00A7r ").append(message), false);
        }
    }
}
