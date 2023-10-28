package dev.muscaw.monitor.weather.api;

import static dev.muscaw.monitor.util.http.Retryable.retry;

import dev.muscaw.monitor.util.domain.LatLon;
import dev.muscaw.monitor.weather.domain.Weather;
import dev.muscaw.monitor.weather.domain.WeatherService;
import java.util.List;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class OpenWeatherMapWeatherServiceImpl implements WeatherService {

  public static final String UNITS = "metric";
  public static final int LOCATION_LOOKUP_LIMIT = 1;
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
        retry(
            () ->
                client.locationNameLookup(
                    this.token, location.lat(), location.lon(), LOCATION_LOOKUP_LIMIT),
            MAX_RETRIES);
    String locationName =
        locationNames.stream().findFirst().map(ReverseLocationLookup::name).orElse("N/A");
    return retry(
            () -> client.getCurrentWeather(this.token, location.lat(), location.lon(), UNITS),
            MAX_RETRIES)
        .toDomain(locationName);
  }
}
