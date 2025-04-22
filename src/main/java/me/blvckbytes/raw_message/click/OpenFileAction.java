package me.blvckbytes.raw_message.click;

import com.google.gson.JsonObject;
import me.blvckbytes.raw_message.ServerVersion;

public class OpenFileAction extends ClickAction {

  private final String path;

  public OpenFileAction(String path) {
    this.path = path;
  }

  @Override
  public void appendSelf(JsonObject component, ServerVersion version) {
    JsonObject container = makeAndAppendContainer(component, version);
    container.addProperty("action", "open_file");
    container.addProperty("value", path);
  }
}
