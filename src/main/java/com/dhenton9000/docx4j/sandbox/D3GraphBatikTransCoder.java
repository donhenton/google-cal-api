/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.docx4j.sandbox;

 
import com.dhenton9000.xml.utils.ReplaceFilterInputStream;
import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.svg.SVGDocument;

/**
 * class which takes the svg from a d3 graph and rasterizes it using
 * Batik.
 * 
 * 
 * @author dhenton
 */
public class D3GraphBatikTransCoder {

    protected static Logger LOG = LoggerFactory.getLogger(D3GraphBatikTransCoder.class);
    private final String svgInput ;
    
   public D3GraphBatikTransCoder(String svgInput) {
         this.svgInput = svgInput;
    }

    /**
     * adds the proper names space to svg that comes from d3
     * 
     * @param docText the inputstream that contains the svg string.
     * @return an svg inputstream where the namespace has been added
     * 
     */
    private InputStream addNS(InputStream docText) {
        Map<byte[], byte[]> rawReplacements = new HashMap<byte[], byte[]>();
        rawReplacements.put("<svg".getBytes(), 
                ("<svg xmlns=\"" + SVGDOMImplementation.SVG_NAMESPACE_URI + "\"").getBytes());
        LOG.debug("added namespace");
        return new ReplaceFilterInputStream(docText, rawReplacements);

    }

    /**
     * 
     * @param svgString the svg as a string, it should not have the ns defined;
     * @return a byte[] that contains the jpeg data.
     * @throws Exception 
     */
    public ByteArrayInputStream getDocument( ) throws Exception {
        LOG.debug("loading document ");
        InputStream docStream = new ByteArrayInputStream(svgInput.getBytes("UTF-8"));
        docStream = addNS(docStream);
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        SVGDocument document = factory.createSVGDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, docStream);
        int width = Integer.parseInt(document.getRootElement().getAttributeNode("width").getValue());
        int height = Integer.parseInt(document.getRootElement().getAttributeNode("height").getValue());
        Dimension d = new Dimension(width,height);
        LOG.debug("dim is "+d);

        JPEGTranscoder t = new JPEGTranscoder();
        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
                new Float(.9));

        // Set the transcoder input and output.
        TranscoderInput input = new TranscoderInput(document);

        TranscoderOutput output = new TranscoderOutput(ostream);

        // Perform the transcoding.
        t.transcode(input, output);
        return new ByteArrayInputStream(ostream.toByteArray());
        

    }

}
