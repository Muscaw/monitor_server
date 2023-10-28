package dev.muscaw.monitor.transport.api;

import java.util.concurrent.CompletableFuture;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface TransportOpenDataEndpoint {

  @GET("/v1/stationboard")
  @Headers("accept: application/json")
  CompletableFuture<StationBoardResponse> getStationBoard(
      @Query("id") String stationId, @Query("limit") int limit);

  @GET("/v1/locations")
  @Headers("accept: application/json")
  CompletableFuture<LocationsResponse> getLocations(
      @Query("x") double latitude, @Query("y") double longitude, @Query("type") String type);
}
