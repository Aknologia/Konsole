package dev.aknologia.konsole.console;

import dev.aknologia.konsole.KonsoleClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

@Environment(value=EnvType.CLIENT)
public class KonsoleScreen extends Screen {
    private static final Text USAGE_TEXT = new TranslatableText("konsole.usage");
    private String konsoleLastMessage = "";
    private int messageHistorySize = -1;
    protected TextFieldWidget konsoleField;
    private final String originalKonsoleText;
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public KonsoleScreen(String string) {
        super(new TranslatableText("konsole.title"));
        this.originalKonsoleText = string;
    }

    @Override
    protected void init() {
        this.client.keyboard.setRepeatEvents(true);
        this.messageHistorySize = KonsoleClient.KONSOLE.getMessageHistory().size();
        this.konsoleField = new TextFieldWidget(this.textRenderer, 4, this.height - 12, this.width - 4, 12, (Text) new TranslatableText("konsole.editBox")) {
            @Override
            protected MutableText getNarrationMessage() {
                return super.getNarrationMessage().append("E");
            }
        };
        this.konsoleField.setMaxLength(256);
        this.konsoleField.setDrawsBackground(false);
        this.konsoleField.setText(this.originalKonsoleText);
        this.addSelectableChild(this.konsoleField);
        this.setInitialFocus(this.konsoleField);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string = this.konsoleField.getText();
        this.init(client, width, height);
        this.setText(string);
    }

    @Override
    public void removed() {
        this.client.keyboard.setRepeatEvents(false);
        KonsoleClient.KONSOLE.resetScroll();
    }

    @Override
    public void tick() { this.konsoleField.tick(); }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(super.keyPressed(keyCode, scanCode, modifiers)) { return true; }
        if(keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.client.setScreen(null);
            return true;
        }
        if(keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            String string = this.konsoleField.getText().trim();
            if(!string.isEmpty()) {
                KonsoleClient.KONSOLE.run(string);
            }
            this.konsoleField.setText("");
            return true;
        }
        if(keyCode == GLFW.GLFW_KEY_UP) {
            this.setKonsoleFromHistory(-1);
            return true;
        }
        if(keyCode == GLFW.GLFW_KEY_DOWN) {
            this.setKonsoleFromHistory(1);
            return true;
        }
        if(keyCode == GLFW.GLFW_KEY_PAGE_UP) {
            KonsoleClient.KONSOLE.scroll(KonsoleClient.KONSOLE.getVisibleLineCount() - 1);
            return true;
        }
        if(keyCode == GLFW.GLFW_KEY_PAGE_DOWN) {
            KonsoleClient.KONSOLE.scroll(-KonsoleClient.KONSOLE.getVisibleLineCount() + 1);
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
        if(!KonsoleScreen.hasShiftDown()) { amount *= 7.0; }
        KonsoleClient.KONSOLE.scroll(amount);
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
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
        int i = this.messageHistorySize + offset;
        int j = KonsoleClient.KONSOLE.getMessageHistory().size();
        if((i = MathHelper.clamp(i, 0, j)) == this.messageHistorySize) {
            return;
        }
        if(i == j) {
            this.messageHistorySize = j;
            this.konsoleField.setText(this.konsoleLastMessage);
            return;
        }
        if(this.messageHistorySize == j) {
            this.konsoleLastMessage = this.konsoleField.getText();
        }
        this.konsoleField.setText(KonsoleClient.KONSOLE.getMessageHistory().get(i));
        this.messageHistorySize = i;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.setFocused(this.konsoleField);
        this.konsoleField.setTextFieldFocused(true);

        int backgroundColor = 0xA0000000;

        // Fill output background
        KonsoleScreen.fill(matrices, 2, 2, this.width - 2, this.height - 16, backgroundColor);
        // Fill field background
        KonsoleScreen.fill(matrices, 2, this.height - 14, this.width - 2, this.height - 2, backgroundColor);

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
                    this.height - 18 - i*(KonsoleClient.KONSOLE.spaceBetweenLines + this.textRenderer.fontHeight) - this.textRenderer.fontHeight,
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
                    this.height - 18 - i*(KonsoleClient.KONSOLE.spaceBetweenLines + this.textRenderer.fontHeight) - this.textRenderer.fontHeight,
                    0xFFFFFF
            );
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    public int originX() { return 2; }

    public int originY() { return 2; }

    public int getBoxWidth() { return this.width() - this.originX()*4; }

    private int width() {
        return this.client.getWindow().getScaledWidth() - this.originX()*2;
    }

    public int getTimestampWidth() {
        return this.textRenderer.getWidth("00:00:00");
    }

    public int getLinesWidth() {
        return MathHelper.floor(this.getBoxWidth() - this.getTimestampWidth() - this.originX()*2);
    }

    public List<KonsoleLine<Text>> getVisibleLines() {
        int maxSize = KonsoleClient.KONSOLE.getVisibleLineCount();
        List<KonsoleLine<Text>> visibleLines = new ArrayList<>();
        if(KonsoleClient.KONSOLE.messages.size() < 1) return visibleLines;
        int indexCursor = KonsoleClient.KONSOLE.scrolledLines;
        while(visibleLines.size() < maxSize && KonsoleClient.KONSOLE.messages.size() > indexCursor) {
            KonsoleLine<Text> message = KonsoleClient.KONSOLE.messages.get(indexCursor);
            List<KonsoleLine<Text>> lineList = new ArrayList<>();
            if(message == null) continue;
            Text text = message.getText();
            String base = text.getString();
            String[] fullParts = base.split("\n");
            Integer parentId = null;
            for(int m = 0; m < fullParts.length; m++) {
                String sum = fullParts[m];

                int iterations = 0;
                while (this.textRenderer.getWidth(sum) > this.getLinesWidth()) {
                    int maxIndex = 0;
                    for (int i = 0; i < sum.length(); i++) {
                        if (this.textRenderer.getWidth(sum.substring(0, i)) < this.getLinesWidth()) maxIndex = i;
                        else break;
                    }
                    String sub = sum.substring(0, maxIndex);
                    if (iterations == 0 && parentId == null) {
                        lineList.add(new KonsoleLine<Text>(message.getCreationTick(), new LiteralText(sub).setStyle(text.getStyle()), message.getId(), message.getTimestamp()));
                        parentId = message.getId();
                    } else
                        lineList.add(new KonsoleLine<Text>(parentId, new LiteralText(sub).setStyle(text.getStyle())));
                    sum = sum.substring(maxIndex);
                    iterations++;
                }
                if (sum.trim().length() > 0) {
                    if (parentId == null) {
                        lineList.add(new KonsoleLine<Text>(message.getCreationTick(), new LiteralText(sum).setStyle(text.getStyle()), message.getId(), message.getTimestamp()));
                        parentId = message.getId();
                    }
                    else
                        lineList.add(new KonsoleLine<Text>(parentId, new LiteralText(sum).setStyle(text.getStyle())));
                }
            }
            Collections.reverse(lineList);
            visibleLines.addAll(lineList);
            indexCursor++;
        }
        return visibleLines;
    }

    @Override
    public boolean isPauseScreen() { return false; }

    private void setText(String text) { this.konsoleField.setText(text); }

    @Override
    protected void addScreenNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, this.getTitle());
        builder.put(NarrationPart.USAGE, USAGE_TEXT);
        String string = this.konsoleField.getText();
        if (!string.isEmpty()) {
            builder.nextMessage().put(NarrationPart.TITLE, (Text)new TranslatableText("chat_screen.message", string));
        }
    }
}
