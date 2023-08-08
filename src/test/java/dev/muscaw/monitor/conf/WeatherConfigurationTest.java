package dev.muscaw.monitor.conf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.muscaw.monitor.util.domain.LatLon;
import dev.muscaw.monitor.weather.api.WeatherApiWeatherServiceImpl;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WeatherConfigurationTest {
  private Env env;
  private WeatherConfiguration configuration;

  @BeforeEach
  public void setUp() {
    this.env = mock(Env.class);

    this.configuration = new WeatherConfiguration();
  }

  @Test
  public void createWeatherService_success() {
    when(env.getEnv("WEATHER_API_TOKEN")).thenReturn(Optional.of("some-token"));
    when(env.getEnv("WEATHER_API_BASEURL")).thenReturn(Optional.of("https://some-api.com"));

    var service = configuration.createWeatherService(env);

    assertThat(service).isInstanceOf(WeatherApiWeatherServiceImpl.class);
    assertThat(service).extracting("token").isEqualTo("some-token");
  }

  @Test
  public void createWeatherService_baseUrlDefaultValue() {
    when(env.getEnv("WEATHER_API_TOKEN")).thenReturn(Optional.of("some-token"));
    when(env.getEnv("WEATHER_API_BASEURL")).thenReturn(Optional.empty());

    var service = configuration.createWeatherService(env);

    assertThat(service).isInstanceOf(WeatherApiWeatherServiceImpl.class);
    assertThat(service).extracting("token").isEqualTo("some-token");
  }

  @Test
  public void createWeatherService_missingToken() {
    when(env.getEnv("WEATHER_API_TOKEN")).thenReturn(Optional.empty());
    when(env.getEnv("WEATHER_API_BASEURL")).thenReturn(Optional.of("https://some-api.com"));

    assertThrows(NoSuchElementException.class, () -> configuration.createWeatherService(env));
  }

  @Test
  public void getWeatherLocation_success() {
    when(env.getEnv("WEATHER_LAT_LON")).thenReturn(Optional.of("10.2,13.5"));

    var latLon = configuration.getWeatherLocation(env);
    assertThat(latLon).isEqualTo(new LatLon(10.2f, 13.5f));
  }

  @Test
  public void getWeatherLocation_missingEnvVariable() {
    when(env.getEnv("WEATHER_LAT_LON")).thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class, () -> configuration.getWeatherLocation(env));
  }

  @Test
  public void getWeatherLocation_couldNotParse() {
    when(env.getEnv("WEATHER_LAT_LON")).thenReturn(Optional.of("some random, text"));

    assertThrows(NumberFormatException.class, () -> configuration.getWeatherLocation(env));
  }
}
