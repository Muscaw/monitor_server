package dev.muscaw.monitor.weather.api;

import dev.muscaw.monitor.util.domain.LatLon;
import dev.muscaw.monitor.weather.domain.Weather;
import dev.muscaw.monitor.weather.domain.WeatherService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class WeatherApiWeatherServiceImpl implements WeatherService {

  private String token;
  private WeatherApiEndpoint client;

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
    CompletableFuture<WeatherApiWeather> response =
        client.getCurrentWeather(this.token, location.as2DecLatLonString());

    WeatherApiWeather result;
    try {
      result = response.get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    return null;
  }
}
