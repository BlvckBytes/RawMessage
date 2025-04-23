package at.blvckbytes.raw_message.json;

public class JsonBoolean implements JsonElement {

  public final boolean value;

  public JsonBoolean(boolean value) {
    this.value = value;
  }

  @Override
  public Object getValue() {
    return this.value;
  }
}
