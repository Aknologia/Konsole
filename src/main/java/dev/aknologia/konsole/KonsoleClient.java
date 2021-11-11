package dev.aknologia.konsole;

import dev.aknologia.konsole.command.CommandManager;
import dev.aknologia.konsole.console.Konsole;
import dev.aknologia.konsole.input.KeyManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;

import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KonsoleClient implements ClientModInitializer {
    public static KonsoleClient INSTANCE;
    public static final String MODID = "konsole";
    public static final ModMetadata META = FabricLoader.getInstance().getModContainer(MODID).get().getMetadata();
    public static final Logger LOG = LogManager.getFormatterLogger(META.getName());
    public static MinecraftClient CLIENT;
    public static CommandManager COMMAND_MANAGER;
    public static Konsole KONSOLE;

    @Override
    public void onInitializeClient() {
        if(!FabricLoader.getInstance().isModLoaded(MODID)) return;
        LOG.info("Initializing %s v%s", META.getName(), META.getVersion());
        CLIENT = MinecraftClient.getInstance();
        KONSOLE = new Konsole(CLIENT);
        INSTANCE = this;
        KeyManager.init();
        COMMAND_MANAGER = new CommandManager();
    }
}
