package dev.aknologia.konsole.niflheim;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.KonsoleLogger;
import dev.aknologia.konsole.command.*;
import dev.aknologia.konsole.convar.*;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import dev.aknologia.konsole.util.KonsoleUtils;
import net.minecraft.SharedConstants;
import net.minecraft.text.*;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class CommandManager {
    private final CommandDispatcher dispatcher = new CommandDispatcher();
    public CommandManager() {
        this.registerAll();

        this.dispatcher.setConsumer(((context, success, result) -> {
            KonsoleClient.LOG.info("[DISPATCHER] %s [%s > %s] => %s", context.getInput(), context.getCommand().getName(), result, success ? "SUCCESS" : "FAIL");
        }));
    }

    public void execute(String command) {
        try {
            int n = this.dispatcher.execute(command);
        } catch(final CommandSyntaxException ex) {
            int i;
            KonsoleLogger.getInstance().error(Texts.toText(ex.getRawMessage()));
            if(ex.getInput() != null && ex.getCursor() >= 0) {
                i = Math.min(ex.getInput().length(), ex.getCursor());
                StringBuilder stringbuilder = new StringBuilder("\u00a77");
                if(i > 10) {
                    stringbuilder.append("...");
                }
                stringbuilder.append(ex.getInput(), Math.max(0, i - 10), i);
                if( i < ex.getInput().length()) {
                    stringbuilder.append(" \u00a7c\u00a7n").append(ex.getInput().substring(i+1));
                }
                stringbuilder.append("\u00a7r\u00a7c\u00a7o<--[").append(KonsoleUtils.getTranslated("konsole.misc.here").toUpperCase(Locale.ROOT)).append("]");
                KonsoleLogger.getInstance().error(stringbuilder.toString());
            }
            i = 0;
        } catch(final Exception ex) {
            LiteralText i = new LiteralText(ex.getMessage() == null ? ex.getClass().getName() : ex.getMessage());
            if(KonsoleClient.LOG.isDebugEnabled()) {
                KonsoleClient.LOG.error("[MANAGER] Error while executing '%s': %s", command, ex.toString());
                StackTraceElement[] mutableText = ex.getStackTrace();
                for (int text = 0; text < Math.min(mutableText.length, 3); ++text) {
                    i.append("\n\n").append(mutableText[text].getMethodName()).append("\n ").append(mutableText[text].getFileName()).append(":").append(String.valueOf(mutableText[text].getLineNumber()));
                }
            }
            KonsoleLogger.getInstance().error(new TranslatableText("command.failed").styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, i))));
            if(SharedConstants.isDevelopment) {
                KonsoleLogger.getInstance().error(Util.getInnermostMessage(ex));
                KonsoleClient.LOG.error("[MANAGER] '%s' throw an exception: %s", command, ex.toString());
            }
            int n = 0;
        }
    }

    public CommandDispatcher getDispatcher() { return this.dispatcher; }

    @Nullable
    public static <S> CommandSyntaxException getException(ParseResults parse) {
        if (!parse.getReader().canRead()) {
            return null;
        }
        if (parse.getExceptions().size() == 1) {
            return parse.getExceptions().values().iterator().next();
        }
        if (parse.getContext().getRange().isEmpty()) {
            return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
        }
        return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parse.getReader());
    }

    public static Command fromConVar(ConsoleVariable<?> convar) {
        Function callback = convar.hasCallback() ? convar.getCallback() : (Object contextObj) -> {
            CommandContext context = (CommandContext) contextObj;
            try {
                convar.set(convar.getArgumentValue(context));
            } catch(IllegalArgumentException error) {
                KonsoleLogger.getInstance().info(convar.toString());
            }

            return 1;
        };
        List<Argument> arguments = new ArrayList<>();
        arguments.add(new Argument("value", convar.getType().getArgument(convar.getArgumentParams())));

        return new DynamicCommand(convar.getName(), convar.getDescription(), UtilityCategory.class, arguments, callback);
    }

    private void registerAll() {
        /* REGISTER CATEGORIES */
        List<Category> categories = Arrays.asList(
                new ActionCategory(),
                new InfoCategory(),
                new UtilityCategory()
        );

        categories.forEach(this.dispatcher::register);

        /* REGISTER CONVARS */
        new GammaConVar().register(this.dispatcher);
        new AdvancedTooltipsConVar().register(this.dispatcher);
        new ChunkBorderConVar().register(this.dispatcher);
        new HitboxConVar().register(this.dispatcher);
        new HUDHiddenConVar().register(this.dispatcher);

        new ChatLogLevelConVar().register(this.dispatcher);
        new ScaleConVar().register(this.dispatcher);
    }
}
