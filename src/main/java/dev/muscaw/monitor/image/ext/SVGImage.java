package dev.muscaw.monitor.image.ext;

import dev.muscaw.monitor.image.domain.Renderable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.io.*;
import java.util.List;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class SVGImage implements Renderable {

  private static final String SVG_NS = "http://www.w3.org/2000/svg";
  protected SVGGraphics2D g2;

  protected SVGImage(int width, int height) {
    DOMImplementation domImplementation = SVGDOMImplementation.getDOMImplementation();
    Document document = domImplementation.createDocument(SVG_NS, "svg", null);
    g2 = new SVGGraphics2D(document);
    g2.setColor(Color.BLACK);
    g2.setSVGCanvasSize(new Dimension(width, height));
  }

  public String getSVGDocument() {
    StringWriter writer = new StringWriter();
    try {
      g2.stream(writer);
    } catch (SVGGraphics2DIOException e) {
      // Not a recoverable error.
      throw new RuntimeException(e);
    }
    return writer.toString();
  }

  public byte[] getPNGImage() {
    StringReader reader = new StringReader(getSVGDocument());
    PNGTranscoder transcoder = new PNGTranscoder();
    TranscoderInput input = new TranscoderInput(reader);
    ByteArrayOutputStream ostream = new ByteArrayOutputStream();
    TranscoderOutput output = new TranscoderOutput(ostream);
    try {
      transcoder.transcode(input, output);
    } catch (TranscoderException e) {
      // Not recoverable
      throw new RuntimeException(e);
    }
    try {
      ostream.flush();
      ostream.close();
    } catch (IOException e) {
      // Not recoverable
      throw new RuntimeException(e);
    }
    return ostream.toByteArray();
  }

  protected int getStringHeight() {
    FontMetrics metrics = g2.getFontMetrics();
    return metrics.getHeight();
  }

  protected int getStringWidth(String value) {
    FontMetrics metrics = g2.getFontMetrics();
    return metrics.stringWidth(value);
  }

  protected void drawStringCentered(int x, int y, String value) {
    int width = getStringWidth(value);

    drawStringAt(x - (width / 2), y, value);
  }

  protected void drawStringAt(int x, int y, String value) {
    int height = getStringHeight();

    g2.drawString(value, x, y + (height / 2));
  }

  protected void drawLines(int x, int y, List<String> lines) {
    int height = getStringHeight();
    int currentY = y;
    for (String line : lines) {
      drawStringAt(x, currentY, line);
      currentY += height;
    }
  }

  public String asSerial() {
    return "bwbwwwbbb";
  }
}
