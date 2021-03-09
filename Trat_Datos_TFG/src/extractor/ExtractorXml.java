package extractor;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class ExtractorXml {
	    
    public void abrirDoc(String fichero) {
    	
    	try {
    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    		Document doc = dBuilder.parse(fichero);
    		doc.getDocumentElement().normalize();
    		NodeList nList = doc.getElementsByTagName("entry");
    		
    		for (int i = 0; i < nList.getLength(); i++) {
    			Node n = nList.item(i);
    			
    			if (n.getNodeType() == Node.ELEMENT_NODE) {
    				Element e = (Element) n;
    				
    				System.out.println("\nid: " +e.getElementsByTagName("id").item(0).getTextContent());
    			}
    		}
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}

    }
    
    
}
