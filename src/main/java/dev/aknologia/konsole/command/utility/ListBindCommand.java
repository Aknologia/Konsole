package dev.aknologia.konsole.command.utility;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.command.UtilityCategory;
import dev.aknologia.konsole.input.KeyManager;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.text.LiteralText;

import java.util.*;

public class ListBindCommand implements Command {
    public String name = "listbind";
    public String description = "Shows all registered binds.";
    public Class<?> category = UtilityCategory.class;
    public List<Argument> arguments = new ArrayList<>();

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        List<String> lines = new ArrayList<>();
        Map<Integer, String> bindsList = KonsoleClient.BINDS;
        Iterator<Integer> iterator = bindsList.keySet().iterator();
        while(iterator.hasNext()) {
            Integer keyCode = iterator.next();
            String boundCommand = bindsList.get(keyCode);
            lines.add(String.format("\u00A73%s \u00A7r= \u00A77\"%s\"", KeyManager.KEYS.getKeyName(keyCode), boundCommand));
        }
        KonsoleClient.getKonsole().addMessage(new LiteralText(String.join("\n", lines)));
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
