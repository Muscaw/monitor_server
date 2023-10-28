package dev.muscaw.monitor.app.domain;

import dev.muscaw.monitor.image.domain.DeviceConfiguration;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public interface AppService {
  default Optional<Page> getPage(PageNumber pageNumber, DeviceConfiguration deviceConfiguration) {
    var generator = getPageGenerator();
    if (pageNumber.page() < 0 || pageNumber.page() >= generator.size()) {
      return Optional.empty();
    }

    var pager = new Pager(pageNumber, generator.size());
    return Optional.of(generator.get(pageNumber.page()).apply(pager, deviceConfiguration));
  }

  List<BiFunction<Pager, DeviceConfiguration, Page>> getPageGenerator();

  String getName();
}
