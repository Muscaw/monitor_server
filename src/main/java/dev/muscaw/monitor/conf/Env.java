package dev.muscaw.monitor.conf;

import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class Env {

  public Optional<String> getEnv(String key) {
    return Optional.ofNullable(System.getenv(key));
  }
}
