package dev.aknologia.konsole.command;

import dev.aknologia.konsole.command.info.*;
import dev.aknologia.konsole.niflheim.Category;

import java.util.Arrays;

public class InfoCategory extends Category {
    public InfoCategory() {
        super("Info",
                Arrays.asList(
                        new DimensionCommand(),
                        new NearCommand(),
                        new PingCommand(),
                        new PlayersCommand(),
                        new PosCommand(),
                        new TPSCommand(),
                        new WhoIsCommand(),
                        new WorldCommand()
                )
        );
    }
}
