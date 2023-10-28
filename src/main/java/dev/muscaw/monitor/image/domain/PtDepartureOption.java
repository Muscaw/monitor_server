package dev.muscaw.monitor.image.domain;

import dev.muscaw.monitor.transport.domain.Departure;
import java.util.List;

public record PtDepartureOption(
    DeviceConfiguration deviceConfiguration, List<Departure> nextDepartures)
    implements RenderableOption {
  @Override
  public Renderable accept(ImageGeneratorVisitor visitor) {
    return visitor.renderDepartures(this);
  }
}
