package dev.muscaw.monitor.conf;

import dev.muscaw.monitor.util.domain.LatLon;
import dev.muscaw.monitor.weather.api.WeatherApiWeatherServiceImpl;
import dev.muscaw.monitor.weather.domain.WeatherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherConfiguration {

  @Bean
  public WeatherService createWeatherService(Env env) {
    String token = env.getEnv("WEATHER_API_TOKEN").orElseThrow();
    String baseUrl = env.getEnv("WEATHER_API_BASEURL").orElse("https://api.weatherapi.com");
    return new WeatherApiWeatherServiceImpl(baseUrl, token);
  }

  @Bean(name = "weatherLocation")
  public LatLon getWeatherLocation(Env env) {
    String latLon = env.getEnv("WEATHER_LAT_LON").orElseThrow();
    String[] values = latLon.split(",");

    return new LatLon(Float.parseFloat(values[0]), Float.parseFloat(values[1]));
  }
}
