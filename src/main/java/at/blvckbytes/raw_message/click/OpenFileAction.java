package at.blvckbytes.raw_message.click;

import at.blvckbytes.raw_message.ServerVersion;
import at.blvckbytes.raw_message.json.JsonObject;

public class OpenFileAction extends ClickAction {

  private final String path;

  public OpenFileAction(String path) {
    this.path = path;
  }

  @Override
  public void appendSelf(JsonObject component, ServerVersion version) {
    JsonObject container = makeAndAppendContainer(component, version);
    container.add("action", "open_file");
    container.add("value", path);
  }
}
