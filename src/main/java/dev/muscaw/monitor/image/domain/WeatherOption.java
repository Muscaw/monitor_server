package dev.muscaw.monitor.image.domain;

import dev.muscaw.monitor.weather.domain.Weather;

public record WeatherOption(DeviceConfiguration configuration, Weather weather)
    implements RenderableOption {
  @Override
  public Renderable accept(ImageGeneratorVisitor visitor) {
    return visitor.renderWeather(this);
  }
}
