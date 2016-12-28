/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.docx4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.packages.PresentationMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.PresentationML.MainPresentationPart;
import org.docx4j.openpackaging.parts.PresentationML.SlideLayoutPart;
import org.docx4j.openpackaging.parts.PresentationML.SlidePart;
import org.pptx4j.jaxb.Context;
import org.pptx4j.pml.Sld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PresentationMLPackage presentationMLPackage = (PresentationMLPackage)
 * OpcPackage.load(is, Filetype.ZippedPackage);
 *
 * @author dhenton
 */
public class SlideUtils {

    public static final String MAIN_PRESENTATION_NAME = "/ppt/presentation.xml";
    private static final String SLIDE_TEMPLATE = "/part_templates/slide_part.xml";
    protected static Logger LOG = LoggerFactory.getLogger(SlideUtils.class);

    /**
     * This will insert a blank slide using SLIDE_TEMPLATE as a template. The
     * template contains variables suitable for replacement
     *
     * @param presentationMLPackage the pointer to the original pptx file which
     * is a template. 
     * @param mappings since this is a new slide the variables that are 
     * being replaced are in SLIDE_TEMPLATE
    
     * @throws Exception
     */
    public static void appendSlide(PresentationMLPackage presentationMLPackage,HashMap<String,String> mappings)
            throws Exception {

        MainPresentationPart mainPresentationPart = (MainPresentationPart) presentationMLPackage
                .getParts().getParts().get(new PartName(MAIN_PRESENTATION_NAME));

        SlideLayoutPart layoutPart = (SlideLayoutPart) presentationMLPackage.getParts().getParts()
                .get(new PartName("/ppt/slideLayouts/slideLayout2.xml")); //base slide format, not the title page

        int slideCount = mainPresentationPart.getSlideCount();
        SlidePart slidePart = new SlidePart(new PartName("/ppt/slides/slide" + (slideCount+1) + ".xml"));
        mainPresentationPart.addSlideIdListEntry(slidePart);
        slidePart.setJaxbElement(SlidePart.createSld());

        // Slide layout part
        slidePart.addTargetPart(layoutPart);

        StringBuilder slideXMLBuffer = new StringBuilder();
        BufferedReader br = null;
        String line = "";

        InputStream in = SlideUtils.class.getResourceAsStream(SLIDE_TEMPLATE);
        Reader fr = new InputStreamReader(in, "utf-8");
        br = new BufferedReader(fr);
        while ((line = br.readLine()) != null) {
            slideXMLBuffer.append(line);
            slideXMLBuffer.append(" ");
        }
        Sld sld = (Sld) XmlUtils.unmarshalString(slideXMLBuffer.toString(), Context.jcPML,
                Sld.class);
        //slide1.setJaxbElement(sld);
        slidePart.setContents(sld);
        slidePart.variableReplace(mappings);

    }

}
