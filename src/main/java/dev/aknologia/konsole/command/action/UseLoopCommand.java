package dev.aknologia.konsole.command.action;

import dev.aknologia.konsole.command.ActionCategory;
import dev.aknologia.konsole.interfaces.MinecraftClientMixinInterface;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;

import java.util.*;

public class UseLoopCommand extends AbstractCommand {
    private boolean enabled = false;

    public UseLoopCommand() {
        super("*use", "Use the selected item or block continuously.", Category.ACTION, List.of());
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        this.enabled = !this.enabled;
        if(this.enabled) this.trigger();
        return 1;
    }

    private void trigger() {
        UseLoopCommand instance = this;
        Timer t = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(!instance.enabled) t.cancel();
                else ((MinecraftClientMixinInterface) MinecraftClient.getInstance()).doItemUseMixed();
            }
        };
        t.scheduleAtFixedRate(task, new Date(), 50);
    }
}
