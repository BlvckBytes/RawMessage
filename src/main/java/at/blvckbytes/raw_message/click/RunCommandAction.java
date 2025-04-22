package at.blvckbytes.raw_message.click;

import com.google.gson.JsonObject;
import at.blvckbytes.raw_message.ServerVersion;

public class RunCommandAction extends ClickAction {

  private final String command;

  public RunCommandAction(String command) {
    this.command = command;
  }

  @Override
  public void appendSelf(JsonObject component, ServerVersion version) {
    JsonObject container = makeAndAppendContainer(component, version);
    container.addProperty("action", "run_command");

    if (version.compareTo(ServerVersion.V1_21_5) >= 0)
      container.addProperty("command", command);
    else
      container.addProperty("value", command);
  }
}
