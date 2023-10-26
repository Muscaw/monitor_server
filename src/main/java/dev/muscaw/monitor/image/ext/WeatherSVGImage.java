package dev.muscaw.monitor.image.ext;

import dev.muscaw.monitor.image.domain.DeviceConfiguration;
import dev.muscaw.monitor.image.domain.Renderable;
import dev.muscaw.monitor.weather.domain.Weather;
import java.util.List;

public class WeatherSVGImage implements Renderable {

  public static final int MARGIN_PX = 20;
  private final SVGImage image;

  WeatherSVGImage(SVGImage image, Weather weather) {
    this.image = image;

    image.drawStringAt(MARGIN_PX, MARGIN_PX, "METEO " + weather.locationName());
    image.drawStringTable(
        MARGIN_PX,
            (int) (MARGIN_PX + image.getStringHeight() * 1.5),
        image.getImageWidth() - MARGIN_PX * 2,
        List.of(
            List.of(
                "C°: " + weather.temperature().temperatureC(),
                "RH: " + weather.humidity().percentage() + "%",
                "UV: " + weather.uvLevel().uvLevel()),
            List.of(
                "Wind: " + weather.wind().windKph() + " kph",
                "\t\tgust " + weather.wind().gustKph() + " kph",
                "\t\tdir "
                    + weather.wind().direction().direction()
                    + "("
                    + weather.wind().direction().rotation()
                    + "°)",
                "Rain: " + weather.precipitation().millimeters() + " mm")));
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
