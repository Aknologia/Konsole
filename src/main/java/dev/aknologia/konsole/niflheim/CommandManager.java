package dev.aknologia.konsole.niflheim;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.command.*;
import dev.aknologia.konsole.convar.GammaConVar;
import dev.aknologia.konsole.niflheim.CommandDispatcher;
import dev.aknologia.konsole.niflheim.ParseResults;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.SharedConstants;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class CommandManager {
    private final CommandDispatcher dispatcher = new CommandDispatcher();
    public CommandManager() {
        this.registerAll();

        this.dispatcher.setConsumer(((context, success, result) -> {
            KonsoleClient.LOG.info("[DISPATCHER] %s [%s > %s] => %s", context.getInput(), context.getCommand().getName(), result, success ? "SUCCESS" : "FAIL");
        }));
    }

    public void sendError(Text message) {
        KonsoleClient.KONSOLE.addMessage(new LiteralText("").formatted(Formatting.RED).append(message));
    }

    public int execute(String command) {
        try {
            int n = this.dispatcher.execute(command);
            return n;
        } catch(final CommandSyntaxException ex) {
            int i;
            this.sendError(Texts.toText(ex.getRawMessage()));
            if(ex.getInput() != null && ex.getCursor() >= 0) {
                i = Math.min(ex.getInput().length(), ex.getCursor());
                MutableText mutableText = new LiteralText("").formatted(Formatting.GRAY);
                if(i > 10) {
                    mutableText.append("...");
                }
                mutableText.append(ex.getInput().substring(Math.max(0, i -10), i));
                if( i < ex.getInput().length()) {
                    MutableText text = new LiteralText(ex.getInput().substring(i)).formatted(Formatting.RED, Formatting.UNDERLINE);
                    mutableText.append(text);
                }
                mutableText.append(new LiteralText("<--[HERE]").formatted(Formatting.RED, Formatting.ITALIC));
                this.sendError(mutableText);
            }
            i = 0;
            return i;
        } catch(final Exception ex) {
            LiteralText i = new LiteralText(ex.getMessage() == null ? ex.getClass().getName() : ex.getMessage());
            if(KonsoleClient.LOG.isDebugEnabled()) {
                KonsoleClient.LOG.error("[MANAGER] Error while executing '%s': %s", command, ex);
                StackTraceElement[] mutableText = ex.getStackTrace();
                for (int text = 0; text < Math.min(mutableText.length, 3); ++text) {
                    i.append("\n\n").append(mutableText[text].getMethodName()).append("\n ").append(mutableText[text].getFileName()).append(":").append(String.valueOf(mutableText[text].getLineNumber()));
                }
            }
            this.sendError(new TranslatableText("command.failed").styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, i))));
            if(SharedConstants.isDevelopment) {
                this.sendError(new LiteralText(Util.getInnermostMessage(ex)));
                KonsoleClient.LOG.error("'%s' throw an exception: %s", command, ex);
            }
            int n = 0;
            return n;
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
                System.out.println(error);
                KonsoleClient.KONSOLE.addMessage(new LiteralText(convar.toString()));
            }

            return 1;
        };
        List<Argument> arguments = new ArrayList<>();
        arguments.add(new Argument("value", convar.getType().getArgument(convar.getArgumentParams())));

        return new DynamicCommand(convar.getName(), convar.getDescription(), arguments, callback);
    }

    private void registerAll() {
        /* REGISTER COMMANDS */
        new HelpCommand().register(this.dispatcher);
        new SettingsCommand().register(this.dispatcher);

        new EchoCommand().register(this.dispatcher);
        new ClearCommand().register(this.dispatcher);

        new DisconnectCommand().register(this.dispatcher);
        new QuitCommand().register(this.dispatcher);

        new BindCommand().register(this.dispatcher);
        new UnbindCommand().register(this.dispatcher);
        new UnbindAllCommand().register(this.dispatcher);
        new ListBindCommand().register(this.dispatcher);

        new SayCommand().register(this.dispatcher);

        new WorldCommand().register(this.dispatcher);
        new DimensionCommand().register(this.dispatcher);
        new PosCommand().register(this.dispatcher);

        new HideHUDCommand().register(this.dispatcher);
        new RefreshCommand().register(this.dispatcher);
        new HitboxCommand().register(this.dispatcher);
        new ChunkBorderCommand().register(this.dispatcher);
        new AdvancedTooltipsCommand().register(this.dispatcher);

        new NearCommand().register(this.dispatcher);
        new PlayersCommand().register(this.dispatcher);

        new ActionCommands().register(this.dispatcher);

        /* REGISTER CONVARS */
        new GammaConVar().register(this.dispatcher);
    }
}
