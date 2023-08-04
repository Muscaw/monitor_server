package dev.muscaw.monitor.weather.api;

import java.util.concurrent.CompletableFuture;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface WeatherApiEndpoint {
  @GET("/v1/current.json")
  @Headers("accept: application/json")
  CompletableFuture<WeatherApiWeather> getCurrentWeather(
      @Query("key") String key, @Query("q") String latLon);
}
