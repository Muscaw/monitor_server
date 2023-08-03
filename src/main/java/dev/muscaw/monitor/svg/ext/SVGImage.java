package dev.muscaw.monitor.svg.ext;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.io.IOException;
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

    public byte[] getJpg() {
        JPEGTranscoder transcoder = new JPEGTranscoder();

        transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, .8f);

        TranscoderInput input = new TranscoderInput(g2.getDOMFactory());

        StringWriter writer = new StringWriter();
        TranscoderOutput output = new TranscoderOutput(writer);

        try {
            transcoder.transcode(input, output);
        } catch (TranscoderException e) {
            // Not a recoverable exception
            throw new RuntimeException(e);
        }

        writer.flush();
        try {
            writer.close();
        } catch (IOException e) {
            // Not a recoverable exception as it close on a StringWriter should be a no-op
            throw new RuntimeException(e);
        }

        return writer.toString().getBytes();
    }
}
