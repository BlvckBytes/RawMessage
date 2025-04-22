package at.blvckbytes.raw_message;

import java.awt.*;

public class MessageColor {

  public static final MessageColor BLACK        = new MessageColor("black",          0,   0,   0, '0');
  public static final MessageColor DARK_BLUE    = new MessageColor("dark_blue",      0,   0, 170, '1');
  public static final MessageColor DARK_GREEN   = new MessageColor("dark_green",     0, 170,   0, '2');
  public static final MessageColor DARK_AQUA    = new MessageColor("dark_aqua",      0, 170, 170, '3');
  public static final MessageColor DARK_RED     = new MessageColor("dark_red",     170,   0,   0, '4');
  public static final MessageColor DARK_PURPLE  = new MessageColor("dark_purple",  170,   0, 170, '5');
  public static final MessageColor GOLD         = new MessageColor("gold",         255, 170,   0, '6');
  public static final MessageColor GRAY         = new MessageColor("gray",         170, 170, 170, '7');
  public static final MessageColor DARK_GRAY    = new MessageColor("dark_gray",     85,  85,  85, '8');
  public static final MessageColor BLUE         = new MessageColor("blue",          85,  85, 255, '9');
  public static final MessageColor GREEN        = new MessageColor("green",         85, 255,  85, 'a');
  public static final MessageColor AQUA         = new MessageColor("aqua",          85, 255, 255, 'b');
  public static final MessageColor RED          = new MessageColor("red",          255,  85,  85, 'c');
  public static final MessageColor LIGHT_PURPLE = new MessageColor("light_purple", 255,  85, 255, 'd');
  public static final MessageColor YELLOW       = new MessageColor("yellow",       255, 255,  85, 'e');
  public static final MessageColor WHITE        = new MessageColor("white",        255, 255, 255, 'f');

  private static final MessageColor[] LEGACY_COLORS = {
    BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD,
    GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE,
  };

  public final String value;
  public final char legacyCharacter;

  private final Color color;

  private MessageColor(String value, int r, int g, int b, char legacyCharacter) {
    this.value = value;
    this.color = new Color(r, g, b);
    this.legacyCharacter = legacyCharacter == 0 ? closest(color).legacyCharacter : legacyCharacter;
  }

  private static MessageColor closest(Color targetColor) {
    double leastDistance = 0;
    MessageColor closestColor = null;

    for (MessageColor legacyColor : LEGACY_COLORS) {
      double currentDistance = legacyColor.colorDistance(targetColor);

      if (closestColor == null || currentDistance < leastDistance) {
        closestColor = legacyColor;
        leastDistance = currentDistance;
      }
    }

    return closestColor;
  }

  private double colorDistance(Color other) {
    int rMean = (color.getRed() + other.getRed()) / 2;

    int deltaR = color.getRed() - other.getRed();
    int deltaG = color.getGreen() - other.getGreen();
    int deltaB = color.getBlue() - other.getBlue();

    return Math.sqrt(
      (((512 + rMean) * deltaR * deltaR) >> 8) +
      4 * deltaG * deltaG +
      (((767 - rMean) * deltaB * deltaB) >> 8)
    );
  }

  public static MessageColor ofHex(String hexValue) {
    if (hexValue.length() != 7)
      throw new IllegalStateException("Expected \"" + hexValue + "\" to be of length 7");

    if (hexValue.charAt(0) != '#')
      throw new IllegalStateException("Expected \"" + hexValue + "\" to begin with a #");

    try {
      int r = Integer.parseInt(hexValue.substring(1, 3), 16);
      int g = Integer.parseInt(hexValue.substring(3, 5), 16);
      int b = Integer.parseInt(hexValue.substring(5), 16);

      return new MessageColor(hexValue, r, g, b, (char) 0);
    } catch (NumberFormatException ignored) {
      throw new IllegalStateException("Expected \"" + hexValue + "\" to be of pattern #[0-9A-Fa-f]{6}");
    }
  }
}