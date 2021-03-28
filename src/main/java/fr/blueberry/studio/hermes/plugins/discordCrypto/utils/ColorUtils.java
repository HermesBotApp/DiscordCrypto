package fr.blueberry.studio.hermes.plugins.discordCrypto.utils;

import java.net.URL;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ColorUtils {
    
    public static Color pickColorFromImage(String url) {
        int redAvg = 0;
        int greenAvg = 0;
        int blueAvg = 0;
        int counter = 0;
        int red = 0;
        int green = 0;
        int blue = 0;

        try {
            BufferedImage img = ImageIO.read(new URL(url));

            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    int pixel = img.getRGB(x,y);
    
                    Color processColor = new Color(pixel, true);
                    
                    red = processColor.getRed();
                    green = processColor.getGreen();
                    blue = processColor.getBlue();
                    redAvg = redAvg + red;
                    greenAvg = greenAvg + green;
                    blueAvg = blueAvg + blue;
                    counter++;
                }
            }
        } catch(IOException e) {}
        
        counter = counter == 0 ? 1 : counter;

        int redColor = redAvg / counter;
        int greenColor = greenAvg / counter;
        int blueColor = blueAvg / counter;

        return new Color(redColor, greenColor, blueColor);
    }
}
