package dev.muscaw.monitor.image.ext;

import dev.muscaw.monitor.image.domain.DeviceConfiguration;
import dev.muscaw.monitor.image.domain.PtDepartureOption;
import dev.muscaw.monitor.image.domain.Renderable;
import dev.muscaw.monitor.transport.domain.Departure;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PtDepartureImage implements Renderable {

  public static final int MAX_ITEMS_PER_DESTINATION = 2;
  public static final int MARGIN_PX = 20;

  private final SVGImage image;

  PtDepartureImage(SVGImage image, List<Departure> departureList) {
    this.image = image;

    this.image.drawStringAt(
        MARGIN_PX,
        MARGIN_PX,
        "From " + departureList.stream().findFirst().map(Departure::stopName).orElse(""));

    int remainingSpace =
        (int) (this.image.getImageHeight() - 2 * MARGIN_PX - this.image.getStringHeight() * 1.5);
    int remainingLines = remainingSpace / this.image.getStringHeight();

    departureList = maxDeparturesPerDestination(departureList, MAX_ITEMS_PER_DESTINATION);
    departureList = departureList.subList(0, Math.min(remainingLines, departureList.size()));

    this.image.drawStringLines(
        MARGIN_PX,
        (int) (MARGIN_PX + this.image.getStringHeight() * 1.5),
        departureList.stream().map(this::fromDeparture).toList());
  }

  private List<Departure> maxDeparturesPerDestination(List<Departure> departureList, int maxCount) {
    var destinationDepartureMap = new HashMap<String, List<Departure>>();
    return departureList.stream()
        .filter(
            departure -> {
              var list =
                  destinationDepartureMap.getOrDefault(
                      departure.destinationName(), new ArrayList<>());
              list.add(departure);
              destinationDepartureMap.put(departure.destinationName(), list);
              return list.size() <= maxCount;
            })
        .toList();
  }

  public static PtDepartureImage newImage(
      DeviceConfiguration deviceConfig, PtDepartureOption departureOption, FontGroup font) {
    SVGImage image = SVGImage.newImage(deviceConfig.width(), deviceConfig.height(), font);
    return new PtDepartureImage(image, departureOption.nextDepartures());
  }

  private String durationToString(Duration duration) {
    if (duration.isZero()) {
      return "";
    }
    if (duration.isNegative()) {
      return "" + duration.toMinutes();
    } else {
      return "+" + duration.toMinutes();
    }
  }

  private String fromDeparture(Departure departure) {
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
    String durationPart = durationToString(departure.delay());
    return departure.destinationName()
        + " - "
        + formatter.format(departure.scheduledDepartureDate())
        + durationPart;
  }

  @Override
  public String getSVGDocument() {
    return image.getSVGDocument();
  }

  @Override
  public byte[] getPNGImage() {
    return image.getPNGImage();
  }

  @Override
  public byte[] asBitmap() {
    return image.asBitmap();
  }
}
