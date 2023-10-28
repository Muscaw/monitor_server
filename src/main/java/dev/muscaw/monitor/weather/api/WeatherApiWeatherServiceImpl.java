package dev.muscaw.monitor.weather.api;

import static dev.muscaw.monitor.util.http.Retryable.retry;

import dev.muscaw.monitor.util.domain.LatLon;
import dev.muscaw.monitor.weather.domain.Weather;
import dev.muscaw.monitor.weather.domain.WeatherService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class WeatherApiWeatherServiceImpl implements WeatherService {

  public static final int MAX_RETRIES = 3;
  private final String token;
  private final WeatherApiEndpoint client;

  public WeatherApiWeatherServiceImpl(String baseUrl, String token) {
    this.token = token;

    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    client = retrofit.create(WeatherApiEndpoint.class);
  }

  @Override
  public Weather getCurrentWeather(LatLon location) {
    return retry(
            () -> client.getCurrentWeather(this.token, location.as2DecLatLonString()), MAX_RETRIES)
        .toDomain();
  }
}
