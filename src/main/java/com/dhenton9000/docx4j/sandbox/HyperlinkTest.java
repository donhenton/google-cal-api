
package com.dhenton9000.docx4j.sandbox;





 
import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.relationships.Namespaces;
import org.docx4j.wml.P.Hyperlink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Fun with hyperlinks to external resources
 * eg web pages.
 * 
 * For an example of an internal hyperlink,
 * see the BookmarkAdd sample.
 * 
 * @author Jason Harrop
 */
public class HyperlinkTest {
    
        private static final Logger LOG = LoggerFactory.getLogger(HyperlinkTest.class);
	
	/*
	 * <w:p>
	 * 	<w:r>
	 * 		<w:t xml:space="preserve">Here is an example of a </w:t>
	 *  </w:r>
	 *  <w:hyperlink r:id="rId4" w:history="1">
	 *  	<w:r>
	 *  		<w:rPr>
	 *  			<w:rStyle w:val="Hyperlink"/>
	 *  		</w:rPr>
	 *  		<w:t>hyperlink</w:t>
	 *  	</w:r>
	 *  </w:hyperlink>
	 *  <w:r>
	 *  	<w:t xml:space="preserve">.  </w:t>
	 *  </w:r>
	 * </w:p>
	 * 
	 * 
	 * word/_rels/document.xml.rels contains:
	 * 
	 * <Relationship Id="rId4" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink" 
	 * 		Target="http://dev.plutext.org/" TargetMode="External"/>

	 */

 
	
	public static Hyperlink createHyperlink(MainDocumentPart mdp, String url) {
		
		try {

			// We need to add a relationship to word/_rels/document.xml.rels
			// but since its external, we don't use the 
			// usual wordMLPackage.getMainDocumentPart().addTargetPart
			// mechanism
			org.docx4j.relationships.ObjectFactory factory =
				new org.docx4j.relationships.ObjectFactory();
			
			org.docx4j.relationships.Relationship rel = factory.createRelationship();
			rel.setType( Namespaces.HYPERLINK  );
			rel.setTarget(url);
			rel.setTargetMode("External");  
									
			mdp.getRelationshipsPart().addRelationship(rel);
			
			// addRelationship sets the rel's @Id
			
			String hpl = "<w:hyperlink r:id=\"" + rel.getId() + "\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" " +
            "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" >" +
            "<w:r>" +
            "<w:rPr>" +
            "<w:rStyle w:val=\"Hyperlink\" />" +  // TODO: enable this style in the document!
            "</w:rPr>" +
            "<w:t>Link</w:t>" +
            "</w:r>" +
            "</w:hyperlink>";

//			return (Hyperlink)XmlUtils.unmarshalString(hpl, Context.jc, P.Hyperlink.class);
			return (Hyperlink)XmlUtils.unmarshalString(hpl);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
		
}

