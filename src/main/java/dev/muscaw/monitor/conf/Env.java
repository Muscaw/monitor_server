package dev.muscaw.monitor.conf;

import java.util.Optional;

public class Env {

  public static Optional<String> getEnv(String key) {
    return Optional.ofNullable(System.getenv(key));
  }
}
