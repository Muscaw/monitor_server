package dev.muscaw.monitor.app.service;

import dev.muscaw.monitor.app.domain.AppService;
import dev.muscaw.monitor.app.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class MonitorAppServiceImpl implements AppService {

  public final String NAME = "monitor";

  @Override
  public Page getPage(int pageNumber) {
    return new Page(pageNumber, (pageNumber + 1) % 5);
  }

  @Override
  public String getName() {
    return NAME;
  }
}
