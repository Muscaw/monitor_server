package dev.muscaw.monitor.transport.domain;

import java.time.Duration;
import java.util.Date;

public record Departure(
    String stopName, String destinationName, Date scheduledDepartureDate, Duration delay) {}
