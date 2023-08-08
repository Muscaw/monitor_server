package dev.muscaw.monitor.image.ext;

import static org.assertj.core.api.Assertions.assertThat;

import dev.muscaw.monitor.image.domain.DeviceConfiguration;
import dev.muscaw.monitor.util.domain.LatLon;
import dev.muscaw.monitor.weather.domain.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WeatherSVGImageTest {
  private DeviceConfiguration configuration;
  private Weather weather;
  private FontGroup fontGroup;

  @BeforeEach
  public void setUp() throws IOException, FontFormatException {
    this.configuration = new DeviceConfiguration(296, 128);
    this.weather =
        new Weather(
            "Home",
            new LatLon(50, 10),
            new Temperature(23),
            new RelativeHumidity(40),
            new UV(2),
            new Wind(10, 20.5f, new CardinalDirection("W", 270)),
            new Precipitation(1));
    var font =
        Font.createFont(
            Font.TRUETYPE_FONT,
            Paths.get("src/main/resources/font/pixeloid/PixeloidSans-mLxMm.ttf").toFile());

    this.fontGroup = new FontGroup(font, GraphicsEnvironment.getLocalGraphicsEnvironment());
  }

  @Test
  public void newImage_generatesExpectedPng() throws IOException {
    byte[] expectedPngContent =
        Files.readAllBytes(Paths.get("src/test/resources/weather/weather.png"));
    var image = WeatherSVGImage.newImage(configuration, weather, fontGroup);

    byte[] result = image.getPNGImage();

    assertThat(result).isEqualTo(expectedPngContent);
  }
}
