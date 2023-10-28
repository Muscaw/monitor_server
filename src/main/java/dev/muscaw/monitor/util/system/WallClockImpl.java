package dev.muscaw.monitor.util.system;

import dev.muscaw.monitor.util.domain.WallClock;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class WallClockImpl implements WallClock {
  @Override
  public Date now() {
    return new Date(System.currentTimeMillis());
  }
}
