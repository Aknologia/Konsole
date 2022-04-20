package dev.aknologia.konsole.mixin;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.KonsoleLogger;
import dev.aknologia.konsole.console.Konsole;
import net.minecraft.client.gui.hud.ChatHudListener;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.UUID;

@Mixin(ChatHudListener.class)
public class ChatHudListenerMixin {
    @Inject(
            method = "onChatMessage",
            at = @At("HEAD")
    )
    public void onChatMessage(MessageType messageType, Text message, UUID sender, CallbackInfo ci) {
        int chatLogLevel = (int) Objects.requireNonNull(KonsoleClient.getKonsole().getConvarValue("chat_log_level"));
        if (chatLogLevel == 0) return;

        if(chatLogLevel > 0 && messageType == MessageType.SYSTEM) KonsoleLogger.getInstance().chat(messageType, message);
        else if(chatLogLevel > 1 && messageType == MessageType.GAME_INFO) KonsoleLogger.getInstance().chat(messageType, message);
        else if(chatLogLevel > 2 && messageType == MessageType.CHAT) KonsoleLogger.getInstance().chat(messageType, message);
    }
}
