package dev.aknologia.konsole.command.utility;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.input.KeyManager;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.text.LiteralText;

import java.util.*;

public class ListBindCommand extends AbstractCommand {
    public ListBindCommand() {
        super("listbind", "Shows all registered binds.", Category.UTILITY, List.of());
    }

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
}
