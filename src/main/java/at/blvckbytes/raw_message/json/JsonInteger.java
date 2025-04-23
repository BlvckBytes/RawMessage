package at.blvckbytes.raw_message.json;

public class JsonInteger implements JsonElement {

  public final int value;

  public JsonInteger(int value) {
    this.value = value;
  }

  @Override
  public Object getValue() {
    return this.value;
  }
}
