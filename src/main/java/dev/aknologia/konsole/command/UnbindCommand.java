package dev.aknologia.konsole.command;

import com.mojang.brigadier.LiteralMessage;
import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.CommandDispatcher;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.KeyArgumentType;
import dev.aknologia.konsole.niflheim.arguments.StringArgumentType;
import dev.aknologia.konsole.niflheim.arguments.types.Keys;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import dev.aknologia.konsole.niflheim.exceptions.DynamicCommandExceptionType;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class UnbindCommand implements Command {
    public String name = "unbind";
    public String description = "Unbind a specific key.";
    public List<Argument> arguments = new ArrayList<>();

    public static final DynamicCommandExceptionType KEY_NOT_BOUND_EXCEPTION = new DynamicCommandExceptionType(expected -> new LiteralMessage("Key not bound: '" + expected + "'"));

    @Override
    public void register(CommandDispatcher dispatcher) {
        arguments.add(new Argument("key", KeyArgumentType.keyArg(), true));
        dispatcher.register(this);
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        Keys.Key key = KeyArgumentType.getKey(context, "key");
        if(key.getKeyName() == null || key.getKeyCode() == GLFW.GLFW_KEY_UNKNOWN) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().create("key");
        if(!KonsoleClient.BINDS.containsKey(key.getKeyCode())) throw KEY_NOT_BOUND_EXCEPTION.create(key.getKeyName());
        KonsoleClient.BINDS.remove(key.getKeyCode());
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
