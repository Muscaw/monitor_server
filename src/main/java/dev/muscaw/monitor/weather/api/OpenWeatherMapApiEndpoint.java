package dev.muscaw.monitor.weather.api;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface OpenWeatherMapApiEndpoint {

  @GET("/data/2.5/weather")
  @Headers("Accept: application/json")
  CompletableFuture<OpenWeatherMapWeather> getCurrentWeather(
      @Query("appid") String token,
      @Query("lat") double lat,
      @Query("lon") double lon,
      @Query("units") String units);

  @GET("/geo/1.0/reverse")
  @Headers("Accept: application/json")
  CompletableFuture<List<ReverseLocationLookup>> locationNameLookup(
      @Query("appid") String token,
      @Query("lat") double lat,
      @Query("lon") double lon,
      @Query("limit") int limit);
}
