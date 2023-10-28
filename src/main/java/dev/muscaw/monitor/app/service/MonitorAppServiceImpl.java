package dev.muscaw.monitor.app.service;

import dev.muscaw.monitor.app.domain.AppService;
import dev.muscaw.monitor.app.domain.Page;
import dev.muscaw.monitor.app.domain.Pager;
import dev.muscaw.monitor.image.domain.DeviceConfiguration;
import dev.muscaw.monitor.image.domain.ImageGeneratorService;
import dev.muscaw.monitor.image.domain.PtDepartureOption;
import dev.muscaw.monitor.image.domain.WeatherOption;
import dev.muscaw.monitor.transport.domain.PublicTransportNextDeparturesService;
import dev.muscaw.monitor.util.domain.LatLon;
import dev.muscaw.monitor.weather.domain.WeatherService;
import java.util.List;
import java.util.function.BiFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MonitorAppServiceImpl implements AppService {

  public final String NAME = "monitor";

  private final WeatherService weatherService;
  private final PublicTransportNextDeparturesService nextDeparturesService;
  private final ImageGeneratorService imageGeneratorService;

  private final LatLon weatherLocation;
  private final LatLon ptStationLocation;
  private final List<BiFunction<Pager, DeviceConfiguration, Page>> pageGenerators;

  @Autowired
  public MonitorAppServiceImpl(
      WeatherService weatherService,
      ImageGeneratorService imageGeneratorService,
      PublicTransportNextDeparturesService nextDeparturesService,
      @Qualifier("weatherLocation") LatLon weatherLocation,
      @Qualifier("ptStationLocation") LatLon ptStationLocation) {
    this.weatherService = weatherService;
    this.imageGeneratorService = imageGeneratorService;
    this.nextDeparturesService = nextDeparturesService;
    this.weatherLocation = weatherLocation;
    this.ptStationLocation = ptStationLocation;
    this.pageGenerators = List.of(this::generateWeatherPage, this::generatePublicTransportPage);
  }

  private Page generateWeatherPage(Pager pager, DeviceConfiguration deviceConfiguration) {
    var weather = weatherService.getCurrentWeather(weatherLocation);
    return new Page(
        pager,
        imageGeneratorService.generateImage(new WeatherOption(deviceConfiguration, weather)));
  }

  private Page generatePublicTransportPage(Pager pager, DeviceConfiguration deviceConfiguration) {
    var departures = nextDeparturesService.getNextDepartureForLocation(ptStationLocation);
    return new Page(
        pager,
        imageGeneratorService.generateImage(
            new PtDepartureOption(deviceConfiguration, departures)));
  }

  @Override
  public List<BiFunction<Pager, DeviceConfiguration, Page>> getPageGenerator() {
    return pageGenerators;
  }

  @Override
  public String getName() {
    return NAME;
  }
}
