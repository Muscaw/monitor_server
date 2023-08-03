package dev.muscaw.monitor.svg.ext;

import java.awt.*;

public class WeatherSVGImage extends SVGImage {

    private WeatherSVGImage(int width, int height) {
        super(width, height);

        g2.setColor(Color.BLACK);
        g2.drawLine(0, 0, width, height);
        g2.setBackground(Color.BLUE);
        g2.fillRect(0, 0, width / 2, height / 2);
    }

    public static WeatherSVGImage newImage(int width, int height) {
        return new WeatherSVGImage(width, height);
    }
}
