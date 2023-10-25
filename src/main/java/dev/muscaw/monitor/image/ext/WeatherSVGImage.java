package dev.muscaw.monitor.image.ext;

import dev.muscaw.monitor.image.domain.DeviceConfiguration;
import dev.muscaw.monitor.image.domain.Renderable;
import dev.muscaw.monitor.weather.domain.Weather;
import java.util.List;

public class WeatherSVGImage implements Renderable {

  public static final int MARGIN_PX = 5;
  private final SVGImage image;

  WeatherSVGImage(SVGImage image, Weather weather) {
    this.image = image;

    image.drawStringAt(MARGIN_PX, MARGIN_PX, "METEO " + weather.locationName());
    image.drawStringTable(
        MARGIN_PX,
        MARGIN_PX + image.getStringHeight(),
        List.of(
            List.of(
                "C°: " + weather.temperature().temperatureC(),
                "RH: " + weather.humidity().percentage() + "%",
                "UV: " + weather.uvLevel().uvLevel()),
            List.of(
                "Wind: " + weather.wind().windKph() + " kph",
                "\tgust " + weather.wind().gustKph() + " kph",
                "\tdir "
                    + weather.wind().direction().direction()
                    + "("
                    + weather.wind().direction().rotation()
                    + "°)",
                "Rain: " + weather.precipitation().millimeters() + " mm")),
        250);
  }

  public static WeatherSVGImage newImage(
      DeviceConfiguration deviceConfig, Weather weather, FontGroup font) {
    SVGImage image = SVGImage.newImage(deviceConfig.width(), deviceConfig.height(), font);
    return new WeatherSVGImage(image, weather);
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
  public byte[] asBitmap() {
    return image.asBitmap();
  }
}
