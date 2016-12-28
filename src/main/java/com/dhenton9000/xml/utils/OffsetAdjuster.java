package com.dhenton9000.xml.utils;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this class computes the offsets for an image to be centered
 * on the slide.
 * 
             <p:spPr>
                    <a:xfrm>
                        <a:off x="4281437" y="2990906"/>
                        <a:ext cx="10922000" cy="2667000"/>
                    </a:xfrm>
                    <a:prstGeom prst="rect">
                        <a:avLst/>
                    </a:prstGeom>
                </p:spPr>
 * 
 * 
 * 
 * @author dhenton
 */
public class OffsetAdjuster {


    private final Point2D.Float imageDimension;
    private final float scale;
    private final String offsetX;
    private final String offsetY;
    private final String extcX;
    private final String extcY;
    //pixels per inch
    private final static double DPI = 72d;
    //the PPTX units per inch
    private final static long UNITS_PER_IN = 915000;
    //width in inches of a PPTX slide
    private final static float PPTX_WIDTH_IN = 13.3f;
    //height in inches of a PPTX slide
    private final static float PPTX_HEIGHT_IN = 7.5f;
    private static Logger LOG = LoggerFactory.getLogger(OffsetAdjuster.class);

    public OffsetAdjuster(Dimension imgDim, float scaleAsPercent) {
        //img comes in as pixels
        this.scale = scaleAsPercent/100.0f;
        float w = (new Double((double) imgDim.width * this.scale/DPI)).floatValue();
        float h = (new Double((double) imgDim.height * this.scale/DPI)).floatValue();
        //it has been scaled and switched to inches
         this.imageDimension = new Point2D.Float(w,h);
              
        double wCalc = (PPTX_WIDTH_IN - this.imageDimension.getX())/2;
        this.offsetX = Long.toString((long) Math.floor(wCalc * UNITS_PER_IN)); 
         
        double hCalc = (PPTX_HEIGHT_IN - this.imageDimension.getY())/2;
        this.offsetY = Long.toString((long) Math.floor(hCalc * UNITS_PER_IN)); 
        //2500000 is 25%
       // Long extLong = new Float(scaleAsPercent).longValue() * 100000;
       // this.extcX = extLong.toString();
       // this.extcY = this.extcX;
       Long dx = (new Double(this.imageDimension.getX()* UNITS_PER_IN)).longValue();
       Long dy = (new Double(this.imageDimension.getY()* UNITS_PER_IN)).longValue();
       
       this.extcX = dx+"";
       this.extcY = dy + "";
       
        
    }

    /**
     * @return the imageDimension
     */
    public Point2D.Float getImageDimension() {
        return imageDimension;
    }

    /**
     * @return the offsetX
     */
    public String getOffsetX() {
        return offsetX;
    }

    /**
     * @return the offsetY
     */
    public String getOffsetY() {
        return offsetY;
    }

    /**
     * @return the extcX
     */
    public String getExtcX() {
        return extcX;
    }

    /**
     * @return the extcY
     */
    public String getExtcY() {
        return extcY;
    }

    /**
     * @return the scale
     */
    public float getScale() {
        return scale;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.offsetX);
        hash = 37 * hash + Objects.hashCode(this.offsetY);
        hash = 37 * hash + Objects.hashCode(this.extcX);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OffsetAdjuster other = (OffsetAdjuster) obj;
        if (!Objects.equals(this.offsetX, other.offsetX)) {
            return false;
        }
        if (!Objects.equals(this.offsetY, other.offsetY)) {
            return false;
        }
        if (!Objects.equals(this.extcX, other.extcX)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OffsetAdjuster{" + "offsetX=" + offsetX + ", offsetY=" + offsetY + ", extcX=" + extcX + ", extcY=" + extcY + '}';
    }

}
