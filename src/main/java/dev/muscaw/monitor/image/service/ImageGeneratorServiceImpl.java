package dev.muscaw.monitor.image.service;

import dev.muscaw.monitor.image.domain.ImageGeneratorService;
import dev.muscaw.monitor.image.domain.ImageGeneratorVisitor;
import dev.muscaw.monitor.image.domain.Renderable;
import dev.muscaw.monitor.image.domain.RenderableOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageGeneratorServiceImpl implements ImageGeneratorService {

  public ImageGeneratorVisitor visitor;

  @Autowired
  public ImageGeneratorServiceImpl(ImageGeneratorVisitor visitor) {
    this.visitor = visitor;
  }

  @Override
  public Renderable generateImage(RenderableOption option) {
    return option.accept(visitor);
  }
}
