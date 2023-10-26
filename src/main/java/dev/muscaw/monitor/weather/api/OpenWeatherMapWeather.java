package dev.muscaw.monitor.weather.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.muscaw.monitor.util.domain.LatLon;
import dev.muscaw.monitor.weather.domain.*;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenWeatherMapWeather(
    Coord coord, List<LocationWeather> weather, Main main, Wind wind, Rain rain) {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Coord(double lat, double lon) {
    public LatLon toDomain() {
      return new LatLon(this.lat, this.lon);
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record LocationWeather(int id, String main, String description, String icon) {
    public WeatherDescription getWeatherDescription() {
      // Weather description by id https://openweathermap.org/weather-conditions
      if (this.id >= 200 && this.id <= 232) {
        return WeatherDescription.THUNDERSTORM;
      } else if (this.id >= 300 && this.id <= 321) {
        return WeatherDescription.SHOWER_RAIN;
      } else if (this.id >= 500 && this.id <= 504) {
        return WeatherDescription.RAIN;
      } else if (this.id == 511) {
        return WeatherDescription.SNOW;
      } else if (this.id >= 520 && this.id <= 531) {
        return WeatherDescription.SHOWER_RAIN;
      } else if (this.id >= 600 && this.id <= 622) {
        return WeatherDescription.SNOW;
      } else if (this.id >= 701 && this.id <= 781) {
        return WeatherDescription.MIST;
      } else if (this.id == 800) {
        return WeatherDescription.CLEAR_SKY;
      } else if (this.id == 801) {
        return WeatherDescription.FEW_CLOUDS;
      } else if (this.id == 802) {
        return WeatherDescription.SCATTERED_CLOUDS;
      } else if (this.id == 803 || this.id == 804) {
        return WeatherDescription.BROKEN_CLOUDS;
      } else {
        return WeatherDescription.NOT_AVAILABLE;
      }
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Main(
      double temp,
      double feels_like,
      double temp_min,
      double temp_max,
      int pressure,
      int humidity,
      int sea_level,
      int grnd_level) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Wind(double speed, int deg, double gust) {
    public dev.muscaw.monitor.weather.domain.Wind toDomain() {
      return new dev.muscaw.monitor.weather.domain.Wind(
          this.speed, this.gust, CardinalDirection.fromAngle(this.deg));
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Rain(@JsonProperty("1h") double oneHour) {}

  public Weather toDomain(String locationName) {
    return new Weather(
        locationName,
        this.coord.toDomain(),
        new Temperature(this.main.temp),
        new RelativeHumidity(this.main.humidity),
        new UV(-1),
        this.wind().toDomain(),
        new Precipitation(this.rain != null ? this.rain.oneHour : 0),
        this.weather.stream()
            .findFirst()
            .map(LocationWeather::getWeatherDescription)
            .orElse(WeatherDescription.NOT_AVAILABLE));
  }
}
