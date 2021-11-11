package dev.aknologia.konsole.util;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.TextCollector;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Environment(value = EnvType.CLIENT)
public class KonsoleMessages {
    private static final OrderedText SPACES = OrderedText.styled(32, Style.EMPTY);

    private static String getRenderedKonsoleMessage(String message) {
        return MinecraftClient.getInstance().options.chatColors ? message : Formatting.strip(message);
    }

    public static List<OrderedText> breakRenderedKonsoleMessageLines(StringVisitable message2, int width, TextRenderer textRenderer) {
        TextCollector textCollector = new TextCollector();
        message2.visit((style, message) -> {
            textCollector.add(StringVisitable.styled(KonsoleMessages.getRenderedKonsoleMessage(message), style));
            return Optional.empty();
        }, Style.EMPTY);
        ArrayList<OrderedText> list = Lists.newArrayList();
        textRenderer.getTextHandler().wrapLines(textCollector.getCombined(), width, Style.EMPTY, (stringVisitable, boolean_) -> {
            OrderedText orderedText = Language.getInstance().reorder((StringVisitable)stringVisitable);
            list.add(boolean_ != false ? OrderedText.concat(SPACES, orderedText) : orderedText);
        });
        if (list.isEmpty()) {
            return Lists.newArrayList(OrderedText.EMPTY);
        }
        return list;
    }
}
