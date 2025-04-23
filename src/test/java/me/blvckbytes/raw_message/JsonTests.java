package me.blvckbytes.raw_message;

import at.blvckbytes.raw_message.json.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonTests {

  private static final JsonSerializer SERIALIZER_JSON = new JsonSerializer(false);
  private static final JsonSerializer SERIALIZER_SNBT = new JsonSerializer(true);

  @Test
  public void shouldSerializeIntegers() {
    assertEquals("55", SERIALIZER_JSON.serialize(new JsonInteger(55)));
    assertEquals("-32", SERIALIZER_JSON.serialize(new JsonInteger(-32)));
  }

  @Test
  public void shouldSerializeBooleans() {
    assertEquals("true", SERIALIZER_JSON.serialize(new JsonBoolean(true)));
    assertEquals("false", SERIALIZER_JSON.serialize(new JsonBoolean(false)));
  }

  @Test
  public void shouldSerializeStrings() {
    assertEquals("\"hello\"", SERIALIZER_JSON.serialize(new JsonString("hello")));
    assertEquals("\"hello world\"", SERIALIZER_JSON.serialize(new JsonString("hello world")));
    assertEquals("\"hel\\\"lo\"", SERIALIZER_JSON.serialize(new JsonString("hel\"lo")));
    assertEquals("\"hel\\\"lo\\\\\\\"\"", SERIALIZER_JSON.serialize(new JsonString("hel\"lo\\\"")));
  }

  @Test
  public void shouldSerializeArrays() {
    assertEquals(
      "[\"1\",2,true]",
      SERIALIZER_JSON.serialize(
        new JsonArray()
          .add("1")
          .add(2)
          .add(true)
      )
    );
  }

  @Test
  public void shouldSerializeObjects() {
    assertEquals(
      "{\"a\":\"b\",\"b\":1,\"c\":true}",
      SERIALIZER_JSON.serialize(
        new JsonObject()
          .add("a", "b")
          .add("b", 1)
          .add("c", true)
      )
    );

    assertEquals(
      "{a:\"b\",b:1,c:true}",
      SERIALIZER_SNBT.serialize(
        new JsonObject()
          .add("a", "b")
          .add("b", 1)
          .add("c", true)
      )
    );
  }
}
