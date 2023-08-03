package dev.muscaw.monitor.weather.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
}
