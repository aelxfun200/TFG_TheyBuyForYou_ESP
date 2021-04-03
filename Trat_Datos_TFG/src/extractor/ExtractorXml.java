package extractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ExtractorXml {
	
	private String atributos;
	    
    public void abrirDoc(String fichero) {

    	try {
    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    		Document doc = dBuilder.parse(fichero);
    		doc.getDocumentElement().normalize();
    		Element raiz = doc.getDocumentElement();
    		System.out.println("La raiz del documento es:" + raiz.getNodeName());
    		String columnas = "";
    		String entry = "entry";
    		String previoColumnas = "";
    		boolean contiene;
    		
    		Node nodoResponsable = raiz.getFirstChild().getNextSibling();
    		
    		if(nodoResponsable.getNodeType() == Node.ELEMENT_NODE) {
    			//System.out.println("El nombre de la etiqueta es: " + nodoResponsable.getNodeName());
    			//System.out.println("Otra forma de devolver el texto de la etiqueta: " + nodoResponsable.getTextContent());
    			
    			while(nodoResponsable.getNextSibling() != null) {
    			
					Node hijos = nodoResponsable.getFirstChild();
					
					if(hijos != null) {
						while(hijos.getNextSibling() != null) {
							
							previoColumnas = textoXml(hijos);
							if(previoColumnas !=null) {
								System.out.println("previoColumnas:" + previoColumnas);
								String[] atributo = previoColumnas.split(",");
	
								for(int i = 0; i < atributo.length; i++) {
	
									if(!columnas.contains(atributo[i])) {
										columnas = columnas + atributo[i] + ",";	
									}
								} 
							}

							//columnas = columnas + textoXml(hijos,entrada);
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
    		columnas = columnas.replace("null", "");
    		columnas = columnas.replace(",  ", "");
    		if(contiene == true) {   			
    			columnas = columnas.replace(entry, "\n");
    		}
    		System.out.println("Las columnas son: " + columnas);
    		
    		crearFichero(columnas);

	
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}

    }
    
    
    public String textoXml (Node nodo) {
    	   	
    	Node m = nodo.getFirstChild(); 

    	while(m != null) {
    		if (m.getNodeType() == Node.ELEMENT_NODE) {
    			System.out.println("- " + m.getNodeName());
    			setAtributos(m.getNodeName());
    		
    		}
    		
    		if(m.getFirstChild() != null) {	
    			textoXml(m);	
    		}
	
    		m = m.getNextSibling();
    	}
    	
    	return getAtributos();
    }     	
    
    
    public String textoXmlSinHijos (Node nodo) {
    	
    	String texto = "";
		//System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		if (nodo.getNodeType() == Node.ELEMENT_NODE) {
			System.out.println("- "+ nodo.getNodeName());
			texto = texto + nodo.getNodeName() + ", ";
		}
		System.out.println(nodo.getTextContent());
		//texto = "NodeName: "+ nodo.getNodeName() + "\n";
    	//System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    	return texto;
    	
    }
    
    
    public void crearFichero(String texto) {
    	
    	try {
    		String ruta_fichero = "C:/Users/alexf/Documents/Grado en INGENIERÍA INFORMÁTICA/TFG/atributos.txt";
    		File fichero = new File(ruta_fichero);
    		if(!fichero.exists()) {
    			fichero.createNewFile();
    		}
    		
    		FileWriter escribir = new FileWriter(fichero);
    		BufferedWriter bw = new BufferedWriter(escribir);
    		bw.write(texto);
    		bw.close();
    		
    		
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    	
    }
    
    public void setAtributos (String att) {
    	this.atributos = atributos + att + ", ";
    }
    
    public String getAtributos () {
    	return atributos;
    }

    
}
