package dev.muscaw.monitor.transport.domain;

import dev.muscaw.monitor.util.domain.LatLon;

public interface PublicTransportNextDeparturesService {

    Departure getNextDepartureForLocation(LatLon location);
}
