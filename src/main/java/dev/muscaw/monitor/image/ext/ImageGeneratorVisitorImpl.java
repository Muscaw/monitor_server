package dev.muscaw.monitor.image.ext;

import dev.muscaw.monitor.image.domain.ImageGeneratorVisitor;
import dev.muscaw.monitor.image.domain.PtDepartureOption;
import dev.muscaw.monitor.image.domain.Renderable;
import dev.muscaw.monitor.image.domain.WeatherOption;
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
    return WeatherImage.newImage(
        option.deviceConfiguration(), weatherIconLoader, option.weather(), imageFont);
  }

  @Override
  public Renderable renderDepartures(PtDepartureOption option) {
    return PtDepartureImage.newImage(option.deviceConfiguration(), option, imageFont);
  }
}
