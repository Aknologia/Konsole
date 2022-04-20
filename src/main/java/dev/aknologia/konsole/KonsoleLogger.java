package dev.aknologia.konsole;

import dev.aknologia.konsole.util.LogLevel;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class KonsoleLogger {
    private static KonsoleLogger INSTANCE;

    private KonsoleLogger() {
    }

    public static KonsoleLogger getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new KonsoleLogger();
        }
        return INSTANCE;
    }

    public void log(String message) {
        KonsoleClient.getKonsole().addMessage(new LiteralText(String.format("%s", message)));
    }

    public void log(LogLevel level, String message) {
        KonsoleClient.getKonsole().addMessage(new LiteralText(String.format("%s %s", level.getPrefix(), message)));
    }

    public void log(LogLevel level, String message, Object... args) {
        KonsoleClient.getKonsole().addMessage(new LiteralText(String.format("%s %s", level.getPrefix(), String.format(message, args))));
    }

    public void log(LogLevel level, String message, boolean isLogged) {
        KonsoleClient.getKonsole().addMessage(new LiteralText(String.format("%s %s", level.getPrefix(), message)), isLogged);
    }

    public void log(Text message) {
        KonsoleClient.getKonsole().addMessage(message);
    }

    public void log(LogLevel level, Text message) {
        KonsoleClient.getKonsole().addMessage(new LiteralText(level.getPrefix() + " ").append(message));
    }

    public void log(LogLevel level, Text message, boolean isLogged) {
        KonsoleClient.getKonsole().addMessage(new LiteralText(level.getPrefix() + " ").append(message), isLogged);
    }

    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void info(Text message) {
        log(LogLevel.INFO, message);
    }

    public void info(String message, Object... args) {
        log(LogLevel.INFO, message, args);
    }

    public void warning(String message) {
        log(LogLevel.WARNING, message);
    }

    public void warning(Text message) {
        log(LogLevel.WARNING, message);
    }

    public void warning(String message, Object... args) {
        log(LogLevel.WARNING, message, args);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public void error(Text message) {
        log(LogLevel.ERROR, message);
    }

    public void error(String message, Object... args) {
        log(LogLevel.ERROR, message, args);
    }

    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    public void debug(Text message) {
        log(LogLevel.DEBUG, message);
    }

    public void debug(String message, Object... args) {
        log(LogLevel.DEBUG, message, args);
    }

    public void chat(String message) {
        log(LogLevel.CHAT, message, false);
    }

    public void chat(Text message) {
        log(LogLevel.CHAT, message, false);
    }

    public void chat(MessageType type, String message) {
        chat(type, new LiteralText(message));
    }

    public void chat(MessageType type, Text message) {
        String typePrefix = type.name(); typePrefix = typePrefix.substring(0, 1).toUpperCase() + typePrefix.substring(1).toLowerCase();
        log(LogLevel.CHAT, new LiteralText("\u00a7a(\u00a73"+typePrefix+"\u00a7a)\u00a7r ").append(message), false);
    }
}
