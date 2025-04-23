package at.blvckbytes.raw_message.json;

import java.util.Map;
import java.util.TreeMap;

public class JsonObject implements JsonElement {

  public final Map<String, JsonElement> entries;

  public JsonObject() {
    this(new TreeMap<>());
  }

  public JsonObject(Map<String, JsonElement> entries) {
    this.entries = entries;
  }

  public JsonObject add(String key, JsonElement value) {
    this.entries.put(key, value);
    return this;
  }

  public JsonObject add(String key, String value) {
    this.entries.put(key, new JsonString(value));
    return this;
  }

  public JsonObject add(String key, int value) {
    this.entries.put(key, new JsonInteger(value));
    return this;
  }

  public JsonObject add(String key, boolean value) {
    this.entries.put(key, new JsonBoolean(value));
    return this;
  }

  @Override
  public Object getValue() {
    return entries;
  }
}
