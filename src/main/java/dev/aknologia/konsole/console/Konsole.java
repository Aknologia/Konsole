package dev.aknologia.konsole.console;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import dev.aknologia.konsole.KonsoleClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Environment(EnvType.CLIENT)
public class Konsole {
    private final MinecraftClient client;
    public final List<String> messageHistory = Lists.newArrayList();
    public final List<KonsoleLine<Text>> messages = Lists.newArrayList();
    public final Deque<Text> messageQueue = Queues.newArrayDeque();
    public int scrolledLines;
    public boolean hasUnreadNewMessages;
    public long lastMessageAddedTime;
    public int spaceBetweenLines = 2;

    public Konsole(MinecraftClient minecraftClient) {
        this.client = minecraftClient;
    }

    public void clear(boolean clearHistory) {
        this.messageQueue.clear();
        this.messages.clear();
        if (clearHistory) {
            this.messageHistory.clear();
        }
    }

    public void addMessage(Text message) {
        this.addMessage(message, 0);
    }

    private void addMessage(Text message, int messageId) {
        this.addMessage(message, messageId, this.client.inGameHud.getTicks(), System.currentTimeMillis(), false);
        KonsoleClient.LOG.info("[CONSOLE] %s", (Object)message.getString().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n"));
    }

    private void addMessage(Text message, int messageId, int timestamp, long mlTimestamp, boolean refresh) {
        this.messages.add(0, new KonsoleLine<Text>(timestamp, message, messageId, mlTimestamp));
        while (this.messages.size() > 500) {
            this.messages.remove(this.messages.size() - 1);
        }
    }

    public void reset() {
        List<KonsoleLine<Text>> messagesCopy = this.messages;
        this.messages.clear();
        this.resetScroll();
        for (int i = messagesCopy.size() - 1; i >= 0; --i) {
            KonsoleLine<Text> konsoleLine = this.messages.get(i);
            this.addMessage(konsoleLine.getText(), konsoleLine.getId(), konsoleLine.getCreationTick(), konsoleLine.getTimestamp(), true);
        }
    }

    public List<String> getMessageHistory() {
        return this.messageHistory;
    }

    public void addToMessageHistory(String message) {
        if (this.messageHistory.isEmpty() || !this.messageHistory.get(this.messageHistory.size() - 1).equals(message)) {
            this.messageHistory.add(message);
        }
    }

    public void resetScroll() {
        this.scrolledLines = 0;
        this.hasUnreadNewMessages = false;
    }

    private void removeMessage(int messageId) {
        this.messages.removeIf(message -> message.getId() == messageId);
    }

    public int getWidth() { return this.client.getWindow().getScaledWidth() - 4; }

    public int getHeight() { return this.client.getWindow().getScaledHeight() - 20; }

    public int getVisibleLineCount() {
        return MathHelper.floor(this.getHeight() / (this.client.textRenderer.fontHeight + this.spaceBetweenLines));
    }

    public void run(@NotNull String text) {
        KonsoleClient.COMMAND_MANAGER.execute(text);
    }
}
