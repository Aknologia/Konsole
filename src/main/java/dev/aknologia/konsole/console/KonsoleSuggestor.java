package dev.aknologia.konsole.console;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.brigadier.Message;
import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.CommandManager;
import dev.aknologia.konsole.niflheim.CommandDispatcher;
import dev.aknologia.konsole.niflheim.ParseResults;
import dev.aknologia.konsole.niflheim.ParsedArgument;
import dev.aknologia.konsole.niflheim.StringReader;
import dev.aknologia.konsole.niflheim.context.CommandContextBuilder;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import dev.aknologia.konsole.niflheim.suggestion.Suggestion;
import dev.aknologia.konsole.niflheim.suggestion.SuggestionContext;
import dev.aknologia.konsole.niflheim.suggestion.Suggestions;
import joptsimple.internal.Strings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class KonsoleSuggestor {
    private static final Pattern BACKSLASH_S_PATTERN = Pattern.compile("(\\s+)");
    private static final Style ERROR_STYLE = Style.EMPTY.withColor(Formatting.RED);
    private static final Style INFO_STYLE = Style.EMPTY.withColor(Formatting.GRAY);
    private static final List<Style> HIGHLIGHT_STYLES = Stream.of(Formatting.AQUA, Formatting.YELLOW, Formatting.GREEN, Formatting.LIGHT_PURPLE, Formatting.GOLD).map(Style.EMPTY::withColor).collect(ImmutableList.toImmutableList());
    final MinecraftClient client;
    final KonsoleScreen owner;
    final TextFieldWidget textField;
    final TextRenderer textRenderer;
    final int inWindowIndexOffset;
    final int maxSuggestionSize;
    final boolean konsoleScreenSized;
    final int color;
    private final List<OrderedText> messages = Lists.newArrayList();
    private int x;
    private int width;
    @Nullable
    private ParseResults parse;
    @Nullable
    private Suggestions suggestions;
    @Nullable
    SuggestionWindow window;
    private boolean windowActive;
    boolean completingSuggestions;

    public KonsoleSuggestor(MinecraftClient minecraftClient, KonsoleScreen screen, TextFieldWidget textFieldWidget, TextRenderer textRenderer, int i, int j, boolean bl, int k) {
        this.client = minecraftClient;
        this.owner = screen;
        this.textField = textFieldWidget;
        this.textRenderer = textRenderer;
        this.inWindowIndexOffset = i;
        this.maxSuggestionSize = j;
        this.konsoleScreenSized = bl;
        this.color = k;
        textFieldWidget.setRenderTextProvider(this::provideRenderText);
    }

    public void setWindowActive(boolean windowActive) {
        this.windowActive = windowActive;
        if(!windowActive) {
            this.window = null;
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(this.window != null && this.window.keyPressed(keyCode, scanCode, modifiers)) return true;
        if(this.owner.getFocused() == this.textField && keyCode == 258) {
            this.showSuggestion();
            return true;
        }
        return false;
    }

    public boolean mouseScrolled(double amount) {
        return this.window != null && this.window.mouseScrolled(MathHelper.clamp(amount, -1.0, 1.0));
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.window != null && this.window.mouseClicked((int)mouseX, (int)mouseY, button);
    }

    public void showSuggestion() {
        Suggestions suggestions;
        if(this.suggestions != null && !(suggestions = this.suggestions).isEmpty()) {
            int i = 0;
            for(Suggestion suggestion : suggestions.getList()) {
                i = Math.max(i, this.textRenderer.getWidth(suggestion.getText()));
            }
            int j = MathHelper.clamp(this.textField.getCharacterX(suggestions.getRange().getStart()), 0, this.textField.getCharacterX(0) + this.textField.getInnerWidth() - i);
            int suggestion = this.konsoleScreenSized ? this.owner.height() - 12 : 72;
            this.window = new SuggestionWindow(j, suggestion, i, this.sortSuggestions(suggestions));
        }
    }

    private List<Suggestion> sortSuggestions(Suggestions suggestions) {
        String string = this.textField.getText().substring(0, this.textField.getCursor());
        int i = KonsoleSuggestor.getLastPlayerNameStart(string);
        String string2 = string.substring(i).toLowerCase(Locale.ROOT);
        ArrayList<Suggestion> list = Lists.newArrayList();
        for(Suggestion suggestion : suggestions.getList()) {
            if(suggestion.getText().startsWith(string2) || suggestion.getText().startsWith("minecraft:" + string2)) {
                list.add(suggestion);
            }
        }
        return list;
    }

    public void refresh() {
        String string = this.textField.getText();
        if(string.isEmpty() || string.isBlank())  {
            this.textField.setSuggestion(null);
            this.window = null;
            this.messages.clear();
            return;
        }
        if(this.parse != null && !this.parse.getReader().getString().equals(string)) {
            this.parse = null;
        }
        if(!this.completingSuggestions) {
            this.textField.setSuggestion(null);
            this.window = null;
        }
        this.messages.clear();
        StringReader stringReader = new StringReader(string);
        int i = this.textField.getCursor();
        int j;
        CommandDispatcher commandDispatcher = KonsoleClient.getCommandManager().getDispatcher();
        if(this.parse == null) {
            try {
                this.parse = commandDispatcher.parse(stringReader);
            } catch(CommandSyntaxException ignored) {

            }
        }
        int n = j = stringReader.getCursor();
        if(!(i < j || this.window != null && this.completingSuggestions)) {
            this.suggestions = commandDispatcher.getCompletionSuggestions(this.parse, this.textField.getText(), i);
            if(!this.suggestions.isEmpty()) this.show();
        }
    }

    private static int getLastPlayerNameStart(String input) {
        if(Strings.isNullOrEmpty(input)) return 0;
        int i = 0;
        Matcher matcher = BACKSLASH_S_PATTERN.matcher(input);
        while(matcher.find()) {
            i = matcher.end();
        }
        return i;
    }

    private static OrderedText formatException(CommandSyntaxException exception) {
        Text text = Texts.toText(exception.getRawMessage());
        String string = exception.getContext();
        if(string == null) {
            return text.asOrderedText();
        }
        return new TranslatableText("command.context.parse_error", text, exception.getCursor(), string).asOrderedText();
    }

    private void show() {
        if(this.parse != null && this.textField.getCursor() == this.textField.getText().length()) {
            assert this.suggestions != null;
            if(this.suggestions.isEmpty() && !this.parse.getExceptions().isEmpty()) {
                int i = 0;
                for(Map.Entry<Integer, CommandSyntaxException> entry : this.parse.getExceptions().entrySet()) {
                    CommandSyntaxException commandSyntaxException = entry.getValue();
                    if(commandSyntaxException.getType() == CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect()) {
                        ++i;
                        continue;
                    }
                    this.messages.add(KonsoleSuggestor.formatException(commandSyntaxException));
                }
                if(i > 0) {
                    this.messages.add(KonsoleSuggestor.formatException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().create()));
                }
            } else if(this.parse.getReader().canRead()) {
                this.messages.add(KonsoleSuggestor.formatException(Objects.requireNonNull(CommandManager.getException(this.parse))));
            }
        }
        this.x = 0;
        this.width = this.owner.width;

        if(this.messages.isEmpty()) {
            assert this.suggestions != null;
            if (this.suggestions.isEmpty()) {
                this.showUsages(Formatting.GRAY);
            }
        }
        this.window = null;
        assert this.suggestions != null;
        if(!this.suggestions.isEmpty()) {
            this.showSuggestion();
        }
    }

    private void showUsages(Formatting formatting) {
        if(this.parse == null) return;
        CommandContextBuilder commandContextBuilder = this.parse.getContext();
        SuggestionContext suggestionContext = commandContextBuilder.findSuggestionContext(this.textField.getText(), this.textField.getCursor());
        int startPos = suggestionContext != null && suggestionContext.startPos > 0 ? suggestionContext.startPos : 0;
        String usage = KonsoleClient.getCommandManager().getDispatcher().getUsage(commandContextBuilder.getCommand());
        Style style = Style.EMPTY.withColor(formatting);
        if(usage != null && usage.trim().length() > 0) {
            this.messages.add(OrderedText.styledForwardsVisitedString(usage, style));
            int i = this.textRenderer.getWidth(usage);
            this.x = MathHelper.clamp(this.textField.getCharacterX(startPos), 0, this.textField.getCharacterX(0) + this.textField.getInnerWidth() - i);
            this.width = i;
        }
    }

    private OrderedText provideRenderText(String original, int firstCharacterIndex) {
        if(this.parse != null) {
            return KonsoleSuggestor.highlight(this.parse, original, firstCharacterIndex);
        }
        return OrderedText.styledForwardsVisitedString(original, Style.EMPTY);
    }

    @Nullable
    static String getSuggestionSuffix(String original, String suggestion) {
        if(suggestion.startsWith(original)) {
            return suggestion.substring(original.length());
        }
        return null;
    }

    private static OrderedText highlight(ParseResults parse, String original, int firstCharacterIndex) {
        int m;
        ArrayList<OrderedText> list = Lists.newArrayList();
        int i = 0;
        int j = -1;
        CommandContextBuilder commandContextBuilder = parse.getContext();
        for(ParsedArgument<?> parsedArgument : commandContextBuilder.getArguments().values()) {
            int k;
            if(++j >= HIGHLIGHT_STYLES.size()) {
                j = 0;
            }
            if((k = Math.max(parsedArgument.getRange().getStart() - firstCharacterIndex, 0)) >= original.length()) break;
            int l = Math.min(parsedArgument.getRange().getEnd() - firstCharacterIndex, original.length());
            if(l <= 0) continue;
            list.add(OrderedText.styledForwardsVisitedString(original.substring(i, k), INFO_STYLE));
            list.add(OrderedText.styledForwardsVisitedString(original.substring(k, l), HIGHLIGHT_STYLES.get(j)));
            i = l;
        }
        if(parse.getReader().canRead() && (m = Math.max(parse.getReader().getCursor() - firstCharacterIndex, 0)) < original.length()) {
            int parsedArgument = Math.min(m + parse.getReader().getRemainingLength(), original.length());
            list.add(OrderedText.styledForwardsVisitedString(original.substring(i, m), INFO_STYLE));
            list.add(OrderedText.styledForwardsVisitedString(original.substring(m, parsedArgument), ERROR_STYLE));
            i = parsedArgument;
        }
        list.add(OrderedText.styledForwardsVisitedString(original.substring(i), INFO_STYLE));
        return OrderedText.concat(list);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY) {
        if(this.window != null) {
            this.window.render(matrices, mouseX, mouseY);
        } else {
            int i = 0;
            for(OrderedText orderedText : this.messages) {
                int j = this.konsoleScreenSized ? this.owner.height - 14 - 13 - 12 * i : 72 + 12 * i;
                DrawableHelper.fill(matrices, this.x - 1, j, this.x + this.width + 1, j + 12, this.color);
                this.textRenderer.drawWithShadow(matrices, orderedText, (float)this.x, (float)(j + 2), -1);
                ++i;
            }
        }
    }




    @Environment(EnvType.CLIENT)
    public class SuggestionWindow {
        private final Rect2i area;
        private final String typedText;
        private final List<Suggestion> suggestions;
        private int inWindowIndex;
        private int selection;
        private Vec2f mouse = Vec2f.ZERO;
        private boolean completed;

        SuggestionWindow(int i, int j, int k, List<Suggestion> list) {
            int l = i - 1;
            int m = KonsoleSuggestor.this.konsoleScreenSized ? j - 3 - Math.min(list.size(), KonsoleSuggestor.this.maxSuggestionSize) * 12 : j;
            this.area = new Rect2i(l, m, k + 1, Math.min(list.size(), KonsoleSuggestor.this.maxSuggestionSize) * 12);
            this.typedText = KonsoleSuggestor.this.textField.getText();
            this.suggestions = list;
            this.select(0);
        }

        public void render(MatrixStack matrices, int mouseX, int mouseY) {
            if(this.suggestions.size() < 1) return;
            Message l;
            int k;
            boolean bl4;
            int i = Math.min(this.suggestions.size(), KonsoleSuggestor.this.maxSuggestionSize);
            int j = 0xFFAAAAAA;
            boolean bl = this.inWindowIndex > 0;
            boolean bl2 = this.suggestions.size() > this.inWindowIndex + i;
            boolean bl3 = bl || bl2;
            boolean bl5 = bl4 = this.mouse.x != (float)mouseX || this.mouse.y != (float)mouseY;
            if(bl4) {
                this.mouse = new Vec2f(mouseX, mouseY);
            }
            if(bl3) {
                DrawableHelper.fill(matrices, this.area.getX(), this.area.getY() - 1, this.area.getX() + this.area.getWidth(), this.area.getY(), KonsoleSuggestor.this.color);
                DrawableHelper.fill(matrices, this.area.getX(), this.area.getY() + this.area.getHeight(), this.area.getX() + this.area.getWidth(), this.area.getY() + this.area.getHeight() + 1, KonsoleSuggestor.this.color);
                if(bl) {
                    for(k = 0; k < this.area.getWidth(); ++k) {
                        if(k%2 != 0) continue;
                        DrawableHelper.fill(matrices, this.area.getX() + k, this.area.getY() - 1, this.area.getX() + k + 1, this.area.getY(), -1);
                    }
                }
                if(bl2) {
                    for(k = 0; k < this.area.getWidth(); ++k) {
                        if(k%2 !=0) continue;
                        DrawableHelper.fill(matrices, this.area.getX() + k, this.area.getY() + this.area.getHeight(), this.area.getX() + k + 1, this.area.getY() + this.area.getHeight() + 1, -1);
                    }

                }
            }
            k = 0;
            for(int l2 = 0; l2 < i; ++l2) {
                Suggestion suggestion = this.suggestions.get(l2 + this.inWindowIndex);
                DrawableHelper.fill(matrices, this.area.getX(), this.area.getY() + 12 * l2, this.area.getX() + this.area.getWidth(), this.area.getY() + 12 * l2 + 12, KonsoleSuggestor.this.color);
                if(mouseX > this.area.getX() && mouseX < this.area.getX() + this.area.getWidth() && mouseY > this.area.getY() + 12 * l2 && mouseY < this.area.getY() + 12 * l2 + 12) {
                    if(bl4) {
                        this.select(l2 + this.inWindowIndex);
                    }
                    k = 1;
                }
                KonsoleSuggestor.this.textRenderer.drawWithShadow(matrices, suggestion.getText(), (float)(this.area.getX() + 1), (float)(this.area.getY() + 2 + 12 * l2), l2 + this.inWindowIndex == this.selection ? -256 : -5592406);
            }
            if(k != 0 && (l = this.suggestions.get(this.selection).getTooltip()) != null) {
                KonsoleSuggestor.this.owner.renderTooltip(matrices, Texts.toText(l), mouseX, mouseY);
            }
        }

        public boolean mouseClicked(int x, int y, int button) {
            if(!this.area.contains(x, y)) return false;
            int i = (y - this.area.getY()) / 12 + this.inWindowIndex;
            if(i >= 0 && i < this.suggestions.size()) {
                this.select(i);
                this.complete();
            }
            return true;
        }

        public boolean mouseScrolled(double amount) {
            int j;
            int i = (int)(KonsoleSuggestor.this.client.mouse.getX() * (double)KonsoleSuggestor.this.client.getWindow().getScaledWidth() / (double)KonsoleSuggestor.this.client.getWindow().getWidth());
            if(this.area.contains(i, j = (int)(KonsoleSuggestor.this.client.mouse.getY() * (double)KonsoleSuggestor.this.client.getWindow().getScaledHeight() / (double)KonsoleSuggestor.this.client.getWindow().getHeight()))) {
                this.inWindowIndex = MathHelper.clamp((int)((double)this.inWindowIndex - amount), 0, Math.max(this.suggestions.size() - KonsoleSuggestor.this.maxSuggestionSize, 0));
                return true;
            }
            return false;
        }

        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if(keyCode == GLFW.GLFW_KEY_UP) {
                this.scroll(-1);
                this.completed = false;
                return true;
            }
            if(keyCode == GLFW.GLFW_KEY_DOWN) {
                this.scroll(1);
                this.completed = false;
                return true;
            }
            if(keyCode == 258) {
                if(this.completed) {
                    this.scroll(Screen.hasShiftDown() ? -1 : 1);
                }
                this.complete();
                return true;
            }
            if(keyCode == 256) {
                this.discard();
                return true;
            }
            return false;
        }

        public void scroll(int offset) {
            this.select(this.selection + offset);
            int i = this.inWindowIndex;
            int j = this.inWindowIndex + KonsoleSuggestor.this.maxSuggestionSize - 1;
            if(this.selection < i) {
                this.inWindowIndex = MathHelper.clamp(this.selection, 0, Math.max(this.suggestions.size() - KonsoleSuggestor.this.maxSuggestionSize, 0));
            } else if(this.selection > j) {
                this.inWindowIndex = MathHelper.clamp(this.selection + KonsoleSuggestor.this.inWindowIndexOffset - KonsoleSuggestor.this.maxSuggestionSize, 0, Math.max(this.suggestions.size() - KonsoleSuggestor.this.maxSuggestionSize, 0));
            }
        }

        public void select(int index) {
            if(this.suggestions.isEmpty() || this.suggestions.size() < 1) return;
            this.selection = index;
            if(this.selection < 0) {
                this.selection += this.suggestions.size();
            }
            if(this.selection >= this.suggestions.size()) {
                this.selection -= this.suggestions.size();
            }
            Suggestion suggestion = this.suggestions.get(this.selection);
            KonsoleSuggestor.this.textField.setSuggestion(KonsoleSuggestor.getSuggestionSuffix(KonsoleSuggestor.this.textField.getText(), suggestion.apply(this.typedText)));
        }

        public void complete() {
            if(this.suggestions.isEmpty() || this.selection >= this.suggestions.size()){
                this.selection = this.suggestions.size();
                return;
            }
            Suggestion suggestion = this.suggestions.get(this.selection);
            KonsoleSuggestor.this.completingSuggestions = true;
            KonsoleSuggestor.this.textField.setText(suggestion.apply(this.typedText));
            int i = suggestion.getRange().getStart() + suggestion.getText().length();
            KonsoleSuggestor.this.textField.setSelectionStart(i);
            KonsoleSuggestor.this.textField.setSelectionEnd(i);
            this.select(this.selection);
            KonsoleSuggestor.this.completingSuggestions = false;
            this.completed = true;
        }

        public void discard() {
            KonsoleSuggestor.this.window = null;
            KonsoleSuggestor.this.showUsages(Formatting.GRAY);
        }
    }
}
