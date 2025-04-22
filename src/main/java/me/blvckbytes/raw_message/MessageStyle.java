package me.blvckbytes.raw_message;

import java.util.List;

public enum MessageStyle {
  BOLD("bold"),
  ITALIC("italic"),
  UNDERLINE("underlined"),
  STRIKETHROUGH("strikethrough"),
  MAGIC("obfuscated")
  ;

  public static final List<MessageStyle> VALUES = List.of(values());

  public final String value;

  MessageStyle(String value) {
    this.value = value;
  }
}
