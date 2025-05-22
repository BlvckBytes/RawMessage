package at.blvckbytes.raw_message.click;

import at.blvckbytes.raw_message.ServerVersion;
import at.blvckbytes.raw_message.json.JsonObject;

public abstract class ClickAction {

  public abstract void appendSelf(JsonObject component, ServerVersion version);

  public abstract ClickAction duplicate();

  protected JsonObject makeAndAppendContainer(JsonObject component, ServerVersion version) {
    JsonObject container = new JsonObject();

    if (version.compareTo(ServerVersion.V1_21_5) >= 0)
      component.add("click_event", container);
    else
      component.add("clickEvent", container);

    return container;
  }
}
