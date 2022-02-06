package dev.aknologia.konsole.command;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.CommandDispatcher;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.BoolArgumentType;
import dev.aknologia.konsole.niflheim.arguments.SettingArgumentType;
import dev.aknologia.konsole.niflheim.arguments.StringArgumentType;
import dev.aknologia.konsole.niflheim.arguments.types.Settings;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.List;

public class SettingsCommand implements Command {
    public String name = "settings";
    public String description = "Show/Set Konsole's Settings.";
    public List<Argument> arguments = new ArrayList<>();

    @Override
    public void register(CommandDispatcher dispatcher) {
        arguments.add(new Argument("name", SettingArgumentType.settingArg(), true));
        arguments.add(new Argument("value", BoolArgumentType.bool()));
        dispatcher.register(this);
    }

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        Settings.Setting setting = SettingArgumentType.getSetting(context, "name");
        String value = null;
        try {
            value = StringArgumentType.getString(context, "value");
        } catch(Exception e) {}
        if(value == null) {
            List<String> lines = new ArrayList<>();
            lines.add(String.format("\u00A7f\u00A7lSETTINGS \u00A7r\u00A77> \u00A73%s", setting.getName()));
            String valSep = setting.getValue() ? "\u00A7a" : "\u00A7c";
            lines.add(String.format("\u00A7nValue:\u00A7r %s%s", valSep, setting.getValue()));
            KonsoleClient.KONSOLE.addMessage(new LiteralText(String.join("\n", lines)));
        } else {
            KonsoleClient.SETTINGS.setSetting(setting.getName(), setting.getValue());
        }
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
