package dev.muscaw.monitor.image.domain;

public interface ImageGeneratorVisitor {

  Renderable renderWeather(WeatherOption option);

  Renderable renderDepartures(PtDepartureOption option);
}
