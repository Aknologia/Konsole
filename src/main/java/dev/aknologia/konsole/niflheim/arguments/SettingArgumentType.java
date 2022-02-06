package dev.aknologia.konsole.niflheim.arguments;

import com.mojang.brigadier.LiteralMessage;
import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.StringReader;
import dev.aknologia.konsole.niflheim.arguments.types.Settings;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import dev.aknologia.konsole.niflheim.exceptions.DynamicCommandExceptionType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SettingArgumentType implements ArgumentType<Settings.Setting> {
    private static final Collection<String> EXAMPLES = Arrays.asList("doBindsBypassKeybinds", "showTimestamps");
    public static final DynamicCommandExceptionType SETTING_NOT_FOUND_EXCEPTION = new DynamicCommandExceptionType(expected -> new LiteralMessage("Invalid setting: '" + expected + "'"));

    private SettingArgumentType() {
    }

    public static SettingArgumentType settingArg() { return new SettingArgumentType(); }

    public static Settings.Setting getSetting(final CommandContext context, final String name) {
        return context.getArgument(name, Settings.Setting.class);
    }

    @Override
    public Settings.Setting parse(final StringReader reader) throws CommandSyntaxException {
        String settingName = reader.readString().trim();
        if(!KonsoleClient.SETTINGS.getList().containsKey(settingName)) throw SETTING_NOT_FOUND_EXCEPTION.create(settingName);
        return new Settings.Setting(settingName, KonsoleClient.SETTINGS.getSetting(settingName));
    }

    @Override
    public String toString() { return "setting()"; }

    @Override
    public Collection<String> getExamples() { return EXAMPLES; }

    @Override
    public List<String> getSuggestions() {
        return KonsoleClient.SETTINGS.getList().keySet().stream().toList();
    }
}
