package dev.muscaw.monitor.image.ext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.awt.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FontGroupTest {

  private Font base;
  private GraphicsEnvironment mockGe;
  private FontGroup fontGroup;

  @BeforeEach
  public void setUp() {
    base = new Font(Font.SERIF, Font.PLAIN, 10);
    mockGe = mock(GraphicsEnvironment.class);
    fontGroup = new FontGroup(base, mockGe);
  }

  @Test
  public void generateFont_doesNotYetExist() {
    var generatedFont = fontGroup.generateFont(Font.PLAIN, 10);

    assertThat(generatedFont).extracting(Font::getSize).isEqualTo(10);
    assertThat(generatedFont).extracting(Font::getStyle).isEqualTo(Font.PLAIN);
    verify(mockGe, times(1)).registerFont(generatedFont);
    verifyNoMoreInteractions(mockGe);
  }

  @Test
  public void generateFont_alreadyExists() {
    var generatedFont = fontGroup.generateFont(Font.PLAIN, 10);
    var generatedFont2 = fontGroup.generateFont(Font.PLAIN, 10);

    assertThat(generatedFont).extracting(Font::getSize).isEqualTo(10);
    assertThat(generatedFont).extracting(Font::getStyle).isEqualTo(Font.PLAIN);
    verify(mockGe, times(1)).registerFont(generatedFont);
    verifyNoMoreInteractions(mockGe);
    assertThat(generatedFont).isEqualTo(generatedFont2);
  }

  @Test
  public void generateFont_alreadyExistsGlobally() {
      var generatedFont = fontGroup.generateFont(Font.PLAIN, 10);
      var newFontGroup = new FontGroup(base, mockGe);
      var generatedFont2 = newFontGroup.generateFont(Font.PLAIN, 10);

      assertThat(generatedFont).extracting(Font::getSize).isEqualTo(10);
      assertThat(generatedFont).extracting(Font::getStyle).isEqualTo(Font.PLAIN);
      verify(mockGe, times(1)).registerFont(generatedFont);
      verifyNoMoreInteractions(mockGe);
      assertThat(generatedFont).isEqualTo(generatedFont2);
  }
}
