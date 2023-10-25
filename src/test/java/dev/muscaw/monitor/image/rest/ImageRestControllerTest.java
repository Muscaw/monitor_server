package dev.muscaw.monitor.image.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.muscaw.monitor.app.domain.AppSelector;
import dev.muscaw.monitor.app.domain.AppService;
import dev.muscaw.monitor.app.domain.Page;
import dev.muscaw.monitor.image.domain.DeviceConfiguration;
import dev.muscaw.monitor.image.domain.Renderable;
import java.net.URI;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@WebMvcTest(ImageRestController.class)
public class ImageRestControllerTest {

  @Autowired ImageRestController imageRestController;

  @MockBean AppSelector mockAppSelector;
  @Autowired private MockMvc mockMvc;

  @Test
  public void getNextUri_defaultPath() {
    UriComponentsBuilder builder = ServletUriComponentsBuilder.newInstance();
    builder =
        builder
            .scheme("https")
            .host("some-host")
            .path("/monitor/images")
            .queryParam("width", 100)
            .queryParam("height", 120);
    URI result =
        this.imageRestController.getNextUri(
            builder, "monitor", 150, 100, new Page(0, 1, mock(Renderable.class)));
    assertEquals("https://some-host/monitor/images/1?width=150&height=100", result.toString());
  }

  @Test
  public void getNextUri_pathWithPageNumber() {
    UriComponentsBuilder builder = ServletUriComponentsBuilder.newInstance();
    builder =
        builder
            .scheme("https")
            .host("some-host")
            .path("/monitor/images/5")
            .queryParam("width", 100)
            .queryParam("height", 120);
    URI result =
        this.imageRestController.getNextUri(
            builder, "monitor", 150, 100, new Page(5, 0, mock(Renderable.class)));
    assertEquals("https://some-host/monitor/images/0?width=150&height=100", result.toString());
  }

  @Test
  public void getImage_returnDefaultOutputType() throws Exception {
    var mockAppService = mock(AppService.class);
    var mockRenderable = mock(Renderable.class);

    when(mockRenderable.asBitmap()).thenReturn(new byte[] {0, 1, 2, 3});
    when(mockAppService.getPage(0, new DeviceConfiguration(150, 100)))
        .thenReturn(Optional.of(new Page(0, 1, mockRenderable)));

    when(mockAppSelector.selectApp("monitor")).thenReturn(mockAppService);
    this.mockMvc
        .perform(get("/monitor/images?width=150&height=100"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            header().string("Link", "<http://localhost/monitor/images/1?width=150&height=100>"))
        .andExpect(content().contentType("application/octet-stream"))
        .andExpect(content().bytes(new byte[] {0, 1, 2, 3}));
  }

  @Test
  public void getImage_returnSvgOutputType() throws Exception {
    var mockAppService = mock(AppService.class);
    var mockRenderable = mock(Renderable.class);

    when(mockRenderable.getSVGDocument()).thenReturn("<svg></svg>");
    when(mockAppService.getPage(0, new DeviceConfiguration(150, 100)))
        .thenReturn(Optional.of(new Page(0, 1, mockRenderable)));

    when(mockAppSelector.selectApp("monitor")).thenReturn(mockAppService);
    this.mockMvc
        .perform(get("/monitor/images?width=150&height=100&outputType=svg"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            header().string("Link", "<http://localhost/monitor/images/1?width=150&height=100>"))
        .andExpect(content().contentType("image/svg+xml"))
        .andExpect(content().string("<svg></svg>"));
  }

  @Test
  public void getImage_returnPngOutputType() throws Exception {
    var mockAppService = mock(AppService.class);
    var mockRenderable = mock(Renderable.class);

    when(mockRenderable.getPNGImage()).thenReturn(new byte[] {0, 1, 2});
    when(mockAppService.getPage(0, new DeviceConfiguration(150, 100)))
        .thenReturn(Optional.of(new Page(0, 1, mockRenderable)));

    when(mockAppSelector.selectApp("monitor")).thenReturn(mockAppService);
    this.mockMvc
        .perform(get("/monitor/images?width=150&height=100&outputType=png"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            header().string("Link", "<http://localhost/monitor/images/1?width=150&height=100>"))
        .andExpect(content().contentType("image/png"))
        .andExpect(content().bytes(new byte[] {0, 1, 2}));
  }

  @Test
  public void getImage_pageNotFound() throws Exception {
    var mockAppService = mock(AppService.class);

    when(mockAppService.getPage(0, new DeviceConfiguration(150, 100))).thenReturn(Optional.empty());

    when(mockAppSelector.selectApp("monitor")).thenReturn(mockAppService);
    this.mockMvc
        .perform(get("/monitor/images?width=150&height=100&outputType=png"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }
}
