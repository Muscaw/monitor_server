package dev.muscaw.monitor.app.service;

import dev.muscaw.monitor.app.domain.AppService;
import dev.muscaw.monitor.app.domain.Page;
import dev.muscaw.monitor.image.domain.DeviceConfiguration;
import dev.muscaw.monitor.image.ext.WeatherSVGImage;
import dev.muscaw.monitor.util.domain.LatLon;
import dev.muscaw.monitor.weather.domain.WeatherService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitorAppServiceImpl implements AppService {

  public final String NAME = "monitor";

  private final WeatherService weatherService;

  @Autowired
  public MonitorAppServiceImpl(WeatherService weatherService) {
    this.weatherService = weatherService;
  }

  @Override
  public Optional<Page> getPage(int pageNumber, DeviceConfiguration deviceConfiguration) {
    var weather = weatherService.getCurrentWeather(new LatLon(46.62f, 7.07f));
    return Optional.of(
        new Page(
            pageNumber,
            nextPage(pageNumber),
            WeatherSVGImage.newImage(deviceConfiguration, weather)));
  }

  @Override
  public String getName() {
    return NAME;
  }

  private int nextPage(int currentPage) {
    currentPage += 1;
    if (currentPage >= 5) {
      currentPage = 0;
    }
    return currentPage;
  }
}
