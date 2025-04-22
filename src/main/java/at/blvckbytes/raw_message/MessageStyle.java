package at.blvckbytes.raw_message;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum MessageStyle {
  MAGIC         ("obfuscated",    'k'),
  BOLD          ("bold",          'l'),
  STRIKETHROUGH ("strikethrough", 'm'),
  UNDERLINE     ("underlined",    'n'),
  ITALIC        ("italic",        'o'),
  ;

  public static final List<MessageStyle> VALUES = Collections.unmodifiableList(Arrays.asList(values()));

  public final String value;
  public final char legacyCharacter;

  MessageStyle(String value, char legacyCharacter) {
    this.value = value;
    this.legacyCharacter = legacyCharacter;
  }
}
