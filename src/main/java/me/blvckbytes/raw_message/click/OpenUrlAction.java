package me.blvckbytes.raw_message.click;

import com.google.gson.JsonObject;
import me.blvckbytes.raw_message.ServerVersion;

public class OpenUrlAction extends ClickAction {

  private final String url;

  public OpenUrlAction(String url) {
    this.url = url;
  }

  @Override
  public void appendSelf(JsonObject component, ServerVersion version) {
    JsonObject container = makeAndAppendContainer(component, version);
    container.addProperty("action", "open_url");

    if (version.compareTo(ServerVersion.V1_21_5) >= 0)
      container.addProperty("url", url);
    else
      container.addProperty("value", url);
  }
}
