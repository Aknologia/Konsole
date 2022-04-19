package dev.aknologia.konsole.util;

public enum LogLevel {
  DEBUG(Color.DARK_GRAY, Color.GRAY),
  INFO(Color.GREEN),
  WARNING(Color.YELLOW),
  ERROR(Color.RED, Color.RED);

  private final Color prefixColor;
  private final Color color;

  LogLevel(Color prefixColor) {
    this(prefixColor, Color.WHITE);
  }
  LogLevel(Color prefixColor, Color color) {
    this.prefixColor = prefixColor;
    this.color = color;
  }

  public Color getPrefixColor() {
    return prefixColor;
  }

  public Color getColor() {
    return color;
  }

  public String getPrefix() {
    return String.format("%s[%s]%s", this.prefixColor.toString(), name(), this.color.toString());
  }
}
