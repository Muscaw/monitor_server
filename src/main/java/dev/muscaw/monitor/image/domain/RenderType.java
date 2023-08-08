package dev.muscaw.monitor.image.domain;

public enum RenderType {
  BW,
  GRAYSCALE;

  public static RenderType fromString(String value, RenderType defaultValue) {
    try {
      return RenderType.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException ex) {
      // Not a valid value
      return defaultValue;
    }
  }
}
