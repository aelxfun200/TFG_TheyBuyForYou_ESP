package extractor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ExtractorXml {
	    
    public void abrirDoc(String fichero) {

    	try {
    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    		Document doc = dBuilder.parse(fichero);
    		doc.getDocumentElement().normalize();
    		Element raiz = doc.getDocumentElement();
    		System.out.println("La raiz del documento es:" + raiz.getNodeName());
    		String columnas = "";
    		String entry = " entry,";
    		boolean contiene;
	
    		Node nodoResponsable = raiz.getFirstChild().getNextSibling();
    		
    		if(nodoResponsable.getNodeType() == Node.ELEMENT_NODE) {
    			//System.out.println("El nombre de la etiqueta es: " + nodoResponsable.getNodeName());
    			//System.out.println("Otra forma de devolver el texto de la etiqueta: " + nodoResponsable.getTextContent());
    			
    			while(nodoResponsable.getNextSibling() != null) {
    			
					Node hijos = nodoResponsable.getFirstChild();
					if(hijos != null) {
						while(hijos.getNextSibling() != null) {
							//System.out.println("-");
							//if (hijos.getNodeType() == Node.ELEMENT_NODE) {
							//	System.out.println(hijos.getNodeName());
							//}
							columnas = columnas + textoXml(hijos);
							hijos = hijos.getNextSibling();
						}
					}
	    			
	    			if(nodoResponsable.getNodeType() == Node.ELEMENT_NODE) {
	    				System.out.println("El nodo accedido NO tiene hijos");
	    				columnas = columnas + textoXmlSinHijos(nodoResponsable);
	    			}
	    			nodoResponsable = nodoResponsable.getNextSibling();
    			}
    			
    		}

    		contiene = columnas.contains(entry);
    		columnas = columnas.replace("      ", "");
    		if(contiene == true) {   			
    			columnas = columnas.replace(",  entry,", "\n");
    		}
    		System.out.println("Las columnas son:" + columnas);

	
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}

    }
    
    
    public String textoXml (Node nodo) {
    	
    	String texto = " ";
    	
    	Node m = nodo.getFirstChild();
    	

    	while(m != null) {
    		if (m.getNodeType() == Node.ELEMENT_NODE) {
    			//System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    			System.out.println("- " + m.getNodeName());
    			texto = texto + m.getNodeName() + ", " ;
    		}
    		
    		if(m.getFirstChild() != null) {
    			System.out.println(m.getTextContent());
    			
    			textoXml(m);
    			
    		}
    		
    		//texto = texto.concat("NodeName: "+ m.getNodeName() + "\n");
    			
    		m = m.getNextSibling();
    	}
    	return texto;
    
    }     	
    
    
    public String textoXmlSinHijos (Node nodo) {
    	
    	String texto = " ";
		//System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		if (nodo.getNodeType() == Node.ELEMENT_NODE) {
			System.out.println("- "+ nodo.getNodeName());
			texto = texto + nodo.getNodeName() + ",";
		}
		System.out.println(nodo.getTextContent());
		//texto = "NodeName: "+ nodo.getNodeName() + "\n";
    	//System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    	return texto;
    	
    }
    
    
    public void crearFichero() {
    	
    }
    
}
