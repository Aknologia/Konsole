package dev.aknologia.konsole.command.utility;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.KonsoleLogger;
import dev.aknologia.konsole.input.KeyManager;
import dev.aknologia.konsole.niflheim.AbstractCommand;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
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
        KonsoleLogger.getInstance().info(lines.size() > 0 ? String.join("\n", lines) : "\u00A7cNo binds registered.");
        return 1;
    }
}
