package at.blvckbytes.raw_message;

public class MessageColor {

  public final String value;

  public static final MessageColor BLACK = new MessageColor("black");
  public static final MessageColor DARK_BLUE = new MessageColor("dark_blue");
  public static final MessageColor DARK_GREEN = new MessageColor("dark_green");
  public static final MessageColor DARK_AQUA = new MessageColor("dark_aqua");
  public static final MessageColor DARK_RED = new MessageColor("dark_red");
  public static final MessageColor DARK_PURPLE = new MessageColor("dark_purple");
  public static final MessageColor GOLD = new MessageColor("gold");
  public static final MessageColor GRAY = new MessageColor("gray");
  public static final MessageColor DARK_GRAY = new MessageColor("dark_gray");
  public static final MessageColor BLUE = new MessageColor("blue");
  public static final MessageColor GREEN = new MessageColor("green");
  public static final MessageColor AQUA = new MessageColor("aqua");
  public static final MessageColor RED = new MessageColor("red");
  public static final MessageColor LIGHT_PURPLE = new MessageColor("light_purple");
  public static final MessageColor YELLOW = new MessageColor("yellow");
  public static final MessageColor WHITE = new MessageColor("white");

  private MessageColor(String value) {
    this.value = value;
  }

  public static MessageColor ofHex(String hexValue) {
    if (hexValue.length() != 7)
      throw new IllegalStateException("Expected \"" + hexValue + "\" to be of length 7");

    if (hexValue.charAt(0) != '#')
      throw new IllegalStateException("Expected \"" + hexValue + "\" to begin with a #");

    for (int i = 1; i < 7; ++i) {
      if (!isHexadecimal(hexValue.charAt(i)))
        throw new IllegalStateException("Expected character number " + (i + 1) + " of \"" + hexValue + "\" to be hexadecimal");
    }

    return new MessageColor(hexValue);
  }

  private static boolean isHexadecimal(char c) {
    return (
      (c >= 'a' && c <= 'f') ||
      (c >= 'A' && c <= 'F') ||
      (c >= '0' && c <= '9')
    );
  }
}
