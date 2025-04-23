package at.blvckbytes.raw_message.json;

public class JsonString implements JsonElement {

  public final String value;

  public JsonString(String value) {
    this.value = value;
  }

  @Override
  public Object getValue() {
    return this.value;
  }
}
