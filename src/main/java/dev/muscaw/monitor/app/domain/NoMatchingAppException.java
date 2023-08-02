package dev.muscaw.monitor.app.domain;

public class NoMatchingAppException extends RuntimeException {

  public NoMatchingAppException(String name) {
    super("Could not find app " + name);
  }
}
