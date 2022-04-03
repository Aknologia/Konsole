package dev.aknologia.konsole.command.action;

import dev.aknologia.konsole.command.ActionCategory;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class JumpCommand implements Command {
    public String name = "+jump";
    public String description = "Jump once.";
    public Class<?> category = ActionCategory.class;
    public List<Argument> arguments = new ArrayList<>();

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        if(MinecraftClient.getInstance().player.isOnGround()) MinecraftClient.getInstance().player.jump();
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

    @Override
    public Class<Category> getCategory() { return (Class<Category>) this.category; }
}
