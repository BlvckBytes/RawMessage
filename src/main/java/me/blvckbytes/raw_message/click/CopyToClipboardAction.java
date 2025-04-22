package me.blvckbytes.raw_message.click;

import com.google.gson.JsonObject;
import me.blvckbytes.raw_message.ServerVersion;

public class CopyToClipboardAction extends ClickAction {

  private final String value;

  public CopyToClipboardAction(String value) {
    this.value = value;
  }

  @Override
  public void appendSelf(JsonObject component, ServerVersion version) {
    JsonObject container = makeAndAppendContainer(component, version);
    container.addProperty("action", "copy_to_clipboard");
    container.addProperty("value", value);
  }
}
