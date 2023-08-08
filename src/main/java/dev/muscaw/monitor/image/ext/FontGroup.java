package dev.muscaw.monitor.image.ext;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class FontGroup {
  private Font base;
  private GraphicsEnvironment ge;
  private final Map<FontStyle, Font> fonts = new HashMap<>();

  public FontGroup(Font base, GraphicsEnvironment ge) {
    this.base = base;
    this.ge = ge;
  }

  record FontStyle(int style, float size) {}

  public Font generateFont(int style, float size) {
    return fonts.computeIfAbsent(
        new FontStyle(style, size),
        (fs) -> {
          var f = base.deriveFont(fs.style(), fs.size());
          ge.registerFont(f);
          return f;
        });
  }
}
