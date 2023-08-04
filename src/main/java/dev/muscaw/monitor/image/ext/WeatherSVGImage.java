package dev.muscaw.monitor.image.ext;

import dev.muscaw.monitor.image.domain.DeviceConfiguration;
import dev.muscaw.monitor.weather.domain.Weather;
import java.awt.Color;
import java.util.List;

public class WeatherSVGImage extends SVGImage {

  public static final int MARGIN_PX = 5;

  private WeatherSVGImage(DeviceConfiguration deviceConfig, Weather weather) {
    super(deviceConfig.width(), deviceConfig.height());

    drawStringAt(MARGIN_PX, MARGIN_PX, "METEO " + weather.locationName());
    drawLines(
        MARGIN_PX,
        MARGIN_PX + getStringHeight(),
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
    g2.setColor(Color.BLACK);
    g2.drawRect(0, 0, deviceConfig.width(), deviceConfig.height());
  }

  public static WeatherSVGImage newImage(DeviceConfiguration deviceConfig, Weather weather) {
    return new WeatherSVGImage(deviceConfig, weather);
  }
}
