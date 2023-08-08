package dev.muscaw.monitor.conf;

import dev.muscaw.monitor.image.ext.FontGroup;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageConfiguration {
  @Bean
  public FontGroup getImageFont() throws IOException, FontFormatException {
    var font =
        Font.createFont(
            Font.TRUETYPE_FONT, Paths.get("src/main/resources/font/pixeloid/PixeloidSans-mLxMm.ttf").toFile());
    return new FontGroup(font);
  }
}
