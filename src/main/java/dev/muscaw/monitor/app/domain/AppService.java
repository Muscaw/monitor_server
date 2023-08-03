package dev.muscaw.monitor.app.domain;

import dev.muscaw.monitor.image.domain.DeviceConfiguration;
import java.util.Optional;

public interface AppService {
  Optional<Page> getPage(int pageNumber, DeviceConfiguration deviceConfiguration);

  String getName();
}
