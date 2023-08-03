package dev.muscaw.monitor.image.rest;

import dev.muscaw.monitor.app.domain.AppSelector;
import dev.muscaw.monitor.app.domain.Page;

import java.net.URI;
import java.util.Optional;

import dev.muscaw.monitor.svg.ext.WeatherSVGImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
            @RequestParam(value = "width") int width,
            @RequestParam(value = "height") int height,
            @RequestParam(value = "outputType", required = false, defaultValue = "svg") String outputType,
            @PathVariable(required = true) String appName,
            @PathVariable(required = false) Optional<Integer> pageNumber) {

        var app = appSelector.selectApp(appName);

        int selectedPage = pageNumber.orElse(0);
        Page page = app.getPage(selectedPage);

        var builder = ServletUriComponentsBuilder.fromCurrentRequestUri();

        var svgImage = WeatherSVGImage.newImage(width, height);

        var responseBuilder = ResponseEntity.ok()
                .header("Link", "<" + getNextUri(builder, appName, width, height, page).toString() + ">");

        return switch (outputType) {
            case "svg" ->
                    responseBuilder.header("Content-Type", "image/svg+xml").body(new ByteArrayResource(svgImage.getSVGDocument().getBytes()));
            case "png" ->
                    responseBuilder.header("Content-Type", "image/png").body(new ByteArrayResource(svgImage.getPng()));
            default -> responseBuilder.header("Content-Type", "text/plain").body(new ByteArrayResource("".getBytes()));
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
