package dev.muscaw.monitor.image.ext;

import dev.muscaw.monitor.weather.domain.WeatherDescription;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

public class WeatherIconLoader {
  private Map<WeatherDescription, Path> descriptionToIcon;

  private static AbstractMap.SimpleEntry<WeatherDescription, Path> entry(
      WeatherDescription desc, String iconName) {
    return new AbstractMap.SimpleEntry<>(
        desc, Paths.get("src/main/resources/svg/weather_icons/" + iconName + ".png"));
  }

  public WeatherIconLoader() {

    this.descriptionToIcon =
        Map.ofEntries(
            entry(WeatherDescription.CLEAR_SKY, "clear-day"),
            entry(WeatherDescription.SCATTERED_CLOUDS, "partly-cloudy-day"),
            entry(WeatherDescription.FEW_CLOUDS, "cloudy"),
            entry(WeatherDescription.BROKEN_CLOUDS, "extreme"),
            entry(WeatherDescription.SHOWER_RAIN, "drizzle"),
            entry(WeatherDescription.RAIN, "rain"),
            entry(WeatherDescription.THUNDERSTORM, "thunderstorms"),
            entry(WeatherDescription.SNOW, "snow"),
            entry(WeatherDescription.MIST, "mist"),
            entry(WeatherDescription.NOT_AVAILABLE, "not-available"));

    // Apply sanity checks before server starts
    this.descriptionToIcon.forEach(
        (weatherDescription, path) -> {
          try {
            ImageIO.read(path.toUri().toURL());
          } catch (IOException e) {
            throw new RuntimeException(
                "Could not load icon for weather description " + weatherDescription.name());
          }
        });

    Stream.of(WeatherDescription.values())
        .forEach(
            x -> {
              if (!this.descriptionToIcon.containsKey(x)) {
                throw new RuntimeException(
                    "Weather description is missing in WeatherIconLoader: " + x.name());
              }
            });
  }

  public Path getPathToIcon(WeatherDescription desc) {
    return this.descriptionToIcon.get(desc);
  }
}
