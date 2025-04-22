package me.blvckbytes.raw_message.hover;

import com.google.gson.*;
import me.blvckbytes.raw_message.ServerVersion;
import me.blvckbytes.raw_message.RawMessage;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ShowItemAction extends HoverAction {

  private static final Gson GSON_INSTANCE = new Gson();

  private final Material material;
  private @Nullable RawMessage name;
  private List<RawMessage> lore;

  public ShowItemAction(Material material) {
    this.material = material;
    this.lore = new ArrayList<>();
  }

  public ShowItemAction setName(@Nullable RawMessage name) {
    this.name = name;
    return this;
  }

  public ShowItemAction setLore(List<RawMessage> lore) {
    this.lore = lore;
    return this;
  }

  public ShowItemAction addLoreLine(RawMessage line) {
    this.lore.add(line);
    return this;
  }

  @Override
  public void appendSelf(JsonObject component, ServerVersion version) {
    JsonObject containerObject = makeAndAppendContainer(component, version);

    containerObject.addProperty("action", "show_item");

    // Properties have been inlined
    if (version.compareTo(ServerVersion.V1_21_5) >= 0) {
      containerObject.addProperty("id", decideIdValue(version));
      containerObject.addProperty("count", 1);
      appendMetaData(containerObject, version);
      return;
    }

    JsonObject dataObject = new JsonObject();

    dataObject.addProperty("id", decideIdValue(version));
    dataObject.addProperty("count", 1);

    appendMetaData(dataObject, version);

    // Key "value" has been deprecated; contents now also is an object
    if (version.compareTo(ServerVersion.V1_16_0) >= 0) {
      containerObject.add("contents", dataObject);
      return;
    }

    containerObject.addProperty("value", GSON_INSTANCE.toJson(dataObject));
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
          displayObject.addProperty("minecraft:custom_name", name.toJsonString(version));
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

    if (name != null)
      displayObject.addProperty("Name", name.toJsonString(version));

    if (!lore.isEmpty()) {
      JsonArray loreArray = new JsonArray();
      displayObject.add("Lore", loreArray);

      for (RawMessage loreLine : lore)
        loreArray.add(loreLine.toJsonString(version));
    }

    JsonObject tagObject = new JsonObject();
    tagObject.add("display", displayObject);

    object.addProperty("tag", GSON_INSTANCE.toJson(tagObject));
  }

  private String decideIdValue(ServerVersion version) {
    if (version.compareTo(ServerVersion.V1_13_0) >= 0)
      return material.getKey().toString();

    return material.name().toLowerCase();
  }
}
