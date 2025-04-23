package at.blvckbytes.raw_message.json;

import java.util.Map;

public class JsonSerializer {

  private final boolean snbtMode;

  public JsonSerializer(boolean snbtMode) {
    this.snbtMode = snbtMode;
  }

  private String serializeString(JsonString jsonString) {
    StringBuilder result = new StringBuilder(jsonString.value.length());

    result.append('"');

    char priorChar = 0;

    for (int charIndex = 0; charIndex < jsonString.value.length(); ++charIndex) {
      char currentChar = jsonString.value.charAt(charIndex);

      if (currentChar == '"') {
        if (priorChar == '\\')
          result.append('\\');

        result.append('\\');
      }

      result.append(currentChar);
      priorChar = currentChar;
    }

    result.append('"');

    return result.toString();
  }

  private String serializeArray(JsonArray jsonArray) {
    StringBuilder result = new StringBuilder("[");

    for (JsonElement item : jsonArray.items) {
      if (result.length() != 1)
        result.append(',');

      result.append(serialize(item));
    }

    result.append(']');

    return result.toString();
  }

  private String serializeObject(JsonObject jsonObject) {
    StringBuilder result = new StringBuilder("{");

    for (Map.Entry<String, JsonElement> entry : jsonObject.entries.entrySet()) {
      if (result.length() != 1)
        result.append(',');

      if (!snbtMode)
        result.append('"');

      // Let's save on resources trying to escape quotes - nobody will put any into keys
      result.append(entry.getKey());

      if (!snbtMode)
        result.append('"');

      result.append(':');

      result.append(serialize(entry.getValue()));
    }

    result.append('}');

    return result.toString();
  }

  public String serialize(JsonElement element) {
    if (element instanceof JsonBoolean || element instanceof JsonInteger)
      return String.valueOf(element.getValue());

    if (element instanceof JsonString)
      return serializeString((JsonString) element);

    if (element instanceof JsonArray)
      return serializeArray((JsonArray) element);

    if (element instanceof JsonObject)
      return serializeObject((JsonObject) element);

    throw new IllegalStateException("Unsupported element-type " + (element == null ? null : element.getClass()));
  }
}
