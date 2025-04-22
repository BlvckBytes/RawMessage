package at.blvckbytes.raw_message.click;

import com.google.gson.JsonObject;
import at.blvckbytes.raw_message.ServerVersion;

public abstract class ClickAction {

  public abstract void appendSelf(JsonObject component, ServerVersion version);

  protected JsonObject makeAndAppendContainer(JsonObject component, ServerVersion version) {
    JsonObject container = new JsonObject();

    if (version.compareTo(ServerVersion.V1_21_5) >= 0)
      component.add("click_event", container);
    else
      component.add("clickEvent", container);

    return container;
  }
}
