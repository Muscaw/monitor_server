package dev.muscaw.monitor.conf;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageConfiguration {
  @Bean
  public Font getImageFont() throws IOException, FontFormatException {
    var font = Font.createFont(
        Font.TRUETYPE_FONT, Paths.get("src/main/resources/font/basis33/basis33.ttf").toFile());
    return font.deriveFont(Font.BOLD, 20);
  }
}
