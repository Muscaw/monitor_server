package dev.muscaw.monitor.transport.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.muscaw.monitor.transport.domain.Departure;
import java.time.Duration;
import java.util.Date;
import java.util.List;

// Response as defined in https://transport.opendata.ch/docs.html
@JsonIgnoreProperties(ignoreUnknown = true)
public record StationBoardResponse(List<Journey> stationboard) {
  public static final String FORMAT_WITH_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ssZ";

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Journey(Stop stop, String name, String to) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Stop(
      Station station,
      @JsonFormat(pattern = FORMAT_WITH_TIMEZONE) Date arrival,
      @JsonFormat(pattern = FORMAT_WITH_TIMEZONE) Date departure,
      Prognosis prognosis) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Station(String name) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Prognosis(
      String platform,
      @JsonFormat(pattern = FORMAT_WITH_TIMEZONE) Date departure,
      @JsonFormat(pattern = FORMAT_WITH_TIMEZONE) Date arrival) {}

  public List<Departure> toDomain() {
    return stationboard().stream()
        .map(
            x -> {
              var delay =
                  x.stop().prognosis().departure() != null
                      ? Duration.between(
                          x.stop().departure().toInstant(),
                          x.stop().prognosis().departure().toInstant())
                      : Duration.ofMinutes(0);
              return new Departure(x.stop().station().name(), x.to(), x.stop().departure(), delay);
            })
        .toList();
  }
}
