package dev.muscaw.monitor.weather.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ReverseLocationLookup(String name) {}
