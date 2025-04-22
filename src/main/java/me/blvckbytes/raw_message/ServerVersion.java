package me.blvckbytes.raw_message;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerVersion implements Comparable<ServerVersion> {

  private static final Pattern MINECRAFT_VERSION_MATCHER = Pattern.compile("\\(MC: (\\d\\.\\d+(?:\\.\\d+)?)");

  public static final ServerVersion V1_21_5 = new ServerVersion(1, 21, 5);
  public static final ServerVersion V1_20_3 = new ServerVersion(1, 20, 3);
  public static final ServerVersion V1_20_5 = new ServerVersion(1, 20, 5);
  public static final ServerVersion V1_16_0 = new ServerVersion(1, 16, 0);
  public static final ServerVersion V1_13_0 = new ServerVersion(1, 13, 0);

  public final int phase;
  public final int major;
  public final int minor;

  public ServerVersion(int phase, int major, int minor) {
    this.phase = phase;
    this.major = major;
    this.minor = minor;
  }

  @Override
  public int compareTo(@NotNull ServerVersion o) {
    int result;

    if ((result = Integer.compare(this.phase, o.phase)) != 0)
      return result;

    if ((result = Integer.compare(this.major, o.major)) != 0)
      return result;

    return Integer.compare(this.minor, o.minor);
  }

  @Override
  public String toString() {
    return "v" + phase + "_" + major + "_" + minor;
  }

  public static @Nullable ServerVersion tryParseCurrent() {
    Matcher regexMatcher = MINECRAFT_VERSION_MATCHER.matcher(Bukkit.getVersion());

    if (!regexMatcher.find())
      return null;

    String[] versionParts = regexMatcher.group(1).split("\\.");

    if (versionParts.length < 2)
      return null;

    try {
      return new ServerVersion(
        Integer.parseInt(versionParts[0]),
        Integer.parseInt(versionParts[1]),
        versionParts.length == 3 ? Integer.parseInt(versionParts[2]) : 0
      );
    } catch (NumberFormatException exception) {
      return null;
    }
  }
}
