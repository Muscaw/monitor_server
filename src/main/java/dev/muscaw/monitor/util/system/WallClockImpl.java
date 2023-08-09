package dev.muscaw.monitor.util.system;

import dev.muscaw.monitor.util.domain.WallClock;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class WallClockImpl implements WallClock {
    @Override
    public Date now() {
        return new Date(System.currentTimeMillis());
    }
}
