package dev.aknologia.konsole.niflheim;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.KonsoleLogger;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.context.CommandContextBuilder;
import dev.aknologia.konsole.niflheim.context.StringRange;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import dev.aknologia.konsole.niflheim.suggestion.Suggestion;
import dev.aknologia.konsole.niflheim.suggestion.SuggestionContext;
import dev.aknologia.konsole.niflheim.suggestion.Suggestions;
import net.minecraft.text.TranslatableText;

import java.util.*;

public class CommandDispatcher {
    public static final String ARGUMENT_SEPARATOR = " ";

    public static final char ARGUMENT_SEPARATOR_CHAR = ' ';

    private static final String USAGE_OPTIONAL_OPEN = "[";
    private static final String USAGE_OPTIONAL_CLOSE = "]";
    private static final String USAGE_REQUIRED_OPEN = "<";
    private static final String USAGE_REQUIRED_CLOSE = ">";
    private static final String USAGE_OR = "|";

    private final List<Category> CATEGORIES = new ArrayList<>();
    private final HashMap<String, ConsoleVariable<?>> CONVARS = new HashMap<>();

    private ResultConsumer consumer = (c, s, r) -> {
    };

    public HashMap<String, Command> getCommands() {
        HashMap<String, Command> commands = new HashMap<>();
        this.CATEGORIES.forEach(category -> {
            category.getCommands().forEach(command -> commands.put(command.getName(), command));
        });
        return commands;
    }
    public Command getCommand(String name) {
        return this.getCommands().get(name);
    }
    public ConsoleVariable<?> getConVar(String name) { return this.CONVARS.get(name); }

    public void register(Category category) {
        this.CATEGORIES.add(category);
    }

    public void register(ConsoleVariable<?> convar) {
        if(this.CONVARS.containsKey(convar.getName())) throw new IllegalArgumentException("ConVar '" + convar.getName() + "' already defined.");
        this.CONVARS.put(convar.getName(), convar);
    }

    public void setConsumer(final ResultConsumer consumer) { this.consumer = consumer; }

    public int execute(final String input) throws CommandSyntaxException {
        return execute(new StringReader(input));
    }

    public int execute(final StringReader input) throws CommandSyntaxException {
        final ParseResults parse = parse(input);
        return execute(parse);
    }

    public int execute(final ParseResults parse) throws CommandSyntaxException {
        if(parse.getReader().canRead()) {
            System.out.println(parse.getExceptions().size() + " exceptions");
            if(parse.getExceptions().size() == 1) {
                throw parse.getExceptions().values().iterator().next();
            } else if(parse.getContext().getRange().isEmpty()) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
            } else {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parse.getReader());
            }
        }

        int result = 0;
        final String command = parse.getReader().getString();
        final CommandContext context = parse.getContext().build(command);
        if(context.getCommand() != null) {
            try {
                final int value = context.getCommand().run(context);
                result += value;
                consumer.onCommandComplete(context, true, value);
            } catch(final CommandSyntaxException ex) {
                consumer.onCommandComplete(context, false, 0);
                throw ex;
            } catch(final IllegalArgumentException ex) {
                KonsoleLogger.getInstance().error(new TranslatableText("konsole.error.missing_argument"));
            } catch(Exception ex) {
                KonsoleClient.LOG.warn("Got error while running command: %s", ex);
            }
        } else {
            consumer.onCommandComplete(context, false, 0);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
        }

        return result;
    }

    public ParseResults parse(final String command) throws CommandSyntaxException {
        return parse(new StringReader(command));
    }

    public ParseResults parse(final StringReader command) throws CommandSyntaxException {
        final CommandContextBuilder context = new CommandContextBuilder(this, command.getCursor());
        return parse(context, command);
    }

    public ParseResults parse(final CommandContextBuilder contextSoFar, final StringReader originalReader) throws CommandSyntaxException {
        Map<Integer, CommandSyntaxException> errors = new LinkedHashMap<>();

        final String commandName = originalReader.readUnquotedString();

        Command command = this.getCommand(commandName);
        ConsoleVariable<?> convar;
        if(command == null) {
            convar = this.CONVARS.get(commandName);
            if(convar != null) command = CommandManager.fromConVar(convar);
            else throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(originalReader);
        }

        contextSoFar.withCommand(command);

        for (Argument arg : command.getArguments()) {
            try {
                int start = originalReader.getCursor();
                Object resultArg = arg.getArgumentType().parse(originalReader);
                int end = originalReader.getCursor();
                if (resultArg instanceof String && resultArg.toString().trim().length() == 0 && !arg.isRequired()) {
                    CommandSyntaxException ex = CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedStartOfQuote().createWithContext(originalReader);
                    errors.put(originalReader.getCursor(), ex);
                    throw ex;
                }
                ParsedArgument<?> parsedArg = new ParsedArgument<>(start, end, resultArg);
                contextSoFar.withArgument(arg.getName(), parsedArg);
            } catch (final CommandSyntaxException ex) {
                errors.put(originalReader.getCursor(), ex);
            }
        }

        System.out.println("Errors: " + errors.size());

        return new ParseResults(contextSoFar, originalReader, errors.size() < 1 ? Collections.emptyMap() : errors);
    }

    public String getUsage(final Command command) {
        StringBuilder usage = new StringBuilder(command.getName());
        for (Argument arg : command.getArguments()) {
            usage.append(ARGUMENT_SEPARATOR);
            if (arg.isRequired()) usage.append(USAGE_REQUIRED_OPEN);
            else usage.append(USAGE_OPTIONAL_OPEN);
            usage.append(arg.getName());
            if (arg.isRequired()) usage.append(USAGE_REQUIRED_CLOSE);
            else usage.append(USAGE_OPTIONAL_CLOSE);
        }
        return usage.toString();
    }

    public Suggestions getCompletionSuggestions(final ParseResults parse, final String fullInput) {
        return getCompletionSuggestions(parse, fullInput, parse.getReader().getTotalLength());
    }

    public Suggestions getCompletionSuggestions(final ParseResults parse, final String fullInput, int cursor) {
        final String truncatedInput = fullInput.substring(0, cursor);
        final String truncatedInputLowerCase = truncatedInput.toLowerCase(Locale.ROOT);
        if(parse == null) return this.getCommandCompletionSuggestions(fullInput, cursor);

        final CommandContextBuilder context = parse.getContext();

        final SuggestionContext suggestionContext = context.findSuggestionContext(fullInput, cursor);
        if(suggestionContext == null) {
            return this.getCommandCompletionSuggestions(truncatedInputLowerCase, cursor);
        }
        final Command command = suggestionContext.command;
        final int start = Math.min(suggestionContext.startPos, cursor);

        if(suggestionContext.argument == null) {
            return new Suggestions(new StringRange(suggestionContext.startPos, cursor), Collections.emptyList());
        }
        List<Suggestion> suggestions = new ArrayList<>();
        List<Suggestion> list2 = new ArrayList<>();

        List<String> rawSuggestions = suggestionContext.argument.getArgumentType().getSuggestions();
        Iterator<String> iterator = rawSuggestions.iterator();
        StringRange range = new StringRange(suggestionContext.startPos, cursor);
        while(iterator.hasNext()) {
            String key = iterator.next();
            if(key.toLowerCase(Locale.ROOT).startsWith(truncatedInputLowerCase)) {
                suggestions.add(new Suggestion(range, key));
            } else list2.add(new Suggestion(range, key));
        }
        suggestions.addAll(list2);

        return new Suggestions(range, suggestions);
    }

    public Suggestions getCommandCompletionSuggestions(String truncatedInputLowerCase, int cursor) {
        StringRange range = new StringRange(0, cursor);
        List<Suggestion> suggestions = new ArrayList<>();
        List<String> commands = new ArrayList<>(KonsoleClient.getCommandManager().getDispatcher().getCommands().keySet());
        commands.addAll(new ArrayList<>(KonsoleClient.getCommandManager().getDispatcher().CONVARS.keySet()));
        List<Suggestion> list2 = new ArrayList<>();
        for (String key : commands) {
            if (key.toLowerCase(Locale.ROOT).startsWith(truncatedInputLowerCase)) {
                suggestions.add(new Suggestion(range, key));
            } else list2.add(new Suggestion(range, key));
        }
        suggestions.addAll(list2);
        return new Suggestions(range, suggestions);
    }
}
