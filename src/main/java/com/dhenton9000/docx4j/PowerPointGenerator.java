package com.dhenton9000.docx4j;

import com.dhenton9000.xml.utils.OffsetAdjuster;
import com.dhenton9000.xml.utils.XmlUtilities;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.io.IOUtils;
import org.docx4j.openpackaging.packages.Filetype;
import org.docx4j.openpackaging.packages.PresentationMLPackage;
import org.docx4j.openpackaging.parts.PresentationML.SlidePart;
import org.docx4j.openpackaging.packages.OpcPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.pptx4j.jaxb.Context;
import org.pptx4j.pml.Pic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Generates a powerpoint from a provide set of text substitutions and image. It
 * templateStream tied to the specific PPTX_TEMPLATE and templateStream not
 * generalizable.
 */
public class PowerPointGenerator {

    private static Logger LOG = LoggerFactory.getLogger(PowerPointGenerator.class);
    private static final String PPTX_TEMPLATE = "/docx_templates/substitution_sample.pptx";
    private static final String PIC_TEMPLATE = "docx_templates/part_templates/picElement.xml";
    public static final String IMAGE_SUFFIX = "png"; //only supported format

    /**
     *
     * @param mappings replacement mappings for text replace: "MAIN_TITLE", "MY
     * SLIDES" will replace ${MY_TITLE} in the template
     * @param isImage The input stream of the image to insert
     * @param outStream target output stream, could be a ByteArrayOutputStream
     * for downloading
     * @param scalePercent percent to scale the image eg 25.0f;
     *
     * @throws Exception
     */
    public void generate(HashMap<String, String> mappings, InputStream isImage, OutputStream outStream,float scalePercent) throws Exception {

        InputStream templateStream = this.getClass().getResourceAsStream(PPTX_TEMPLATE);


        Dimension imgDim = getImgDimension(isImage);
        if (templateStream == null) {
            throw new RuntimeException("can't find template file " + PPTX_TEMPLATE);
        }
        PresentationMLPackage presentationMLPackage
                = (PresentationMLPackage) OpcPackage.load(templateStream, Filetype.ZippedPackage);
        SlidePart slidePart0 = presentationMLPackage.getMainPresentationPart().getSlide(0);
        SlidePart slidePart1 = presentationMLPackage.getMainPresentationPart().getSlide(1);
        List<Object> contents = slidePart1.getContents().getCSld().getSpTree().getSpOrGrpSpOrGraphicFrame();
        Iterator partIter = contents.iterator();

        //
        slidePart0.variableReplace(mappings);
        while (partIter.hasNext()) {

            Object i = partIter.next();
            // LOG.debug(i.getClass().getName());
            if (i instanceof Pic) {
                partIter.remove();
            }
        }

        byte[] bytes = IOUtils.toByteArray(isImage);
        BinaryPartAbstractImage newImage
                = BinaryPartAbstractImage.createImagePart(presentationMLPackage, slidePart1, bytes);

        contents.add(1, createPicture(newImage.getSourceRelationships().get(0).getId(), imgDim,scalePercent));
        slidePart1.variableReplace(mappings);

        presentationMLPackage.save(outStream);

    }

    /**
     * get image dimensions
     *
     * @param imageStream
     * @param suffix
     * @return
     * @throws IOException
     */
    private static Dimension getImgDimension(InputStream imageStream) throws IOException {
        imageStream.mark(0);
        ImageInputStream stream = ImageIO.createImageInputStream(imageStream);
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(IMAGE_SUFFIX);

        while (iter.hasNext()) {
            ImageReader reader = iter.next();
            try {

                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                Dimension d = new Dimension(width, height);
                LOG.debug("PPTX read dim "+d);
                return d;

            } finally {
                reader.dispose();
                imageStream.reset();
            }
        }

        return null;
    }

    /**
     * create the xml Pic object that will be inserted into the PPTX structure
     *
     * @param relId
     * @param imgDim
     * @return
     * @throws Exception
     */
    private Object createPicture(String relId, Dimension imgDim, float scalePercent) throws Exception {
        java.util.HashMap<String, String> mappings = new java.util.HashMap<String, String>();

        OffsetAdjuster oA = new OffsetAdjuster(imgDim, scalePercent);
       // LOG.debug("oa " + oA.toString());

        mappings.put("id1", "4");
        mappings.put("name", "Picture 3");
        mappings.put("descr", "embedded.png");
        mappings.put("rEmbedId", relId);
        mappings.put("offx", oA.getOffsetX());
        mappings.put("offy", oA.getOffsetY());
        mappings.put("extcx", oA.getExtcX());//50% templateStream 5000000
        mappings.put("extcy", oA.getExtcY());

        String pixTemplate
                = XmlUtilities.getStringResource(PIC_TEMPLATE, this.getClass().getClassLoader());
        return org.docx4j.XmlUtils.unmarshallFromTemplate(pixTemplate,
                mappings, Context.jcPML, Pic.class);
    }

   
}
