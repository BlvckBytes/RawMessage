package at.blvckbytes.raw_message.json;

import java.util.ArrayList;
import java.util.List;

public class JsonArray implements JsonElement {

  public final List<JsonElement> items;

  public JsonArray() {
    this(new ArrayList<>());
  }

  public JsonArray(List<JsonElement> items) {
    this.items = items;
  }

  public JsonArray add(JsonElement value) {
    this.items.add(value);
    return this;
  }

  public JsonArray add(String value) {
    this.items.add(new JsonString(value));
    return this;
  }
  public JsonArray add(int value) {
    this.items.add(new JsonInteger(value));
    return this;
  }

  public JsonArray add(boolean value) {
    this.items.add(new JsonBoolean(value));
    return this;
  }

  @Override
  public Object getValue() {
    return this.items;
  }
}
