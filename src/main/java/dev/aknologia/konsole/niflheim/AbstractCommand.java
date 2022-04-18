package dev.aknologia.konsole.niflheim;

import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;

import java.util.List;

public abstract class AbstractCommand implements Command {
  protected String name;
  protected String description;
  protected Class<?> category;
  protected List<Argument> arguments;

  public AbstractCommand(String name, String description, Class<?> category, List<Argument> arguments) {
    this.name = name;
    this.description = description;
    this.category = category;
    this.arguments = arguments;
  }

  public int run(CommandContext context) throws CommandSyntaxException {
    return 0;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Class<Category> getCategory() {
    return (Class<Category>) category;
  }

  public List<Argument> getArguments() {
    return arguments;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setCategory(Class<?> category) {
    this.category = category;
  }

  public void setArguments(List<Argument> arguments) {
    this.arguments = arguments;
  }
}
