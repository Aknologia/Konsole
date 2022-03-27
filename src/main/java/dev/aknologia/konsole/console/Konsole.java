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
    public String originalMessage = "";
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
        KonsoleClient.LOG.info("[CONSOLE] %s", (Object)message.getString().replaceAll("\r", "\\\\r"));
    }

    private void addMessage(Text message, int messageId, int timestamp, long mlTimestamp, boolean refresh) {
        this.formatAndAdd(message, messageId, timestamp, mlTimestamp);
        while (this.messages.size() > 500) {
            this.messages.remove(this.messages.size() - 1);
        }
    }

    private void formatAndAdd(Text message, int messageId, int timestamp, long mlTimestamp) {
        List<KonsoleLine<Text>> lineList = new ArrayList<>();
        String base = message.getString();
        String[] fullParts = base.split("\n");
        Integer parentId = null;
        for(int m = 0; m < fullParts.length; m++) {
            String sum = fullParts[m];
            int iterations = 0;
            while(this.client.textRenderer.getWidth(sum) > KonsoleClient.SCREEN_INSTANCE.getLinesWidth()) {
                int maxIndex = 0;
                for(int i = 0; i < sum.length(); i++) {
                    if(this.client.textRenderer.getWidth(sum.substring(0, i)) < KonsoleClient.SCREEN_INSTANCE.getLinesWidth()) maxIndex = i;
                    else break;
                }
                String sub = sum.substring(0, maxIndex);
                if(iterations == 0 && parentId == null) {
                    lineList.add(new KonsoleLine<Text>(timestamp, new LiteralText(sub).setStyle(message.getStyle()), messageId, mlTimestamp));
                    parentId = messageId;
                } else lineList.add(new KonsoleLine<Text>(parentId, new LiteralText(sub).setStyle(message.getStyle())));
                sum = sum.substring(maxIndex);
                iterations++;
            }
            if(sum.trim().length() > 0) {
                if(parentId == null) {
                    lineList.add(new KonsoleLine<Text>(timestamp, new LiteralText(sum).setStyle(message.getStyle()), messageId, mlTimestamp));
                    parentId = messageId;
                } else lineList.add(new KonsoleLine<Text>(parentId, new LiteralText(sum).setStyle(message.getStyle())));
            }
        }
        Collections.reverse(lineList);
        this.messages.addAll(0, lineList);
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
