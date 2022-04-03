package dev.aknologia.konsole.command.action;

import dev.aknologia.konsole.command.ActionCategory;
import dev.aknologia.konsole.interfaces.MinecraftClientMixinInterface;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class AttackCommand implements Command {
    public String name = "+attack";
    public String description = "Attack with the main hand.";
    public Class<?> category = ActionCategory.class;
    public List<Argument> arguments = new ArrayList<>();

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        ((MinecraftClientMixinInterface) MinecraftClient.getInstance()).doAttackMixed();
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
