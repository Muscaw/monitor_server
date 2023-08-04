package dev.muscaw.monitor.conf;

import static dev.muscaw.monitor.conf.Env.getEnv;

import dev.muscaw.monitor.weather.api.WeatherApiWeatherServiceImpl;
import dev.muscaw.monitor.weather.domain.WeatherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherConfiguration {

  @Bean
  public WeatherService createWeatherService() {
    String token = getEnv("WEATHER_API_TOKEN").orElseThrow();
    String baseUrl = getEnv("WEATHER_API_BASEURL").orElse("https://api.weatherapi.com");
    return new WeatherApiWeatherServiceImpl(baseUrl, token);
  }
}
