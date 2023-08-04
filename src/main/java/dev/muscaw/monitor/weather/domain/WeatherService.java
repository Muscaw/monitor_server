package dev.muscaw.monitor.weather.domain;

import dev.muscaw.monitor.util.domain.LatLon;

public interface WeatherService {
  Weather getCurrentWeather(LatLon location);
}
