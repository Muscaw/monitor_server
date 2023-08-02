package dev.muscaw.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class MonitorApplication {

  public static void main(String[] args) {
    SpringApplication.run(MonitorApplication.class, args);
  }
}
