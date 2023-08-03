package dev.muscaw.monitor.image.domain;

public interface Renderable {
  String getSVGDocument();

  byte[] getPNGImage();

  String asSerial();
}