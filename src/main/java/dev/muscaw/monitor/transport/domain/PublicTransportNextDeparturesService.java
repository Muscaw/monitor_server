package dev.muscaw.monitor.transport.domain;

import dev.muscaw.monitor.util.domain.LatLon;
import java.util.List;

public interface PublicTransportNextDeparturesService {

  List<Departure> getNextDepartureForLocation(LatLon location);
}
