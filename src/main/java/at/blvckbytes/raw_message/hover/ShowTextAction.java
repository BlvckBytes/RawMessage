package at.blvckbytes.raw_message.hover;

import com.google.gson.JsonObject;
import at.blvckbytes.raw_message.ServerVersion;
import at.blvckbytes.raw_message.RawMessage;

public class ShowTextAction extends HoverAction {

  private final RawMessage text;

  public ShowTextAction(RawMessage text) {
    this.text = text;
  }

  @Override
  public void appendSelf(JsonObject component, ServerVersion version) {
    JsonObject container = makeAndAppendContainer(component, version);

    container.addProperty("action", "show_text");

    if (version.compareTo(ServerVersion.V1_21_5) >= 0) {
      // Can they not make up their minds?
      container.add("value", text.toJsonObject(version));
      return;
    }

    if (version.compareTo(ServerVersion.V1_20_3) >= 0) {
      container.add("contents", text.toJsonObject(version));
      return;
    }

    container.add("value", text.toJsonObject(version));
  }
}
