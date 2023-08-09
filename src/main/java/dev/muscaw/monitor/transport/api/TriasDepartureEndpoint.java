package dev.muscaw.monitor.transport.api;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;

import java.util.concurrent.CompletableFuture;

public interface TriasDepartureEndpoint {

    @GET("/trias2020")
    @Headers("accept: application/xml")
    CompletableFuture<String> getTriasResult(@Body String request);
}
