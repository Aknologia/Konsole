package dev.aknologia.konsole.niflheim;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.ArgumentType;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.context.CommandContextBuilder;
import dev.aknologia.konsole.niflheim.context.StringRange;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import dev.aknologia.konsole.niflheim.suggestion.Suggestion;
import dev.aknologia.konsole.niflheim.suggestion.SuggestionContext;
import dev.aknologia.konsole.niflheim.suggestion.Suggestions;
import net.minecraft.text.LiteralText;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandDispatcher {
    public static final String ARGUMENT_SEPARATOR = " ";

    public static final char ARGUMENT_SEPARATOR_CHAR = ' ';

    private static final String USAGE_OPTIONAL_OPEN = "[";
    private static final String USAGE_OPTIONAL_CLOSE = "]";
    private static final String USAGE_REQUIRED_OPEN = "(";
    private static final String USAGE_REQUIRED_CLOSE = ")";
    private static final String USAGE_OR = "|";

    private HashMap<String, Command> commands = new HashMap<>();

    private ResultConsumer consumer = (c, s, r) -> {
    };

    public void register(Command command) {
        if(this.commands.keySet().contains(command.getName())) throw new IllegalArgumentException("Command '" + command.getName() + "' already defined.");
        this.commands.put(command.getName(), command);
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
        System.out.println(command);
        final CommandContext context = parse.getContext().build(command);
        if(context.getCommand() != null) {
            try {
                final int value = context.getCommand().run(context);
                result += value;
                consumer.onCommandComplete(context, true, value);
            } catch(final CommandSyntaxException ex) {
                consumer.onCommandComplete(context, false, 0);
                throw ex;
            } catch(IllegalArgumentException ex) {
                KonsoleClient.COMMAND_MANAGER.sendError(new LiteralText("Missing required argument"));
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
        Map<Integer, CommandSyntaxException> errors = null;

        final String commandName = originalReader.readUnquotedString();

        Command command = this.commands.get(commandName);
        if(command == null) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(originalReader);

        contextSoFar.withCommand(command);
        ListIterator<Argument> listIterator = command.getArguments().listIterator();

        while (listIterator.hasNext()) {
            Argument arg = listIterator.next();
            try {
                int start = originalReader.getCursor();
                Object resultArg = arg.getArgumentType().parse(originalReader);
                int end = originalReader.getCursor();
                if(resultArg instanceof String && resultArg.toString().trim().length() == 0) {
                    if(errors == null) errors = new LinkedHashMap<>();
                    CommandSyntaxException ex = CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedStartOfQuote().createWithContext(originalReader);
                    errors.put(originalReader.getCursor(), ex);
                    throw ex;
                }
                ParsedArgument<?> parsedArg = new ParsedArgument<>(start, end, resultArg);
                contextSoFar.withArgument(arg.getName(), parsedArg);
            } catch(final CommandSyntaxException ex) {
                if(errors == null) errors = new LinkedHashMap<>();
                errors.put(originalReader.getCursor(), ex);
                continue;
            }
        }

        return new ParseResults(contextSoFar, originalReader, errors == null ? Collections.emptyMap() : errors);
    }

    public String getUsage(final Command command) {
        String usage = command.getName();
        ListIterator<Argument> iterator = command.getArguments().listIterator();
        while (iterator.hasNext()) {
            Argument arg = iterator.next();
            usage += ARGUMENT_SEPARATOR;
            if(arg.isRequired()) usage += USAGE_REQUIRED_OPEN;
            else usage += USAGE_OPTIONAL_OPEN;
            usage += arg.getName();
            if(arg.isRequired()) usage += USAGE_REQUIRED_OPEN;
            else usage += USAGE_OPTIONAL_OPEN;
        }
        return usage;
    }

    public Suggestions getCompletionSuggestions(final ParseResults parse) {
        return getCompletionSuggestions(parse, parse.getReader().getTotalLength());
    }

    public Suggestions getCompletionSuggestions(final @NotNull ParseResults parse, int cursor) {
        final CommandContextBuilder context = parse.getContext();

        final String fullInput = parse.getReader().getString();
        final String truncatedInput = fullInput.substring(0, cursor);
        final String truncatedInputLowerCase = truncatedInput.toLowerCase(Locale.ROOT);

        final SuggestionContext suggestionContext = context.findSuggestionContext(fullInput, cursor);
        if(suggestionContext == null) {
            StringRange range = new StringRange(0, cursor);
            List<Suggestion> suggestions = new ArrayList<>();
            Set<String> commands = KonsoleClient.COMMAND_MANAGER.getDispatcher().commands.keySet();
            List<Suggestion> list2 = new ArrayList<>();
            Iterator<String> iterator = commands.iterator();
            while(iterator.hasNext()) {
                String key = iterator.next();
                if(key.toLowerCase(Locale.ROOT).startsWith(truncatedInputLowerCase)) {
                    suggestions.add(new Suggestion(range, key));
                } else list2.add(new Suggestion(range, key));
            }
            suggestions.addAll(list2);
            return new Suggestions(range, suggestions);
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
}
