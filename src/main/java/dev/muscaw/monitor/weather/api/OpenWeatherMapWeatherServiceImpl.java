package dev.muscaw.monitor.weather.api;

import dev.muscaw.monitor.util.domain.LatLon;
import dev.muscaw.monitor.weather.domain.Weather;
import dev.muscaw.monitor.weather.domain.WeatherService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class OpenWeatherMapWeatherServiceImpl implements WeatherService {

  public static final String UNITS = "metric";
  public static final int MAX_RETRIES = 3;
  private final String token;

  private final OpenWeatherMapApiEndpoint client;

  public OpenWeatherMapWeatherServiceImpl(String baseUrl, String token) {
    this.token = token;

    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    client = retrofit.create(OpenWeatherMapApiEndpoint.class);
  }

  @Override
  public Weather getCurrentWeather(LatLon location) {
    List<ReverseLocationLookup> locationNames =
        retryGetReverseLocationLookup(location, MAX_RETRIES);
    return retryGetCurrentWeather(
        location,
        locationNames.stream().findFirst().map(ReverseLocationLookup::name).orElse("N/A"),
        MAX_RETRIES);
  }

  public List<ReverseLocationLookup> retryGetReverseLocationLookup(
      LatLon location, int retriesLeft) {
    CompletableFuture<List<ReverseLocationLookup>> response =
        client.locationNameLookup(this.token, location.lat(), location.lon(), 1);
    List<ReverseLocationLookup> result;
    try {
      result = response.get();
    } catch (InterruptedException | ExecutionException e) {
      if (retriesLeft > 0) {
        return retryGetReverseLocationLookup(location, retriesLeft - 1);
      } else {
        // After we went through all retries, we throw
        throw new RuntimeException(e);
      }
    }
    return result;
  }

  public Weather retryGetCurrentWeather(LatLon location, String locationName, int retriesLeft) {
    CompletableFuture<OpenWeatherMapWeather> response =
        client.getCurrentWeather(this.token, location.lat(), location.lon(), UNITS);
    OpenWeatherMapWeather result;
    try {
      result = response.get();
    } catch (InterruptedException | ExecutionException e) {
      if (retriesLeft > 0) {
        return retryGetCurrentWeather(location, locationName, retriesLeft - 1);
      } else {
        // After we went through all retries, we throw
        throw new RuntimeException(e);
      }
    }
    return result.toDomain(locationName);
  }
}
