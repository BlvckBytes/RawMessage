package at.blvckbytes.raw_message.click;

import at.blvckbytes.raw_message.ServerVersion;
import at.blvckbytes.raw_message.json.JsonObject;

public class OpenUrlAction extends ClickAction {

  private final String url;

  public OpenUrlAction(String url) {
    this.url = url;
  }

  @Override
  public void appendSelf(JsonObject component, ServerVersion version) {
    JsonObject container = makeAndAppendContainer(component, version);
    container.add("action", "open_url");

    if (version.compareTo(ServerVersion.V1_21_5) >= 0)
      container.add("url", url);
    else
      container.add("value", url);
  }
}
