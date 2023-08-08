package dev.muscaw.monitor.image.ext;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public record FontGroup(Font base, GraphicsEnvironment ge) {
  record FontStyle(int style, float size) {}

  private static final Map<FontStyle, Font> fonts = new HashMap<>();

  public Font generateFont(int style, float size) {
    return fonts.computeIfAbsent(
        new FontStyle(style, size),
        (fs) -> {
          var f = base.deriveFont(fs.style(), fs.size());
          this.registerFont(f);
          return f;
        });
  }

  private void registerFont(Font font) {
    ge.registerFont(font);
  }
}
