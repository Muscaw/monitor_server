package dev.muscaw.monitor.conf;

import dev.muscaw.monitor.transport.api.PublicTransportNextDeparturesServiceImpl;
import dev.muscaw.monitor.transport.domain.PublicTransportNextDeparturesService;
import dev.muscaw.monitor.util.domain.LatLon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransportConfiguration {
  @Bean
  public PublicTransportNextDeparturesService createNextDepartureService(Env env) {
    String baseUrl =
        env.getEnv("SWISS_TRANSPORT_OPENDATA_BASEURL").orElse("https://transport.opendata.ch");
    return new PublicTransportNextDeparturesServiceImpl(baseUrl);
  }

  @Bean(name = "ptStationLocation")
  public LatLon getPtStationLocation(Env env) {
    String latLon = env.getEnv("PT_STATION_LAT_LON").orElseThrow();
    String[] values = latLon.split(",");
    return new LatLon(Double.parseDouble(values[0]), Double.parseDouble(values[1]));
  }
}
