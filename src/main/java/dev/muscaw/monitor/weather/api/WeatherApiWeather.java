package dev.muscaw.monitor.weather.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.muscaw.monitor.util.domain.LatLon;
import dev.muscaw.monitor.weather.domain.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherApiWeather(Location location, CurrentWeather current) {
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Location(String name, float lat, float lon) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record CurrentWeather(
      float temp_c,
      float wind_kph,
      int wind_degree,
      String wind_dir,
      float precip_mm,
      int humidity,
      float uv,
      float gust_kph) {}

  public Weather toDomain() {
    return new Weather(
        location.name,
        new LatLon(location.lat, location.lon),
        new Temperature(current.temp_c),
        new RelativeHumidity(current.humidity),
        new UV(current.uv),
        new Wind(
            current.wind_kph,
            current.gust_kph,
            new CardinalDirection(current.wind_dir, current.wind_degree)),
        new Precipitation(current.precip_mm));
  }
}
