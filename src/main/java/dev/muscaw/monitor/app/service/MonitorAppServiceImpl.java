package dev.muscaw.monitor.app.service;

import dev.muscaw.monitor.app.domain.AppService;
import dev.muscaw.monitor.app.domain.Page;
import dev.muscaw.monitor.image.domain.DeviceConfiguration;
import dev.muscaw.monitor.image.domain.ImageGeneratorService;
import dev.muscaw.monitor.image.domain.WeatherOption;
import dev.muscaw.monitor.util.domain.LatLon;
import dev.muscaw.monitor.weather.domain.WeatherService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MonitorAppServiceImpl implements AppService {

  public final String NAME = "monitor";

  private final WeatherService weatherService;
  private final ImageGeneratorService imageGeneratorService;

  private final LatLon weatherLocation;

  @Autowired
  public MonitorAppServiceImpl(
      WeatherService weatherService,
      ImageGeneratorService imageGeneratorService,
      @Qualifier("weatherLocation") LatLon weatherLocation) {
    this.weatherService = weatherService;
    this.imageGeneratorService = imageGeneratorService;
    this.weatherLocation = weatherLocation;
  }

  @Override
  public Optional<Page> getPage(int pageNumber, DeviceConfiguration deviceConfiguration) {
    var weather = weatherService.getCurrentWeather(weatherLocation);
    return Optional.of(
        new Page(
            pageNumber,
            nextPage(pageNumber),
            imageGeneratorService.generateImage(new WeatherOption(deviceConfiguration, weather))));
  }

  @Override
  public String getName() {
    return NAME;
  }

  private int nextPage(int currentPage) {
    return 0;
  }
}
