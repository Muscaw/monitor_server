package dev.muscaw.monitor.image.ext;

import dev.muscaw.monitor.image.domain.DeviceConfiguration;
import dev.muscaw.monitor.image.domain.Renderable;
import dev.muscaw.monitor.weather.domain.Weather;
import java.awt.*;
import java.util.List;

public class WeatherSVGImage implements Renderable {

  public static final int MARGIN_PX = 5;
  private final SVGImage image;

  WeatherSVGImage(SVGImage image, Weather weather, DeviceConfiguration deviceConfig) {
    this.image = image;

    image.drawStringAt(MARGIN_PX, MARGIN_PX, "METEO " + weather.locationName());
    image.drawLines(
        MARGIN_PX,
        MARGIN_PX + image.getStringHeight(),
        List.of(
            "\tC°: " + weather.temperature().temperatureC(),
            "\tRH: " + weather.humidity().percentage(),
            "\tUV: " + weather.uvLevel().uvLevel(),
            "\tWind: "
                + weather.wind().windKph()
                + "kph (gust "
                + weather.wind().gustKph()
                + " kph) dir: "
                + weather.wind().direction().direction()
                + "("
                + weather.wind().direction().rotation()
                + ")"));
    image.drawRect(0, 0, deviceConfig.width() - 1, deviceConfig.height() - 1);
  }

  public static WeatherSVGImage newImage(
      DeviceConfiguration deviceConfig, Weather weather, Font font) {
    SVGImage image = SVGImage.newImage(deviceConfig.width(), deviceConfig.height(), font);
    return new WeatherSVGImage(image, weather, deviceConfig);
  }

  @Override
  public String getSVGDocument() {
    return image.getSVGDocument();
  }

  @Override
  public byte[] getPNGImage() {
    return image.getPNGImage();
  }

  @Override
  public byte[] asSerial() {
    return image.asSerial();
  }
}
