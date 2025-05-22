package at.blvckbytes.raw_message;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public enum MessageStyle {
  MAGIC         ("obfuscated",    'k'),
  BOLD          ("bold",          'l'),
  STRIKETHROUGH ("strikethrough", 'm'),
  UNDERLINE     ("underlined",    'n'),
  ITALIC        ("italic",        'o'),
  ;

  public static final List<MessageStyle> VALUES = Collections.unmodifiableList(Arrays.asList(values()));

  private static final Map<String, MessageStyle> messageStyleByName = new HashMap<>();

  static {
    for (MessageStyle style : VALUES)
      messageStyleByName.put(style.value, style);
  }

  public final String value;
  public final char legacyCharacter;

  MessageStyle(String value, char legacyCharacter) {
    this.value = value;
    this.legacyCharacter = legacyCharacter;
  }

  public static @Nullable MessageStyle fromName(String name) {
    return messageStyleByName.get(name.toLowerCase());
  }
}
