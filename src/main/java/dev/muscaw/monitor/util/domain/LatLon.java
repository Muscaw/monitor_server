package dev.muscaw.monitor.util.domain;

import java.text.DecimalFormat;

public record LatLon(float lat, float lon) {
  public String as2DecLatLonString() {
    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    return df.format(lat) + "," + df.format(lon);
  }
}
