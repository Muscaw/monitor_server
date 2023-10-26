package dev.muscaw.monitor.weather.domain;

public record CardinalDirection(String direction, int rotation) {

  private static String directionFromAngle(int rotation) {
    // We add 22 which is equivalent to the portion between NNO and N so that we offset the values
    // to start at 0
    // We use modulo 360 to ensure we are still using the 360 degree (just rotated to the west)
    // We divide by 45 to know which quadrant we are in
    int quart = (rotation = (rotation + 22) % 360) / 45;

    return switch (quart) {
      case 0 -> "N";
      case 1 -> "NE";
      case 2 -> "E";
      case 3 -> "SE";
      case 4 -> "S";
      case 5 -> "SW";
      case 6 -> "W";
      case 7 -> "NW";
        // We cannot have a value higher than 7
      default -> throw new IllegalStateException("Unexpected value: " + quart);
    };
  }

  public static CardinalDirection fromAngle(int rotation) {
    return new CardinalDirection(directionFromAngle(rotation), rotation);
  }
}
