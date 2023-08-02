package dev.muscaw.monitor.app.service;

import dev.muscaw.monitor.app.domain.AppSelector;
import dev.muscaw.monitor.app.domain.AppService;
import dev.muscaw.monitor.app.domain.NoMatchingAppException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppSelectorImpl implements AppSelector {

  @Autowired private List<AppService> services;

  @Override
  public AppService selectApp(String name) {
    return services.stream()
        .filter(i -> i.getName().equals(name))
        .findFirst()
        .orElseThrow(() -> new NoMatchingAppException(name));
  }
}
