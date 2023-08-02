package dev.muscaw.monitor.image.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import dev.muscaw.monitor.app.domain.AppSelector;
import dev.muscaw.monitor.app.domain.Page;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

public class ImageRestControllerTest {
  ImageRestController imageRestController;
  AppSelector mockAppSelector;

  @BeforeEach
  void setUp() {
    mockAppSelector = mock(AppSelector.class);

    imageRestController = new ImageRestController(mockAppSelector);
  }

  @Test
  public void testGetNextUri_defaultPath() {
    UriComponentsBuilder builder = ServletUriComponentsBuilder.newInstance();
    builder =
        builder
            .scheme("https")
            .host("some-host")
            .path("/monitor/images")
            .queryParam("width", 100)
            .queryParam("height", 120);
    URI result = this.imageRestController.getNextUri(builder, "monitor", 150, 100, new Page(0, 1));
    assertEquals("https://some-host/monitor/images/1?width=150&height=100", result.toString());
  }

  @Test
  public void testGetNextUri_pathWithPageNumber() {
    UriComponentsBuilder builder = ServletUriComponentsBuilder.newInstance();
    builder =
            builder
                    .scheme("https")
                    .host("some-host")
                    .path("/monitor/images/5")
                    .queryParam("width", 100)
                    .queryParam("height", 120);
    URI result = this.imageRestController.getNextUri(builder, "monitor", 150, 100, new Page(5, 0));
    assertEquals("https://some-host/monitor/images/0?width=150&height=100", result.toString());
  }
}
