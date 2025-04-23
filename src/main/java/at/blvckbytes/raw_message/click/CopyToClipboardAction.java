package at.blvckbytes.raw_message.click;

import at.blvckbytes.raw_message.ServerVersion;
import at.blvckbytes.raw_message.json.JsonObject;

public class CopyToClipboardAction extends ClickAction {

  private final String value;

  public CopyToClipboardAction(String value) {
    this.value = value;
  }

  @Override
  public void appendSelf(JsonObject component, ServerVersion version) {
    JsonObject container = makeAndAppendContainer(component, version);
    container.add("action", "copy_to_clipboard");
    container.add("value", value);
  }
}
