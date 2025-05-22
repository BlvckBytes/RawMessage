package at.blvckbytes.raw_message.click;

import at.blvckbytes.raw_message.ServerVersion;
import at.blvckbytes.raw_message.json.JsonObject;

public class RunCommandAction extends ClickAction {

  private final String command;

  public RunCommandAction(String command) {
    this.command = command;
  }

  @Override
  public void appendSelf(JsonObject component, ServerVersion version) {
    JsonObject container = makeAndAppendContainer(component, version);
    container.add("action", "run_command");

    if (version.compareTo(ServerVersion.V1_21_5) >= 0)
      container.add("command", command);
    else
      container.add("value", command);
  }

  @Override
  public ClickAction duplicate() {
    return new RunCommandAction(command);
  }
}
