package dev.muscaw.monitor.transport.api;

import static dev.muscaw.monitor.util.http.Retryable.retry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.muscaw.monitor.transport.domain.Departure;
import dev.muscaw.monitor.transport.domain.PublicTransportNextDeparturesService;
import dev.muscaw.monitor.util.domain.LatLon;
import java.util.Comparator;
import java.util.List;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class PublicTransportNextDeparturesServiceImpl
    implements PublicTransportNextDeparturesService {

  public static final int STOPBOARD_LIMIT = 10;
  public static final String LOCATION_TYPE = "station";
  public static final int MAX_RETRIES = 3;
  private final TransportOpenDataEndpoint client;

  public PublicTransportNextDeparturesServiceImpl(String baseUrl) {

    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                JacksonConverterFactory.create(
                    new ObjectMapper().registerModule(new JavaTimeModule())))
            .build();

    client = retrofit.create(TransportOpenDataEndpoint.class);
  }

  @Override
  public List<Departure> getNextDepartureForLocation(LatLon location) {

    LocationsResponse locationsResponse =
        retry(
            () -> client.getLocations(location.lat(), location.lon(), LOCATION_TYPE), MAX_RETRIES);
    String stationId =
        locationsResponse.stations().stream()
            .filter(x -> x.id() != null)
            .min(Comparator.comparing(o -> o.coordinate().toDomain().distanceInMeters(location)))
            .map(LocationsResponse.Station::id)
            .orElseThrow();

    StationBoardResponse stationBoardResponse =
        retry(() -> client.getStationBoard(stationId, STOPBOARD_LIMIT), MAX_RETRIES);
    return stationBoardResponse.toDomain();
  }
}
