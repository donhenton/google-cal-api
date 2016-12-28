/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api.controllers;

/**
 *
 * @author dhenton
 */
public class SVGForm {
    private String svgData ;
    private String imageTitle;
    private String imageSubTitle;

    @Override
    public String toString() {
        return "SVGForm{" + "imageTitle=" + imageTitle + ", imageSubTitle=" + imageSubTitle + '}';
    }
    
    
    
    

    /**
     * @return the svgData
     */
    public String getSvgData() {
        return svgData;
    }

    /**
     * @param svgData the svgData to set
     */
    public void setSvgData(String svgData) {
        this.svgData = svgData;
    }

    /**
     * @return the imageTitle
     */
    public String getImageTitle() {
        return imageTitle;
    }

    /**
     * @param imageTitle the imageTitle to set
     */
    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    /**
     * @return the imageSubTitle
     */
    public String getImageSubTitle() {
        return imageSubTitle;
    }

    /**
     * @param imageSubTitle the imageSubTitle to set
     */
    public void setImageSubTitle(String imageSubTitle) {
        this.imageSubTitle = imageSubTitle;
    }
}
