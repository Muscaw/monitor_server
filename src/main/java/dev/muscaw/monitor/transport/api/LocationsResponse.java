package dev.muscaw.monitor.transport.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.muscaw.monitor.util.domain.LatLon;
import java.util.List;

// Response as defined in https://transport.opendata.ch/docs.html
@JsonIgnoreProperties(ignoreUnknown = true)
public record LocationsResponse(List<Station> stations) {
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Station(String id, String name, Coordinates coordinate) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Coordinates(@JsonProperty("x") double lat, @JsonProperty("y") double lon) {
    public LatLon toDomain() {
      return new LatLon(this.lat, this.lon);
    }
  }
}
