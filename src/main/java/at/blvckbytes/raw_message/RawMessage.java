package at.blvckbytes.raw_message;

import at.blvckbytes.raw_message.click.ClickAction;
import at.blvckbytes.raw_message.hover.HoverAction;
import at.blvckbytes.raw_message.json.JsonArray;
import at.blvckbytes.raw_message.json.JsonObject;
import at.blvckbytes.raw_message.json.JsonSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RawMessage {

  private static final JsonSerializer SERIALIZER_JSON = new JsonSerializer(false);

  private String text;
  private @Nullable String translate;
  private @Nullable List<RawMessage> translateWith;
  private @Nullable MessageColor color;
  private final Boolean[] styleStates;
  private @Nullable ClickAction clickAction;
  private @Nullable HoverAction hoverAction;
  private final List<RawMessage> extraMessages;
  private boolean clearedImplicitStyling;

  public RawMessage() {
    this(null);
  }

  public RawMessage(Object value) {
    this(String.valueOf(value));
  }

  public RawMessage(@Nullable String text) {
    this.styleStates = new Boolean[MessageStyle.VALUES.size()];
    this.extraMessages = new ArrayList<>();
    this.text = text == null ? "" : text;
  }

  private RawMessage(
    String text,
    @Nullable String translate,
    @Nullable List<RawMessage> translateWith,
    @Nullable MessageColor color,
    Boolean[] styleStates,
    @Nullable ClickAction clickAction,
    @Nullable HoverAction hoverAction,
    List<RawMessage> extraMessages,
    boolean clearedImplicitStyling
  ) {
    this.text = text;
    this.translate = translate;

    if (translateWith != null) {
      this.translateWith = new ArrayList<>();

      for (RawMessage item : translateWith)
        this.translateWith.add(item.duplicate());
    }

    this.color = color;

    this.styleStates = new Boolean[styleStates.length];
    System.arraycopy(styleStates, 0, this.styleStates, 0, styleStates.length);

    if (clickAction != null)
      this.clickAction = clickAction.duplicate();

    if (hoverAction != null)
      this.hoverAction = hoverAction.duplicate();

    this.extraMessages = new ArrayList<>();

    for (RawMessage item : extraMessages)
      this.extraMessages.add(item.duplicate());

    this.clearedImplicitStyling = clearedImplicitStyling;
  }

  public RawMessage duplicate() {
    return new RawMessage(
      this.text, this.translate, this.translateWith, this.color, this.styleStates, this.clickAction,
      this.hoverAction, this.extraMessages, this.clearedImplicitStyling
    );
  }

  public RawMessage setText(String text) {
    this.text = text;
    return this;
  }

  public RawMessage setTranslate(String key, @Nullable List<RawMessage> translateWith) {
    this.translate = key;
    this.translateWith = translateWith;
    return this;
  }

  public RawMessage setTranslate(String key) {
    return setTranslate(key, null);
  }

  public RawMessage setColor(@Nullable MessageColor color) {
    this.color = color;
    return this;
  }

  public RawMessage clearImplicitStyling() {
    // On item-names or lore-lines, italics is enabled implicitly
    // Also, lore-lines usually are purple by default
    this.color = MessageColor.WHITE;
    disableStyle(MessageStyle.ITALIC);
    this.clearedImplicitStyling = true;
    return this;
  }

  public RawMessage enableStyle(MessageStyle style) {
    styleStates[style.ordinal()] = true;
    return this;
  }

  public RawMessage disableStyle(MessageStyle style) {
    styleStates[style.ordinal()] = false;
    return this;
  }

  public RawMessage clearStyle(MessageStyle style) {
    styleStates[style.ordinal()] = null;
    return this;
  }

  public RawMessage setClickAction(@Nullable ClickAction action) {
    this.clickAction = action;
    return this;
  }

  public RawMessage setHoverAction(@Nullable HoverAction action) {
    this.hoverAction = action;
    return this;
  }

  public RawMessage addExtra(RawMessage extra) {
    this.extraMessages.add(extra);
    return this;
  }

  public RawMessage addExtra(String text) {
    this.extraMessages.add(new RawMessage(text));
    return this;
  }

  public String toJsonString(ServerVersion version) {
    return SERIALIZER_JSON.serialize(toJsonObject(version));
  }

  public String toJsonString() {
    return toJsonString(ServerVersion.CURRENT);
  }

  private void appendTranslationOrText(JsonObject object, ServerVersion version) {
    if (translate != null) {
      object.add("translate", translate);

      if (translateWith != null) {
        JsonArray translateWithArray = new JsonArray();

        for (RawMessage value : translateWith)
          translateWithArray.add(value.toJsonObject(version));

        object.add("with", translateWithArray);
      }
    }

    else
      object.add("text", text);
  }

  private void appendStyleAndColor(JsonObject object) {
    if (color != null)
      object.add("color", color.value);

    for (MessageStyle messageStyle : MessageStyle.VALUES) {
      Boolean styleState = this.styleStates[messageStyle.ordinal()];

      if (styleState == null)
        continue;

      object.add(messageStyle.value, styleState);
    }
  }

  private void appendExtraMessages(JsonObject object, ServerVersion version) {
    if (!extraMessages.isEmpty()) {
      JsonArray extraItems = new JsonArray();

      for (RawMessage extraMessage : extraMessages)
        extraItems.add(extraMessage.toJsonObject(version));

      object.add("extra", extraItems);
    }
  }

  public JsonObject toJsonObject(ServerVersion version) {
    JsonObject result = new JsonObject();

    appendTranslationOrText(result, version);
    appendStyleAndColor(result);
    appendExtraMessages(result, version);

    if (clickAction != null)
      clickAction.appendSelf(result, version);

    if (hoverAction != null)
      hoverAction.appendSelf(result, version);

    return result;
  }

  public JsonObject toJsonObject() {
    return toJsonObject(ServerVersion.CURRENT);
  }

  public String toLegacyText() {
    if (text == null || text.isEmpty())
      return "";

    StringBuilder result = new StringBuilder();

    if (this.clearedImplicitStyling)
      result.append("§r");

    if (color != null)
      result.append('§').append(color.legacyCharacter);

    for (MessageStyle messageStyle : MessageStyle.VALUES) {
      Boolean styleState = this.styleStates[messageStyle.ordinal()];

      if (styleState == null || !styleState)
        continue;

      result.append('§').append(messageStyle.legacyCharacter);
    }

    result.append(text);

    for (RawMessage extraMessage : extraMessages)
      result.append(extraMessage.toLegacyText());

    return result.toString();
  }

  public void tellRawTo(Player player, ServerVersion version) {
    Bukkit.dispatchCommand(
      Bukkit.getConsoleSender(),
      "tellraw " + player.getName() + " " + toJsonString(version)
    );
  }

  public void tellRawTo(Player player) {
    tellRawTo(player, ServerVersion.CURRENT);
  }
}