package at.blvckbytes.raw_message.click;

import at.blvckbytes.raw_message.ServerVersion;
import at.blvckbytes.raw_message.json.JsonObject;

public class SuggestCommandAction extends ClickAction {

  private final String command;

  public SuggestCommandAction(String command) {
    this.command = command;
  }

  @Override
  public void appendSelf(JsonObject component, ServerVersion version) {
    JsonObject container = makeAndAppendContainer(component, version);
    container.add("action", "suggest_command");

    if (version.compareTo(ServerVersion.V1_21_5) >= 0)
      container.add("command", command);
    else
      container.add("value", command);
  }

  @Override
  public ClickAction duplicate() {
    return new SuggestCommandAction(command);
  }
}
