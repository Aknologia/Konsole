package dev.aknologia.konsole;

import dev.aknologia.konsole.niflheim.CommandManager;
import dev.aknologia.konsole.console.Konsole;
import dev.aknologia.konsole.console.KonsoleScreen;
import dev.aknologia.konsole.input.KeyManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;

import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class KonsoleClient implements ClientModInitializer {
    public static final String MODID = "konsole";
    public static final ModMetadata META = FabricLoader.getInstance().getModContainer(MODID).get().getMetadata();

    public static final Logger LOG = LogManager.getFormatterLogger(META.getName());

    private static KonsoleClient instance;
    private static MinecraftClient client;

    private static CommandManager commandManager;
    private static Konsole konsole;
    private static KonsoleScreen konsoleScreen;

    public static final Map<Integer, String> BINDS = new HashMap<>();
    public static final Map<String, String> ALIASES = new HashMap<>();

    @Override
    public void onInitializeClient() {
        if(!FabricLoader.getInstance().isModLoaded(MODID)) return;
        LOG.info("Initializing %s v%s", META.getName(), META.getVersion());

        KonsoleClient.init();
        KonsoleClient.setInstance(this);

        LOG.info("Initialized %s v%s", META.getName(), META.getVersion());
    }

    private static void init() {
        client = MinecraftClient.getInstance();
        konsole = new Konsole(client);
        konsoleScreen = new KonsoleScreen("", client);
        KeyManager.init();
        commandManager = new CommandManager();
    }

    private static void setInstance(KonsoleClient instance) {
        KonsoleClient.instance = instance;
    }

    public static KonsoleClient getInstance() {
        return instance;
    }

    public static MinecraftClient getClient() {
        return client;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static Konsole getKonsole() {
        return konsole;
    }

    public static KonsoleScreen getKonsoleScreen() {
        return konsoleScreen;
    }
}
