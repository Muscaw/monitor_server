package dev.muscaw.monitor.image.ext;

import dev.muscaw.monitor.image.domain.DeviceConfiguration;
import dev.muscaw.monitor.image.domain.Renderable;
import dev.muscaw.monitor.weather.domain.Weather;
import java.util.List;

public class WeatherImage implements Renderable {

  public static final int MARGIN_PX = 20;
  private final SVGImage image;

  WeatherImage(SVGImage image, WeatherIconLoader iconLoader, Weather weather) {
    this.image = image;

    this.image.drawStringAt(MARGIN_PX, MARGIN_PX, "METEO " + weather.locationName());
    this.image.drawStringTable(
        MARGIN_PX,
        (int) (MARGIN_PX + image.getStringHeight() * 2),
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

    // Compute if there is enough space for icon
    int availableWidth = this.image.getImageWidth();
    int startY =
        MARGIN_PX
            + image.getStringHeight()
                * 5; // Where we currently are in terms of y location with the text
    int availableHeight = this.image.getImageHeight() - startY;

    // We want to keep the icon square and fully visible
    int sideDimension = Math.min(availableHeight, availableWidth);
    if (sideDimension >= 30) { // We don't show the icon if it's too small
      image.drawImage(
          iconLoader.getPathToIcon(weather.weatherDescription()),
          MARGIN_PX,
          startY,
          sideDimension,
          sideDimension);
    }
  }

  public static WeatherImage newImage(
      DeviceConfiguration deviceConfig,
      WeatherIconLoader iconLoader,
      Weather weather,
      FontGroup font) {
    SVGImage image = SVGImage.newImage(deviceConfig.width(), deviceConfig.height(), font);
    return new WeatherImage(image, iconLoader, weather);
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
