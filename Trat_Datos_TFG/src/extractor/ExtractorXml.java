package extractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ExtractorXml {
	
	private String atributos;
	private String datos;
	private String entrada;
	private String etiqueta;
	HashMap<String, String> data = new HashMap<String, String>();
	ArrayList<HashMap<String, String>> entradas = new ArrayList<HashMap<String, String>>();
	    
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
												
							atributos = null;   						//Vaciado del atributo para no ralentizar el proceso
							hijos = hijos.getNextSibling();
						}
					}
	    			
	    			/*if(nodoResponsable.getNodeType() == Node.ELEMENT_NODE) {
	    				System.out.println("El nodo accedido NO tiene hijos");
	    				columnas = columnas + textoXmlSinHijos(nodoResponsable);
	    				
	    			}*/
	    			nodoResponsable = nodoResponsable.getNextSibling();
    			}
    			
    		}
	
    		columnas = columnas.replace("null", "");
    		contiene = columnas.contains(entry);
    		if(contiene == true) {   			
    			columnas = columnas.replace(entry, " ");
    		}
    		
    		columnas = columnas.replace(",  ", "");
    		columnas = columnas.replace(", , ", "");
    		System.out.println("Las columnas son: " + columnas);
    		columnas = columnas + "\n" + getDatos();
    		
    		crearFichero(columnas);
    		System.out.println(data.toString());
	
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}

    }
    
    
    public String textoXml (Node nodo) {
    	   	
    	Node m = nodo.getFirstChild(); 

    	while(m != null) {
    		if (m.getNodeType() == Node.ELEMENT_NODE) {
    			System.out.println("- " + replaceString(m.getNodeName()));
    			setAtributos(replaceString(m.getNodeName()));
    			setEtiqueta(replaceString(m.getNodeName()));

    		}
    		
    		if(m.getFirstChild() != null) {	
    			textoXml(m);	
    		} else if (!m.getTextContent().isBlank()){
    			System.out.println("   " + m.getTextContent());
    			setDatos(m.getTextContent());
    			setEntrada(m.getTextContent());
    			data.put(getEtiqueta(), getEntrada());
    			//data.replace(replaceString(m.getNodeName()), " ", m.getTextContent());
    		}
    		m = m.getNextSibling();
    	}
 
    	return getAtributos();
    }     	
    
    
    public String textoXmlSinHijos (Node nodo) {
    	
    	String texto = "";
    	
		if (nodo.getNodeType() == Node.ELEMENT_NODE) {
			System.out.println("- "+ nodo.getNodeName());
			texto = texto + nodo.getNodeName() + ", ";
			
		}
    	return texto;
    	
    }
    
    
    public void crearFichero(String texto) {
    	
    	try {
    		String ruta_fichero = "C:/Users/alexf/Documents/Grado en INGENIERÍA INFORMÁTICA/TFG/atributos.csv";
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
    
    public void setDatos (String dat) {
    	this.datos = datos + dat + ", ";
    }
    
    public String getDatos () {
    	return datos;
    }

    public String replaceString (String texto) {
    	
    	if(texto.contains("cac:")) {
    		texto = texto.replace("cac:", "");
    	}

    	if(texto.contains("cac-place-ext:")) {
    		texto = texto.replace("cac-place-ext:", "");
    	}
    	if(texto.contains("cbc:")) {
    		texto = texto.replace("cbc:", "");
    	}
    	if(texto.contains("cbc-place-ext:")) {
    		texto = texto.replace("cbc-place-ext:", "");
    	}

    	return texto;
    }
    
    public String tratarDatos (String texto) {  	
    	texto = texto.replace("\n", ",");
    	texto = texto.replace("       ", ",");
    	texto = texto.replace("null,,", "");
    	texto = texto.replace(",,", ","); 
    	return texto;
    }
    
    public void setEntrada(String entrada) {
    	this.entrada = entrada;
    }
    
    public String getEntrada() {
    	return entrada;
    }
    
    public void setEtiqueta(String etiqueta) {
    	this.etiqueta = etiqueta;
    }
    
    public String getEtiqueta() {
    	return etiqueta;
    }
}
