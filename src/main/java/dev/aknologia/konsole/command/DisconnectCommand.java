package dev.aknologia.konsole.command;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.CommandDispatcher;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.StringArgumentType;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

public class DisconnectCommand implements Command {
    public String name = "disconnect";
    public String description = "Disconnect from your current Server.";
    public List<Argument> arguments = new ArrayList<>();

    @Override
    public void register(CommandDispatcher dispatcher) {
        dispatcher.register(this);
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        boolean bl = KonsoleClient.CLIENT.isInSingleplayer();
        boolean bl2 = KonsoleClient.CLIENT.isConnectedToRealms();
        KonsoleClient.CLIENT.world.disconnect();
        if (bl) {
            KonsoleClient.CLIENT.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel")));
        } else {
            KonsoleClient.CLIENT.disconnect();
        }
        TitleScreen titleScreen = new TitleScreen();
        if (bl) {
            KonsoleClient.CLIENT.setScreen(titleScreen);
        } else if (bl2) {
            KonsoleClient.CLIENT.setScreen(new RealmsMainScreen(titleScreen));
        } else {
            KonsoleClient.CLIENT.setScreen(new MultiplayerScreen(titleScreen));
        }
        return 1;
    }

    @Override
    public List<Argument> getArguments() {
        return this.arguments;
    }

    @Override
    public void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
