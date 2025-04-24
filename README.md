# RawMessage

A concise and independent library, leveraging the `tellraw`-command to send raw JSON chat-components to players, supporting various features like click-/hover-events, color, style, translation and the like. Since minecraft revises this JSON-format all the time, quite a few case-decisions are to be made and maintained in regard to future versions - may this little library be the place to do so.

## Supported Versions

While proper full-range testing hasn't been completed yet, various manual trials showed promising compatibility on the whole of `1.8.8` to `1.21.5`.

## Maven Artifact

In order to avoid the friction cloning, building from source and installing into one's local maven-repository brings with itself, this library is hosted on Maven Central - simply add it to your project as a dependency:

```xml
<dependency>
    <groupId>at.blvckbytes</groupId>
    <artifactId>RawMessage</artifactId>
    <version>0.2</version>
</dependency>
```

## Usage

By composing various elements this library has to offer, a message can be specified step-by-step in a piecewise manner, without the need to specify any version-dependent information. Simply start by constructing a new `RawMessage`:

```java
void buildAndSendMyMessage(Player receiver) {
  new RawMessage()
    .tellRawTo(receiver);
}
```

The text of a message can be specified either directly at its constructor, or via the according builder-method.

```java
void buildAndSendMyMessage(Player receiver) {
  new RawMessage("Specify the text here")
    .setText("OR specify it in the builder")
    .tellRawTo(receiver);
}
```

As for styling, all the legacy colors are available - but you can, if the server-version offers support, also specify hexadecimal values. Formatting can be enabled, disabled or cleared; disabling sets the according component-flag to `false`, thereby overriding implicit styles, like lore being italic by default; clearing makes the flag vanish from the resulting component altogether.

```java
void buildAndSendMyMessage(Player receiver) {
  new RawMessage("Specify the text here")
    .setColor(MessageColor.LIGHT_PURPLE)
    .setColor(MessageColor.ofHex("#03BA2C"))
    .enableStyle(MessageStyle.BOLD)
    .disableStyle(MessageStyle.ITALIC)
    .tellRawTo(receiver);
}
```

Next up, Minecraft also supports to render and even parameterize keys within the client's language-file - if a message has translation-content, text-content will be ignored; parameters may be specified as additional components as a follow-up parameter.

```java
void buildAndSendMyMessage(Player receiver) {
  new RawMessage()
    .setTranslate("chat.type.advancement.task", Arrays.asList(
      new RawMessage("Placeholder value")
        .setColor(MessageColor.AQUA)
    ));
}
```

But messages have yet another rather useful feature - `extra` parts! With this, it immediately follows that the structure of a `RawMessage` does not just have to be a sequence, but may just as well be a tree; this allows you to build messages consisting of multiple parts somewhere else, and then plugging them into yet another message, as a sequence-element, allowing for more flexible abstractions.

```java
void buildAndSendMyMessage(Player receiver) {
  new RawMessage("Part one")
    .addExtra(
      new RawMessage("Part two")
        .addExtra(new RawMessage("Part three"))
    )
    .addExtra(new RawMessage("Part four"));
}
```

With all the basic content out of the way, let's look into events - starting out with click-events, as they're the least complicated.

```java
void buildAndSendMyMessage(Player receiver) {
  new RawMessage("My clickable message")
    .setClickAction(new CopyToClipboardAction("clipboard value"))
    // Opening files has been disabled (and will throw an error!) on modern versions, for obvious security-related reasons.
    .setClickAction(new OpenFileAction("/Users/blvckbytes/Desktop/my-file.txt"))
    .setClickAction(new OpenUrlAction("https://google.com"))
    .setClickAction(new RunCommandAction("/help"))
    .setClickAction(new SuggestCommandAction("/me Is sleepy."));
}
```

Next up are hover-actions, with the simplest again being rendering a component as a tooltip when hovering a message.

```java
void buildAndSendMyMessage(Player receiver) {
  new RawMessage("My hoverable message")
    .setHoverAction(new ShowTextAction(
      new RawMessage("Hello there - thanks for hovering! :)")
        .setColor(MessageColor.GOLD)
    ));
}
```

Last but not least, this tooltip may also be that of an item, which - in essence - allows for a multi-line value; while properties like enchantments, flags, etc. would be supported, for the time being, simply a type, name and lore-lines will suffice, as you can always manually create lookalikes.

```java
void buildAndSendMyMessage(Player receiver) {
  new RawMessage("My hoverable message")
    .setHoverAction(
      new ShowItemAction(Material.STICKY_PISTON)
        .setName(
          new RawMessage("My Item-Name")
            .setColor(MessageColor.RED)
            .enableStyle(MessageStyle.BOLD)
        )
        .addLoreLine(
          new RawMessage("Lore-Line 1")
            .setColor(MessageColor.LIGHT_PURPLE)
        )
        .addLoreLine(
          new RawMessage("Lore-Line 2")
            .setColor(MessageColor.DARK_PURPLE)
        )
    );
}
```