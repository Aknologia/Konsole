package dev.aknologia.konsole.command;

import dev.aknologia.konsole.command.utility.*;
import dev.aknologia.konsole.niflheim.Category;

import java.util.Arrays;

public class UtilityCategory extends Category {
    public UtilityCategory() {
        super("Utility",
                Arrays.asList(
                        new BindCommand(),
                        new ClearCommand(),
                        new DisconnectCommand(),
                        new EchoCommand(),
                        new HelpCommand(),
                        new ListBindCommand(),
                        new QuitCommand(),
                        new RefreshCommand(),
                        new SayCommand(),
                        new UnbindAllCommand(),
                        new UnbindCommand()
                )
        );
    }
}
