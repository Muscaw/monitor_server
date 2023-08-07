package dev.muscaw.monitor.image.domain;

public interface RenderableOption {
  Renderable accept(ImageGeneratorVisitor visitor);
}
