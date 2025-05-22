package at.blvckbytes.raw_message.hover;

import at.blvckbytes.raw_message.RawMessage;
import at.blvckbytes.raw_message.ServerVersion;
import at.blvckbytes.raw_message.json.JsonObject;
import at.blvckbytes.raw_message.json.JsonSerializer;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ShowEntityAction extends HoverAction {

  // Note: This action has been introduced in 1.13; F3+h needs to be enabled to render the tooltip

  private static final JsonSerializer SERIALIZER_JSON = new JsonSerializer(false);

  private final UUID id;
  private final EntityType type;
  private @Nullable RawMessage name;

  public ShowEntityAction(EntityType type, UUID id) {
    this.type = type;
    this.id = id;
  }

  public ShowEntityAction setName(@Nullable RawMessage name) {
    this.name = name;
    return this;
  }

  @Override
  @SuppressWarnings("deprecation")
  public void appendSelf(JsonObject component, ServerVersion version) {
    JsonObject container = makeAndAppendContainer(component, version);

    container.add("action", "show_entity");

    String entityType = "minecraft:" + type.getName().toLowerCase();

    // Properties have been inlined
    if (version.compareTo(ServerVersion.V1_21_5) >= 0) {
      container.add("id", entityType);
      container.add("uuid", id.toString());

      if (name != null)
        container.add("name", name.toJsonObject(version));

      return;
    }

    JsonObject dataObject = new JsonObject();

    dataObject.add("type", entityType);
    dataObject.add("id", id.toString());

    // Key "value" has been deprecated; contents now also is an object
    if (version.compareTo(ServerVersion.V1_16_0) >= 0) {
      if (name != null)
        dataObject.add("name", name.toJsonObject(version));

      container.add("contents", dataObject);
      return;
    }

    if (name != null)
      dataObject.add("name", name.toJsonString(version));

    container.add("value", SERIALIZER_JSON.serialize(dataObject));
  }

  @Override
  public HoverAction duplicate() {
    ShowEntityAction duplicate = new ShowEntityAction(type, id);

    if (name != null)
      duplicate.name = name.duplicate();

    return duplicate;
  }
}
