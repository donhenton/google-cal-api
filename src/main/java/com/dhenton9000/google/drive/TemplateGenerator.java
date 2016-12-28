
package com.dhenton9000.google.drive;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P.Hyperlink;
import org.docx4j.wml.R;
import org.docx4j.wml.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * This is the template generator class it is tied to 
 * src/main/resources/demo_template.docx
 * and is not generalizable.
 * 
 * 
 * 
 */
public class TemplateGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateGenerator.class);
   // public static final String TEMPLATE_PATH = "/docx_templates/demo_template.docx";

    public byte[] replaceToByteArray(Properties replacementProps, String classToTemplate)
            throws JAXBException, Docx4JException, UnsupportedEncodingException {

        WordprocessingMLPackage pkg = doReplace(replacementProps, classToTemplate);

        return writeDocxToByteArray(pkg);

    }

    public void replaceToFile(Properties replacementProps, String classToTemplate, String fileDest)
            throws JAXBException, Docx4JException, IOException {

        WordprocessingMLPackage pkg = doReplace(replacementProps, classToTemplate);

        writeDocxToFile(pkg, fileDest);

    }

    /**
     * obtain a template resource from the classpath.
     *
     * @param classPathToTemplate
     * @return
     * @throws Docx4JException , RuntimeException if item not found on bath
     */
    private WordprocessingMLPackage getTemplate(String classPathToTemplate) throws Docx4JException {
        InputStream in = this.getClass().getResourceAsStream(classPathToTemplate);
        if (in == null) {
            throw new RuntimeException("couldn't find template at classpath location '" + classPathToTemplate + "'");
        } else {
            WordprocessingMLPackage template = WordprocessingMLPackage.load(in);

            return template;
        }

    }

    /**
     * get all the objects that match the given class.
     *
     * @param obj the part of the docx4j object that we want to search
     *
     * @param toSearch the class to search
     *
     *
     * List<Object> texts =
     * getAllElementFromObject(template.getMainDocumentPart(), Text.class);
     *
     *
     * @return
     */
    private List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<Object>();
        if (obj instanceof JAXBElement) {
            obj = ((JAXBElement<?>) obj).getValue();
        }

        if (obj.getClass().equals(toSearch)) {
            result.add(obj);
        } else if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }

        }
        return result;
    }

    /**
     * replace the values in the template
     *
     * @param template the template to use
     * @param replacementValue the value for replacement, eg "fred@fred.com"
     * @param placeHolderText aka the key, eg "TO_EMAIL_TEXT"
     *
     */
    private void replacePlaceHolder(WordprocessingMLPackage template, String replacementValue, String placeHolderText) {
        List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);

        for (Object text : texts) {
            Text textElement = (Text) text;
           // LOG.debug(String.format("trying replacing %s into '%s'",placeHolderText,textElement.getValue()));
            if (textElement.getValue().toUpperCase().contains(placeHolderText)) {
              //  LOG.debug("did replace "+placeHolderText);
                textElement.setValue(replacementValue);
            }
        }
    }

    private byte[] writeDocxToByteArray(WordprocessingMLPackage template) throws Docx4JException, UnsupportedEncodingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        template.save(baos);
        return baos.toByteArray();

    }

    private void writeDocxToFile(WordprocessingMLPackage template, String target) throws IOException, Docx4JException {
        File f = new File(target);

        template.save(f);
    }

    private WordprocessingMLPackage doReplace(Properties replacementProps,
            String classToTemplate)
            throws JAXBException, Docx4JException {

        WordprocessingMLPackage pkg = getTemplate(classToTemplate);
        MainDocumentPart mainDocument = pkg.getMainDocumentPart();
        for (Object k : replacementProps.keySet()) {
            String placeHolderText = k.toString();
            //  LOG.debug("place " + placeHolderText);
            String replacementValue = replacementProps.getProperty(placeHolderText);
            if (!placeHolderText.contains("HYPERLINK")) {

                replacePlaceHolder(pkg, replacementValue, placeHolderText);
            }

        }//end for 
        String replacementHref = replacementProps.getProperty("HYPERLINK_HREF");
        String replacementText = replacementProps.getProperty("HYPERLINK_TEXT");
        replaceHyperLink(mainDocument, replacementHref, replacementText);
        return pkg;

    }

    /**
     * find the hyperlink (there is only one in the demo).
     * replace the url content
     * 
     * @param mdp
     * @param url
     * @param replacementText
     * @throws JAXBException 
     */
    private void replaceHyperLink(MainDocumentPart mdp, String url, String replacementText) throws JAXBException {
        List<Object> hyperLinkList = getAllElementFromObject(mdp, Hyperlink.class);

        Hyperlink link = (Hyperlink) hyperLinkList.get(0);
        //replacing the link text
        R rContent = (R) link.getContent().get(0);
        JAXBElement jaxContent = (JAXBElement) rContent.getContent().get(0);
        // Text text = (Text) jaxContent.getValue();
        //LOG.debug("text value " + text.getValue());  
        jaxContent.setValue(replacementText);

        //replacing the url
        mdp.getRelationshipsPart().getRelationships().getRelationship().forEach(r -> {
            if (link.getId().equals(r.getId())) {
                //LOG.debug(String.format("link id %s rel id %s target %s type %s", 
                //link.getId(), r.getId(), r.getTarget(), r.getType()));
                r.setTarget(url);

            }
        });


    }

}
