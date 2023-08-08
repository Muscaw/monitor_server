package dev.muscaw.monitor.image.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class RenderTypeTest {

  private static Stream<Arguments> stringToEnum() {
    return Stream.of(
        Arguments.of("bw", RenderType.BW),
        Arguments.of("BW", RenderType.BW),
        Arguments.of("grayscale", RenderType.GRAYSCALE),
        Arguments.of("GRAYSCALE", RenderType.GRAYSCALE));
  }

  @ParameterizedTest
  @MethodSource("stringToEnum")
  public void testFromString_success(String value, RenderType renderType) {
    var result = RenderType.fromString(value, null);
    assertThat(result).isEqualTo(renderType);
  }

  @Test
  public void testFromString_returns_default() {
    var result = RenderType.fromString("unknown-type", RenderType.BW);
    assertThat(result).isEqualTo(RenderType.BW);
  }
}
