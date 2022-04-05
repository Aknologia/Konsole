package dev.aknologia.konsole.command;

import dev.aknologia.konsole.command.action.*;
import dev.aknologia.konsole.niflheim.Category;

import java.util.Arrays;

public class ActionCategory extends Category {
    public ActionCategory() {
        super("Action",
                Arrays.asList(
                    new AttackCommand(),
                    new AttackLoopCommand(),
                    new JumpCommand(),
                    new JumpLoopCommand(),
                    new PickCommand(),
                    new PickLoopCommand(),
                    new SneakCommand(),
                    new UseCommand(),
                    new UseLoopCommand()
                )
        );
    }
}
