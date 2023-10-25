package dev.muscaw.monitor.image.rest;

import dev.muscaw.monitor.app.domain.AppSelector;
import dev.muscaw.monitor.app.domain.Page;
import dev.muscaw.monitor.image.domain.DeviceConfiguration;
import java.net.URI;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class ImageRestController {

  private AppSelector appSelector;

  @Autowired
  public ImageRestController(AppSelector appSelector) {
    this.appSelector = appSelector;
  }

  @GetMapping(value = {"/{appName}/images", "/{appName}/images/{pageNumber}"})
  public ResponseEntity<ByteArrayResource> getImage(
      @RequestParam("width") int width,
      @RequestParam("height") int height,
      @RequestParam(value = "outputType", required = false, defaultValue = "text")
          String outputType,
      @PathVariable String appName,
      @PathVariable(required = false) Optional<Integer> pageNumber) {

    var app = appSelector.selectApp(appName);
    var deviceConfig = new DeviceConfiguration(width, height);

    int selectedPage = pageNumber.orElse(0);
    Optional<Page> optPage = app.getPage(selectedPage, deviceConfig);
    if (optPage.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    Page page = optPage.get();

    var builder = ServletUriComponentsBuilder.fromCurrentRequestUri();

    var image = page.renderable();

    var responseBuilder =
        ResponseEntity.ok()
            .header(
                "Link", "<" + getNextUri(builder, appName, width, height, page).toString() + ">");

    return switch (outputType) {
      case "svg" -> responseBuilder
          .header("Content-Type", "image/svg+xml")
          .body(new ByteArrayResource(image.getSVGDocument().getBytes()));
      case "png" -> responseBuilder
          .header("Content-Type", "image/png")
          .body(new ByteArrayResource(image.getPNGImage()));
      default -> responseBuilder
          .header("Content-Type", "application/octet-stream")
          .body(new ByteArrayResource(image.asBitmap()));
    };
  }

  URI getNextUri(
      UriComponentsBuilder builder, String appName, int width, int height, Page currentPage) {
    return builder
        .replacePath(String.format("/%s/images/%d", appName, currentPage.nextPage()))
        .replaceQueryParam("width", width)
        .replaceQueryParam("height", height)
        .build()
        .toUri();
  }
}
