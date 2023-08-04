package dev.muscaw.monitor.image.ext;

import dev.muscaw.monitor.image.domain.ImageSerializationException;
import dev.muscaw.monitor.image.domain.Renderable;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public final class SVGImage implements Renderable {

  private static final String SVG_NS = "http://www.w3.org/2000/svg";
  private SVGGraphics2D g2;
  private int width;
  private int height;

  SVGImage(SVGGraphics2D g2, int width, int height) {
    this.g2 = g2;
    this.width = width;
    this.height = height;
  }

  public static SVGImage newImage(int width, int height) {
    DOMImplementation domImplementation = SVGDOMImplementation.getDOMImplementation();
    Document document = domImplementation.createDocument(SVG_NS, "svg", null);
    SVGGraphics2D g2 = new SVGGraphics2D(document);
    g2.setColor(Color.BLACK);
    g2.setSVGCanvasSize(new Dimension(width, height));

    return new SVGImage(g2, width, height);
  }

  private void setDarkColor() {
    g2.setColor(Color.BLACK);
  }

  public int getStringHeight() {
    FontMetrics metrics = g2.getFontMetrics();
    return metrics.getHeight();
  }

  public int getStringWidth(String value) {
    FontMetrics metrics = g2.getFontMetrics();
    return metrics.stringWidth(value);
  }

  public void drawStringCentered(int x, int y, String value) {
    int width = getStringWidth(value);

    drawStringAt(x - (width / 2), y, value);
  }

  public void drawStringAt(int x, int y, String value) {
    int height = getStringHeight();

    g2.drawString(value, x, y + (height / 2));
  }

  public void drawLines(int x, int y, List<String> lines) {
    int height = getStringHeight();
    int currentY = y;
    for (String line : lines) {
      drawStringAt(x, currentY, line);
      currentY += height;
    }
  }

  public void drawRect(int x, int y, int width, int height) {
    setDarkColor();
    g2.drawRect(x, y, width, height);
  }

  // Exporter functions
  public String asSerial() {
    byte[] pngImage = getPNGImage();
    BufferedImage image;
    try {
      image = ImageIO.read(new ByteArrayInputStream(pngImage));
    } catch (IOException e) {
      // Not a recoverable error. Should not be thrown as everything happens in memory
      throw new RuntimeException(e);
    }
    StringBuilder serializedImage = new StringBuilder();
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int color = image.getRGB(x, y);
        serializedImage.append((color & 0x00FFFFFF) == 0xFFFFFF ? "w" : "b");
      }
    }
    return serializedImage.toString();
  }

  public String getSVGDocument() {
    StringWriter writer = new StringWriter();
    try {
      g2.stream(writer);
    } catch (SVGGraphics2DIOException e) {
      // Not a recoverable error.
      throw new ImageSerializationException("Could not transform document to SVG string", e);
    }
    return writer.toString();
  }

  public byte[] getPNGImage() {
    StringReader reader = new StringReader(getSVGDocument());
    PNGTranscoder transcoder = new PNGTranscoder();
    transcoder.addTranscodingHint(ImageTranscoder.KEY_BACKGROUND_COLOR, Color.WHITE);
    TranscoderInput input = new TranscoderInput(reader);
    ByteArrayOutputStream ostream = new ByteArrayOutputStream();
    TranscoderOutput output = new TranscoderOutput(ostream);
    try {
      transcoder.transcode(input, output);
    } catch (TranscoderException e) {
      // Not recoverable
      throw new ImageSerializationException("Could not transform document to PNG", e);
    }
    try {
      ostream.flush();
      ostream.close();
    } catch (IOException e) {
      // Not recoverable and should not happen as we don't write on disk
      throw new RuntimeException(e);
    }
    return ostream.toByteArray();
  }
}
