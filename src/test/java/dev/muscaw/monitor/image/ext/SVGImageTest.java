package dev.muscaw.monitor.image.ext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import dev.muscaw.monitor.image.domain.ImageSerializationException;
import dev.muscaw.monitor.image.domain.RenderType;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.transcoder.TranscoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SVGImageTest {

  public static final int SCREEN_WIDTH = 296;
  public static final int SCREEN_HEIGHT = 128;

  private SVGImage svgImage;
  private SVGGraphics2D mockG2;
  private FontMetrics mockFontMetrics;
  private FontGroup font;
  private GraphicsEnvironment mockGe;

  @BeforeEach
  public void setUp() {
    mockG2 = mock(SVGGraphics2D.class);
    svgImage = new SVGImage(mockG2, SCREEN_WIDTH, SCREEN_HEIGHT);
    mockFontMetrics = mock(FontMetrics.class);
    when(mockG2.getFontMetrics()).thenReturn(mockFontMetrics);
    mockGe = mock(GraphicsEnvironment.class);
    font = new FontGroup(new Font(Font.SERIF, Font.BOLD, 10), mockGe);
  }

  @Test
  public void newImage_success() {
    SVGImage image = SVGImage.newImage(200, 100, font);

    assertThat(image).isNotNull();
  }

  @Test
  public void getStringHeight_success() {
    when(mockFontMetrics.getHeight()).thenReturn(10);

    int result = svgImage.getStringHeight();
    assertEquals(10, result);
  }

  @Test
  public void getStringWidth_success() {
    when(mockFontMetrics.stringWidth("some-string")).thenReturn(10);

    int result = svgImage.getStringWidth("some-string");
    assertEquals(10, result);
  }

  @Test
  public void drawStringCentered_success() {
    when(mockFontMetrics.getHeight()).thenReturn(10);
    when(mockFontMetrics.stringWidth("centered-string")).thenReturn(20);

    svgImage.drawStringCentered(10, 5, "centered-string");

    verify(mockG2).drawString("centered-string", 0, 10);
  }

  @Test
  public void drawStringAt_success() {
    when(mockFontMetrics.getHeight()).thenReturn(10);

    svgImage.drawStringAt(10, 20, "some-string");

    verify(mockG2).drawString("some-string", 10, 25);
  }

  @Test
  public void drawLines_success() {
    when(mockFontMetrics.getHeight()).thenReturn(10);

    svgImage.drawLines(0, 0, List.of("string1", "string2", "string3"));

    verify(mockG2).drawString("string1", 0, 5);
    verify(mockG2).drawString("string2", 0, 15);
    verify(mockG2).drawString("string3", 0, 25);
  }

  @Test
  public void drawRect_success() {
    svgImage.drawRect(0, 0, 100, 200);

    verify(mockG2).setColor(Color.BLACK);
    verify(mockG2).drawRect(0, 0, 100, 200);
  }

  @Test
  public void asSerial_bw_success() throws Exception {
    String svgContent =
        String.join("\n", Files.readAllLines(Paths.get("src/test/resources/svg/coffee.svg")));
    byte[] expectedBinaryContent =
        Files.readAllBytes(Paths.get("src/test/resources/svg/coffee_bw.bin"));

    doAnswer(
        i -> {
          i.getArgument(0, StringWriter.class).write(svgContent);
          return null;
        })
        .when(mockG2)
        .stream(any(StringWriter.class));
    byte[] result = svgImage.asSerial(RenderType.BW);

    assertThat(result).isEqualTo(expectedBinaryContent);
  }

  @Test
  public void asSerial_grayscale_success() throws Exception {
    String svgContent =
        String.join("\n", Files.readAllLines(Paths.get("src/test/resources/svg/coffee.svg")));
    byte[] expectedBinaryContent =
        Files.readAllBytes(Paths.get("src/test/resources/svg/coffee_grayscale.bin"));

    doAnswer(
        i -> {
          i.getArgument(0, StringWriter.class).write(svgContent);
          return null;
        })
        .when(mockG2)
        .stream(any(StringWriter.class));
    byte[] result = svgImage.asSerial(RenderType.GRAYSCALE);

    assertThat(result).isEqualTo(expectedBinaryContent);
  }

  @Test
  public void getSVGDocument_success() throws Exception {
    doAnswer(
        i -> {
          i.getArgument(0, StringWriter.class).write("<svg>a-document</svg>");
          return null;
        })
        .when(mockG2)
        .stream(any(StringWriter.class));

    String result = svgImage.getSVGDocument();

    assertThat(result).isEqualTo("<svg>a-document</svg>");
  }

  @Test
  public void getSVGDocument_dioError() throws Exception {
    doThrow(SVGGraphics2DIOException.class).when(mockG2).stream(any(StringWriter.class));

    assertThrows(ImageSerializationException.class, () -> svgImage.getSVGDocument());
  }

  @Test
  public void getPNGImage_success() throws Exception {
    String svgContent =
        String.join("\n", Files.readAllLines(Paths.get("src/test/resources/svg/coffee.svg")));
    byte[] expectedPngContent = Files.readAllBytes(Paths.get("src/test/resources/svg/coffee.png"));

    doAnswer(
        i -> {
          i.getArgument(0, StringWriter.class).write(svgContent);
          return null;
        })
        .when(mockG2)
        .stream(any(StringWriter.class));

    byte[] result = svgImage.getPNGImage();

    assertThat(result).isEqualTo(expectedPngContent);
  }

  @Test
  public void getPNGImage_transcoderFailure() throws Exception {

    doAnswer(
        i -> {
          i.getArgument(0, StringWriter.class).write("some-bad-content");
          return null;
        })
        .when(mockG2)
        .stream(any(StringWriter.class));

    ImageSerializationException ex =
        assertThrows(ImageSerializationException.class, () -> svgImage.getPNGImage());

    assertThat(ex).hasCauseInstanceOf(TranscoderException.class);
  }
}
