package dev.aknologia.konsole.niflheim.arguments;

import com.mojang.brigadier.LiteralMessage;
import dev.aknologia.konsole.niflheim.StringReader;
import dev.aknologia.konsole.niflheim.arguments.types.Choice;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import dev.aknologia.konsole.niflheim.exceptions.DynamicCommandExceptionType;

import java.util.*;

public class ChoiceArgumentType implements ArgumentType<Choice> {
    public static final DynamicCommandExceptionType CHOICE_NOT_FOUND_EXCEPTION = new DynamicCommandExceptionType(expected -> new LiteralMessage("Unknown choice: '" + expected + "'"));

    private final List<Choice> choices;

    private ChoiceArgumentType(List<Choice> choices) {
        this.choices = choices;
    }

    public static ChoiceArgumentType choice(List<Choice> choices) {
        return new ChoiceArgumentType(choices);
    }

    public static Object getChoice(final CommandContext context, final String name, Class type) throws CommandSyntaxException {
        return context.getArgument(name, type);
    }

    public List<Choice> getChoices() {
        return this.choices;
    }

    @Override
    public Choice parse(final StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final String result = reader.readString();
        Choice foundChoice = null;
        Iterator<Choice> choiceInterator = this.getChoices().iterator();
        while(choiceInterator.hasNext()) {
            Choice choice = choiceInterator.next();
            if(choice.getName().equals(result)){
                foundChoice = choice;
                break;
            }
        }

        if (foundChoice == null) {
            throw CHOICE_NOT_FOUND_EXCEPTION.create(result);
        }
        return foundChoice;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ChoiceArgumentType)) return false;

        final ChoiceArgumentType that = (ChoiceArgumentType) o;
        return choices == that.choices;
    }

    @Override
    public String toString() {
        return "choice(" + String.join(", ", this.getChoicesNames()) + ")";
    }

    @Override
    public Collection<String> getExamples() {
        return this.getChoicesNames();
    }

    @Override
    public List<String> getSuggestions() { return this.getChoicesNames(); }

    private List<String> getChoicesNames() {
        List<String> choicesNames = new ArrayList<>();
        this.choices.forEach(ch -> choicesNames.add(ch.getName()));
        return choicesNames;
    }
}
