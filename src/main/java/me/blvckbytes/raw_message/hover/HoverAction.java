package me.blvckbytes.raw_message.hover;

import com.google.gson.JsonObject;
import me.blvckbytes.raw_message.ServerVersion;

public abstract class HoverAction {

  public abstract void appendSelf(JsonObject component, ServerVersion version);

  protected JsonObject makeAndAppendContainer(JsonObject component, ServerVersion version) {
    JsonObject container = new JsonObject();

    if (version.compareTo(ServerVersion.V1_21_5) >= 0)
      component.add("hover_event", container);
    else
      component.add("hoverEvent", container);

    return container;
  }
}
