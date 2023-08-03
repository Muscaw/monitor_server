package dev.muscaw.monitor.app.service;

import dev.muscaw.monitor.app.domain.AppService;
import dev.muscaw.monitor.app.domain.Page;
import dev.muscaw.monitor.image.domain.DeviceConfiguration;
import dev.muscaw.monitor.image.ext.WeatherSVGImage;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MonitorAppServiceImpl implements AppService {

  public final String NAME = "monitor";

  @Override
  public Optional<Page> getPage(int pageNumber, DeviceConfiguration deviceConfiguration) {
    return Optional.of(
        new Page(
            pageNumber,
            nextPage(pageNumber),
            WeatherSVGImage.newImage(deviceConfiguration.width(), deviceConfiguration.height())));
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
