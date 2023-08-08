package dev.muscaw.monitor.image.ext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import dev.muscaw.monitor.image.domain.DeviceConfiguration;
import dev.muscaw.monitor.image.domain.Renderable;
import dev.muscaw.monitor.image.domain.WeatherOption;
import dev.muscaw.monitor.util.domain.LatLon;
import dev.muscaw.monitor.weather.domain.*;
import java.awt.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ImageGeneratorVisitorImplTest {

  private ImageGeneratorVisitorImpl imageGeneratorVisitor;

  @BeforeEach
  public void setUp() {
    Font font = new Font(Font.SERIF, Font.PLAIN, 10);
    GraphicsEnvironment ge = mock(GraphicsEnvironment.class);
    imageGeneratorVisitor = new ImageGeneratorVisitorImpl(new FontGroup(font, ge));
  }

  @Test
  public void renderWeather_success() {
    DeviceConfiguration configuration = new DeviceConfiguration(200, 100);
    Weather weather =
        new Weather(
            "Home",
            new LatLon(50, 10),
            new Temperature(20),
            new RelativeHumidity(10),
            new UV(2),
            new Wind(20, 10, new CardinalDirection("NWN", 180)),
            new Precipitation(0));
    WeatherOption option = new WeatherOption(configuration, weather);
    Renderable output = imageGeneratorVisitor.renderWeather(option);

    assertThat(output).isInstanceOf(WeatherSVGImage.class);
  }
}
