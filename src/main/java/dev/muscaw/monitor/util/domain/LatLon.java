package dev.muscaw.monitor.util.domain;

import java.text.DecimalFormat;

public record LatLon(double lat, double lon) {
  public String as2DecLatLonString() {
    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    return df.format(lat) + "," + df.format(lon);
  }

  public double distanceInMeters(LatLon other) {
    long earthRadius = 6371;
    double dLat = Math.toRadians(other.lat() - this.lat());
    double dLon = Math.toRadians(other.lon() - this.lon());

    double startLat = Math.toRadians(this.lat());
    double endLat = Math.toRadians(other.lat());

    double a =
        Math.pow(Math.sin(dLat / 2), 2)
            + Math.cos(startLat) * Math.cos(endLat) * Math.pow(Math.sin(dLon / 2), 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return earthRadius * c;
  }
}
