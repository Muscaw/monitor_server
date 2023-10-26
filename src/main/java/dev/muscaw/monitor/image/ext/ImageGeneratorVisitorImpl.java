package dev.muscaw.monitor.image.ext;

import dev.muscaw.monitor.image.domain.ImageGeneratorVisitor;
import dev.muscaw.monitor.image.domain.Renderable;
import dev.muscaw.monitor.image.domain.WeatherOption;
import java.awt.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImageGeneratorVisitorImpl implements ImageGeneratorVisitor {
  private final FontGroup imageFont;
  private final WeatherIconLoader weatherIconLoader;

  @Autowired
  public ImageGeneratorVisitorImpl(FontGroup imageFont, WeatherIconLoader weatherIconLoader) {
    this.imageFont = imageFont;
    this.weatherIconLoader = weatherIconLoader;
  }

  @Override
  public Renderable renderWeather(WeatherOption option) {
    return WeatherSVGImage.newImage(
        option.configuration(), weatherIconLoader, option.weather(), imageFont);
  }
}
