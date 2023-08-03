package dev.muscaw.monitor.app.service;

import dev.muscaw.monitor.app.domain.AppSelector;
import dev.muscaw.monitor.app.domain.AppService;
import dev.muscaw.monitor.app.domain.NoMatchingAppException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppSelectorImpl implements AppSelector {

  private List<AppService> services;

  @Autowired
  public void setServices(List<AppService> services) {
    var distinctNamesCount = services.stream().map(AppService::getName).distinct().count();
    if (distinctNamesCount != services.size()) {
      Set<String> uniqueItems = new HashSet<>();
      String duplicates =
          services.stream()
              .map(AppService::getName)
              .filter(name -> !uniqueItems.add(name))
              .collect(Collectors.joining(", "));
      throw new RuntimeException("Multiple AppService have the same name: " + duplicates);
    }
    this.services = services;
  }

  @Override
  public AppService selectApp(String name) {
    return services.stream()
        .filter(i -> i.getName().equals(name))
        .findFirst()
        .orElseThrow(() -> new NoMatchingAppException(name));
  }
}
