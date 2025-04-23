package at.blvckbytes.raw_message.hover;

import at.blvckbytes.raw_message.ServerVersion;
import at.blvckbytes.raw_message.RawMessage;
import at.blvckbytes.raw_message.json.JsonArray;
import at.blvckbytes.raw_message.json.JsonObject;
import at.blvckbytes.raw_message.json.JsonSerializer;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ShowItemAction extends HoverAction {

  // TODO: Since 1.11.2-, `value` is SNBT, meaning that keys do not allow quotes
  //       {"text":"A","hoverEvent":{"action":"show_item","value":"{id:\"stone\",Count:1,tag:{display:{Name:\"hi\"}}}"}}

  private static final JsonSerializer SERIALIZER_JSON = new JsonSerializer(false);

  private final Material material;
  private @Nullable RawMessage name;
  private List<RawMessage> lore;

  public ShowItemAction() {
    this(Material.STONE);
  }

  public ShowItemAction(Material material) {
    this.material = material;
    this.lore = new ArrayList<>();
  }

  public ShowItemAction setName(@Nullable RawMessage name) {
    this.name = name;
    return this;
  }

  public ShowItemAction setName(String name) {
    this.name = new RawMessage(name).clearImplicitStyling();
    return this;
  }

  public ShowItemAction setLore(List<RawMessage> lore) {
    this.lore = lore;
    return this;
  }

  public ShowItemAction setLoreStrings(List<String> lore) {
    this.lore.clear();

    for (String line : lore)
      this.lore.add(new RawMessage(line).clearImplicitStyling());

    return this;
  }

  public ShowItemAction addLoreLine(RawMessage line) {
    this.lore.add(line);
    return this;
  }

  public ShowItemAction addLoreLine(String line) {
    this.lore.add(new RawMessage(line).clearImplicitStyling());
    return this;
  }

  @Override
  public void appendSelf(JsonObject component, ServerVersion version) {
    JsonObject containerObject = makeAndAppendContainer(component, version);

    containerObject.add("action", "show_item");

    // Properties have been inlined
    if (version.compareTo(ServerVersion.V1_21_5) >= 0) {
      containerObject.add("id", decideIdValue(version));
      containerObject.add("count", 1);
      appendMetaData(containerObject, version);
      return;
    }

    JsonObject dataObject = new JsonObject();

    dataObject.add("id", decideIdValue(version));

    appendMetaData(dataObject, version);

    // Key "value" has been deprecated; contents now also is an object
    if (version.compareTo(ServerVersion.V1_16_0) >= 0) {
      dataObject.add("count", 1);
      containerObject.add("contents", dataObject);
      return;
    }

    // It's crucial to have this property capitalized - otherwise, the item's invalid
    dataObject.add("Count", 1);

    containerObject.add("value", SERIALIZER_JSON.serialize(dataObject));
  }

  private void appendMetaData(JsonObject object, ServerVersion version) {
    if (name == null && lore.isEmpty())
      return;

    JsonObject displayObject = new JsonObject();

    // Components have been introduced
    if (version.compareTo(ServerVersion.V1_20_5) >= 0) {
      if (name != null) {
        // Values are now elements instead of strings
        if (version.compareTo(ServerVersion.V1_21_5) >= 0)
          displayObject.add("minecraft:custom_name", name.toJsonObject(version));
        else
          displayObject.add("minecraft:custom_name", name.toJsonString(version));
      }

      if (!lore.isEmpty()) {
        JsonArray loreArray = new JsonArray();
        displayObject.add("minecraft:lore", loreArray);

        for (RawMessage loreLine : lore) {
          if (version.compareTo(ServerVersion.V1_21_5) >= 0)
            loreArray.add(loreLine.toJsonObject(version));
          else
            loreArray.add(loreLine.toJsonString(version));
        }
      }

      object.add("components", displayObject);
      return;
    }

    // Minecraft 1.14+ supports components in Name and Lore
    // Minecraft 1.13 supports components in Name, but not Lore
    // Minecraft 1.12- supports no components, neither in Name nor Lore

    if (name != null) {
      if (version.compareTo(ServerVersion.V1_13_0) >= 0) {
        displayObject.add("Name", name.toJsonString(version));
      } else {
        displayObject.add("Name", name.toLegacyText());
      }
    }

    if (!lore.isEmpty()) {
      JsonArray loreArray = new JsonArray();
      displayObject.add("Lore", loreArray);

      for (RawMessage loreLine : lore) {
        if (version.compareTo(ServerVersion.V1_14_0) >= 0)
          loreArray.add(loreLine.toJsonString(version));
        else
          loreArray.add(loreLine.toLegacyText());
      }
    }

    JsonObject tagObject = new JsonObject();
    tagObject.add("display", displayObject);

    // Why would they stringify the tag-key when its parent just has been
    // converted to a structured object? How weird.
    if (version.compareTo(ServerVersion.V1_16_0) >= 0)
      object.add("tag", SERIALIZER_JSON.serialize(tagObject));
    else
      object.add("tag", tagObject);
  }

  private String decideIdValue(ServerVersion version) {
    if (version.compareTo(ServerVersion.V1_13_0) >= 0)
      return material.getKey().toString();

    return material.name().toLowerCase();
  }
}
