package dev.aknologia.konsole.command.action;

import dev.aknologia.konsole.command.ActionCategory;
import dev.aknologia.konsole.interfaces.MinecraftClientMixinInterface;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;

import java.util.*;

public class PickLoopCommand implements Command {
    public String name = "*pick";
    public String description = "Pick items continuously.";
    public Class<?> category = ActionCategory.class;
    public List<Argument> arguments = new ArrayList<>();

    private boolean enabled = false;

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

    @Override
    public Class<Category> getCategory() { return (Class<Category>) this.category; }
}
