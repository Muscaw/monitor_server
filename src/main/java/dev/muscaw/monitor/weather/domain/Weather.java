package dev.muscaw.monitor.weather.domain;

import dev.muscaw.monitor.util.domain.LatLon;

public record Weather(
    String locationName,
    LatLon geoLocation,
    Temperature temperature,
    RelativeHumidity humidity,
    UV uvLevel,
    Wind wind,
    Precipitation precipitation) {}
