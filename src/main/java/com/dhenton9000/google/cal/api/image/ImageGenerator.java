/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.imageio.ImageIO;



public class ImageGenerator {

     private static final Logger LOG = LoggerFactory.getLogger(ImageGenerator.class);
    
    /**
     * create an image.
     * Sample usage:
     *   byte[] b =  createImage("png", "Spring MVC");
     * 
     * @param sImgType
     * @param message
     * @return 
     */
    public byte[] createImage(String sImgType, String message) {
        ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
        byte[] captchaBytes = null; // imageBytes

        int width = 200;
        int height = 50;

        try {
            BufferedImage bufImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufImage.createGraphics();
            drawString(g2d, Color.BLUE, new Font("mono", Font.BOLD, 10),
                    message, 3, 10);

            drawBorder(g2d, Color.BLUE, width, height);
            g2d.setColor(Color.RED);
            g2d.drawArc(100, 25, 20, 20, 0, 360);

            g2d.dispose();

            ImageIO.write(bufImage, sImgType, imgOutputStream);
            captchaBytes = imgOutputStream.toByteArray();
            //

        } catch (Exception e) {
            //
            LOG.error("Image failed #0001 ");
        }
        return captchaBytes;
    }

    private void drawBorder(Graphics2D g, Color color, int width, int height) {
        // draw a border
        g.setColor(color);
        g.drawRect(0, 0, width - 1, height - 1);
    }

    private void drawString(Graphics2D g, Color color, Font font, String str,
            int posX, int posY) {
        // Draw a string
        g.setColor(color);
        g.setFont(font);
        g.drawString(str, posX, posY);
    }

    private void fillBackground(Graphics2D g, Color color, int width, int height) {
        // Fill background
        g.setColor(color);
        g.fillRect(0, 0, width, height);
    }

 

}
