package dev.aknologia.konsole.console;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.interfaces.KeyboardMixinInterface;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

@Environment(value=EnvType.CLIENT)
public class KonsoleScreen extends Screen {
    private static final Text USAGE_TEXT = new TranslatableText("konsole.usage");
    private final String konsoleLastMessage = "";
    private int messageHistoryOffset = 0;
    protected TextFieldWidget konsoleField;
    private String originalKonsoleText;
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    public float scale = 1.0F;
    public float multiplier = 1.0F / scale;
    KonsoleSuggestor konsoleSuggestor;

    public KonsoleScreen(String string) {
        super(new TranslatableText("konsole.title"));
        this.originalKonsoleText = string;
    }

    public KonsoleScreen(String string, MinecraftClient client) {
        super(new TranslatableText("konsole.title"));
        this.originalKonsoleText = string;
        this.client = client;
    }

    @Override
    protected void init() {
        this.originalKonsoleText = KonsoleClient.getKonsole().originalMessage;
        this.scale = this.getScale(); this.multiplier = 1.0F / this.scale;
        assert this.client != null;
        this.client.keyboard.setRepeatEvents(true);
        this.konsoleField = new TextFieldWidget(this.textRenderer, 4, this.height() - 12, this.width() - 4, 12, new TranslatableText("konsole.editBox"));
        this.konsoleField.setMaxLength(256);
        this.konsoleField.setDrawsBackground(false);
        this.konsoleField.setText(this.originalKonsoleText);
        this.konsoleField.setChangedListener(this::onKonsoleFieldUpdate);
        this.addSelectableChild(this.konsoleField);
        this.konsoleSuggestor = new KonsoleSuggestor(this.client, this, this.konsoleField, this.textRenderer, 1, 10, true, -805306368);
        this.konsoleSuggestor.refresh();
        this.setInitialFocus(this.konsoleField);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string = this.konsoleField.getText();
        this.init(client, width, height);
        this.setText(string);
        this.konsoleSuggestor.refresh();
    }

    @Override
    public void removed() {
        assert this.client != null;
        this.client.keyboard.setRepeatEvents(false);
        KonsoleClient.getKonsole().resetScroll();
    }

    @Override
    public void tick() { this.konsoleField.tick(); }

    private void onKonsoleFieldUpdate(String konsoleText) {
        String string = this.konsoleField.getText();
        this.konsoleSuggestor.setWindowActive(!string.equals(this.originalKonsoleText));
        this.konsoleSuggestor.refresh();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        assert this.client != null;
        if(((KeyboardMixinInterface)this.client.keyboard).isCtrlPressed()) {
            if(keyCode == GLFW.GLFW_KEY_A){
                this.konsoleField.setSelectionStart(0);
                this.konsoleField.setSelectionEnd(this.konsoleField.getText().length()-1);
                return true;
            }
        }
        if(this.konsoleSuggestor.keyPressed(keyCode, scanCode, modifiers)) return true;
        if(super.keyPressed(keyCode, scanCode, modifiers)) { return true; }
        if(keyCode == GLFW.GLFW_KEY_ESCAPE) {
            KonsoleClient.getKonsole().originalMessage = this.konsoleField.getText();
            this.client.setScreen(null);
            return true;
        }
        if(keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            String string = this.konsoleField.getText().trim();
            if(!string.isEmpty()) {
                KonsoleClient.getKonsole().addToMessageHistory(this.konsoleField.getText());
                KonsoleClient.getKonsole().addMessage(new LiteralText("> ").append(this.konsoleField.getText()).formatted(Formatting.GRAY));
                KonsoleClient.getKonsole().run(string);
            }
            this.konsoleField.setText("");
            this.originalKonsoleText = "";
            KonsoleClient.getKonsole().originalMessage = "";
            this.messageHistoryOffset = 0;
            return true;
        }
        if(keyCode == GLFW.GLFW_KEY_UP) {
            this.setKonsoleFromHistory(1);
            return true;
        }
        if(keyCode == GLFW.GLFW_KEY_DOWN) {
            this.setKonsoleFromHistory(-1);
            return true;
        }
        if(keyCode == GLFW.GLFW_KEY_PAGE_UP) {
            this.scroll(KonsoleClient.getKonsole().getVisibleLineCount() - 1d);
            return true;
        }
        if(keyCode == GLFW.GLFW_KEY_PAGE_DOWN) {
            this.scroll(-KonsoleClient.getKonsole().getVisibleLineCount() + 1d);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if(amount > 1.0) {
            amount = 1.0;
        }
        if(amount < -1.0) {
            amount = -1.0;
        }
        if(this.konsoleSuggestor.mouseScrolled(amount)) return true;
        if(!KonsoleScreen.hasShiftDown()) { amount *= 7.0; }
        this.scroll(amount);
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.konsoleSuggestor.mouseClicked(mouseX, mouseY, button)) return true;
        if(this.konsoleField.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void insertText(String text, boolean override) {
        if(override) {
            this.konsoleField.setText(text);
        } else {
            this.konsoleField.write(text);
        }
    }

    public void setKonsoleFromHistory(int offset) {
        int i = KonsoleClient.getKonsole().getMessageHistory().size();
        if(i == 0) return;
        int j = this.messageHistoryOffset + offset;
        --j;
        if(j < 0) j = 0;
        else if(j > i) j = i;
        List<String> history = KonsoleClient.getKonsole().getMessageHistory();
        Collections.reverse(history);
        this.konsoleField.setText(history.get(j));
        this.konsoleSuggestor.setWindowActive(false);
        this.messageHistoryOffset = j;
    }

    public void onScaleChanged() {
        this.init();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        matrices.scale(this.scale, this.scale, this.scale);
        this.setFocused(this.konsoleField);
        this.konsoleField.setTextFieldFocused(true);

        int width = this.width(); int height = this.height();

        int backgroundColor = 0xA0000000;

        // Fill output background
        KonsoleScreen.fill(matrices, 2, 2, width - 2, height - 16, backgroundColor);
        // Fill field background
        KonsoleScreen.fill(matrices, 2, height - 14, width - 2, height - 2, backgroundColor);

        this.konsoleField.render(matrices, mouseX, mouseY, delta);

        // Get All Lines
        List<KonsoleLine<Text>> visibleLines = this.getVisibleLines();

        ListIterator<KonsoleLine<Text>> timestampIterator = visibleLines.listIterator();
        // Render Timestamps
        while(timestampIterator.hasNext()) {
            int i = timestampIterator.nextIndex();
            KonsoleLine<Text> line = timestampIterator.next();
            if(line.getTimestamp() == 0L) continue;
            String timestamp = sdf.format(new Timestamp(line.getTimestamp()));
            KonsoleScreen.drawTextWithShadow(
                    matrices,
                    this.textRenderer,
                    new LiteralText(String.format("\u00A77\u00A7o%s\u00A7r ", timestamp)),
                    this.originX()*2,
                    height - 18 - i*(KonsoleClient.getKonsole().spaceBetweenLines + this.textRenderer.fontHeight) - this.textRenderer.fontHeight,
                    0xFFFFFF
            );
        }

        ListIterator<KonsoleLine<Text>> linesIterator = visibleLines.listIterator();
        //Render Lines
        while(linesIterator.hasNext()) {
            int i = linesIterator.nextIndex();
            KonsoleLine<Text> line = linesIterator.next();
            if(line == null || line.getText() == null) continue;

            KonsoleScreen.drawTextWithShadow(
                    matrices,
                    this.textRenderer,
                    line.getText(),
                    3*this.originX() + this.getTimestampWidth(),
                    height - 18 - i*(KonsoleClient.getKonsole().spaceBetweenLines + this.textRenderer.fontHeight) - this.textRenderer.fontHeight,
                    0xFFFFFF
            );
        }
        this.konsoleSuggestor.render(matrices, mouseX, mouseY);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public void scroll(double amount) {
        KonsoleClient.getKonsole().scrolledLines = (int)((double)KonsoleClient.getKonsole().scrolledLines + amount);
        int fullLines = KonsoleClient.getKonsole().messages.size();
        int visibleLines = KonsoleClient.getKonsole().getVisibleLineCount();
        if (KonsoleClient.getKonsole().scrolledLines > fullLines - visibleLines) {
            KonsoleClient.getKonsole().scrolledLines = fullLines - visibleLines;
        }
        if (KonsoleClient.getKonsole().scrolledLines <= 0) {
            KonsoleClient.getKonsole().scrolledLines = 0;
            KonsoleClient.getKonsole().hasUnreadNewMessages = false;
        }
    }

    public int originX() { return 2; }

    public int originY() { return 2; }

    public int getBoxWidth() { return this.width() - this.originX()*6; }

    public int width() {
        assert this.client != null;
        return Math.round(this.client.getWindow().getScaledWidth() * this.multiplier);
    }

    public int height() {
        assert this.client != null;
        return Math.round(this.client.getWindow().getScaledHeight() * this.multiplier);
    }

    private float getScale() {
        return (float) KonsoleClient.getKonsole().getConvarValue("scale");
    }

    public int getTimestampWidth() {
        assert this.client != null;
        return this.client.textRenderer.getWidth("00:00:00");
    }

    public int getLinesWidth() {
        return MathHelper.floor(this.getBoxWidth() - this.getTimestampWidth() - this.originX()*2);
    }

    public List<KonsoleLine<Text>> getVisibleLines() {
        int maxSize = KonsoleClient.getKonsole().getVisibleLineCount();
        List<KonsoleLine<Text>> lines = KonsoleClient.getKonsole().messages;
        if(lines.isEmpty()) return lines;
        if(lines.size() < KonsoleClient.getKonsole().scrolledLines + maxSize) return lines.subList(Math.max(lines.size() - maxSize - 1, 0), lines.size());
        return lines.subList(KonsoleClient.getKonsole().scrolledLines, KonsoleClient.getKonsole().scrolledLines + maxSize);
    }

    private void setText(String text) { this.konsoleField.setText(text); }

    @Override
    protected void addScreenNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, this.getTitle());
        builder.put(NarrationPart.USAGE, USAGE_TEXT);
        String string = this.konsoleField.getText();
        if (!string.isEmpty()) {
            builder.nextMessage().put(NarrationPart.TITLE, new TranslatableText("chat_screen.message", string));
        }
    }
}
