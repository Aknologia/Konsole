package dev.aknologia.konsole.util;

import net.minecraft.util.Formatting;

public enum Color {
  BLACK(Formatting.BLACK, "0"),
  RED(Formatting.RED, "c"),
  GREEN(Formatting.GREEN, "a"),
  DARK_GREEN(Formatting.DARK_GREEN, "2"),
  YELLOW(Formatting.YELLOW, "e"),
  BLUE(Formatting.BLUE, "9"),
  DARK_BLUE(Formatting.DARK_BLUE, "1"),
  AQUA(Formatting.AQUA, "b"),
  DARK_AQUA(Formatting.DARK_AQUA, "3"),
  GOLD(Formatting.GOLD, "6"),
  LIGHT_PURPLE(Formatting.LIGHT_PURPLE, "d"),
  DARK_PURPLE(Formatting.DARK_PURPLE, "5"),
  WHITE(Formatting.WHITE, "f"),
  GRAY(Formatting.GRAY, "7"),
  DARK_GRAY(Formatting.DARK_GRAY, "8");


  private final Formatting formatting;
  private final String code;

  Color(Formatting formatting, String code) {
    this.formatting = formatting;
    this.code = code;
  }

  public Formatting getFormatting() {
    return this.formatting;
  }

  public String getCode() {
    return this.code;
  }

  @Override
  public String toString() {
      return String.format("\u00A7%s", this.code);
  }
}
