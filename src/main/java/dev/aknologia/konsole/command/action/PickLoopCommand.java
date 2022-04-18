package dev.aknologia.konsole.command.action;

import dev.aknologia.konsole.interfaces.MinecraftClientMixinInterface;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;

import java.util.*;

public class PickLoopCommand extends AbstractCommand {
    private boolean enabled = false;

    public PickLoopCommand() {
        super("*pick", "Pick items continuously.", Category.ACTION, List.of());
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        this.enabled = !this.enabled;
        if(this.enabled) this.trigger();
        return 1;
    }

    private void trigger() {
        PickLoopCommand instance = this;
        Timer t = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(!instance.enabled) t.cancel();
                else ((MinecraftClientMixinInterface) MinecraftClient.getInstance()).doItemPickMixed();
            }
        };
        t.scheduleAtFixedRate(task, new Date(), 50);
    }
}
