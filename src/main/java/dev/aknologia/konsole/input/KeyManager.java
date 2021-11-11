package dev.aknologia.konsole.input;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.console.KonsoleScreen;
import dev.aknologia.konsole.niflheim.arguments.types.Keys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class KeyManager {
    public static Keys KEYS = new Keys();
    public static KeyBinding OPEN_CONSOLE = new KeyBinding("key.konsole.open-console", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, KonsoleClient.META.getName());

    public static void init() {
        register();
    }

    private static void register() {
        KeyBindingHelper.registerKeyBinding(OPEN_CONSOLE);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(OPEN_CONSOLE.wasPressed() && MinecraftClient.getInstance().currentScreen == null)
                MinecraftClient.getInstance().setScreen(new KonsoleScreen(""));
        });
    }
}
