package dev.muscaw.monitor.app.domain;

public interface AppSelector {
  AppService selectApp(String name);
}
