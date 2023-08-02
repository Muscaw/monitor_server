package dev.muscaw.monitor.app.domain;

public interface AppService {
  Page getPage(int pageNumber);

  String getName();
}
