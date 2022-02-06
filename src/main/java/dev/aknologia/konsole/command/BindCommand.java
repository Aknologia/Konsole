package dev.aknologia.konsole.command;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.CommandDispatcher;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.KeyArgumentType;
import dev.aknologia.konsole.niflheim.arguments.StringArgumentType;
import dev.aknologia.konsole.niflheim.arguments.types.Keys;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class BindCommand implements Command {
    public String name = "bind";
    public String description = "Bind a key to a specific command.";
    public List<Argument> arguments = new ArrayList<>();

    @Override
    public void register(CommandDispatcher dispatcher) {
        arguments.add(new Argument("key", KeyArgumentType.keyArg(), true));
        arguments.add(new Argument("command", StringArgumentType.string(), true));
        dispatcher.register(this);
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        Keys.Key key = KeyArgumentType.getKey(context, "key");
        if(key.getKeyName() == null || key.getKeyCode() == GLFW.GLFW_KEY_UNKNOWN) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().create("key");
        String command = StringArgumentType.getString(context, "command");
        if(command == null || command.trim().length() < 1) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().create("command");
        KonsoleClient.BINDS.put(key.getKeyCode(), command.trim());
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
