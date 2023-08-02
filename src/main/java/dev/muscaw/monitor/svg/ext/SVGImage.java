package dev.muscaw.monitor.svg.ext;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.io.StringWriter;

public class SVGImage {

    private static final String SVG_NS = "http://www.w3.org/2000/svg";
    protected SVGGraphics2D g2;

    protected SVGImage(int width, int height) {
        DOMImplementation domImplementation = GenericDOMImplementation.getDOMImplementation();
        Document document = domImplementation.createDocument(SVG_NS, "svg", null);
        g2 = new SVGGraphics2D(document);
        g2.setColor(Color.BLACK);
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
}
