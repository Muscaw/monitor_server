package dev.muscaw.monitor.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.muscaw.monitor.app.domain.Page;
import dev.muscaw.monitor.image.domain.DeviceConfiguration;
import dev.muscaw.monitor.image.domain.ImageGeneratorService;
import dev.muscaw.monitor.image.domain.Renderable;
import dev.muscaw.monitor.image.domain.WeatherOption;
import dev.muscaw.monitor.util.domain.LatLon;
import dev.muscaw.monitor.weather.domain.Weather;
import dev.muscaw.monitor.weather.domain.WeatherService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MonitorAppServiceImplTest {
  private WeatherService mockWeatherService;
  private ImageGeneratorService mockImageGeneratorService;
  private LatLon weatherLocation;

  private MonitorAppServiceImpl monitorAppService;

  @BeforeEach
  public void setUp() {
    this.mockWeatherService = mock(WeatherService.class);
    this.mockImageGeneratorService = mock(ImageGeneratorService.class);
    this.weatherLocation = new LatLon(46.54f, 6.99f);

    this.monitorAppService =
        new MonitorAppServiceImpl(mockWeatherService, mockImageGeneratorService, weatherLocation);
  }

  @Test
  public void getName_isExpectedName() {
    assertThat(monitorAppService.getName()).isEqualTo("monitor");
  }

  @Test
  public void getPage_success() {
    var deviceConfig = new DeviceConfiguration(200, 100);
    var mockWeather = mock(Weather.class);
    when(mockWeatherService.getCurrentWeather(weatherLocation)).thenReturn(mockWeather);
    var mockRenderable = mock(Renderable.class);
    when(mockImageGeneratorService.generateImage(new WeatherOption(deviceConfig, mockWeather)))
        .thenReturn(mockRenderable);

    Optional<Page> page = monitorAppService.getPage(0, deviceConfig);

    assertThat(page).isEqualTo(Optional.of(new Page(0, 0, mockRenderable)));
  }
}
