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
import org.w3c.dom.NodeList;

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
    		//System.out.println("La raiz del documento es:" + raiz.getNodeName());
    		String columnas = "";
    		String entry = "entry";
    		String previoColumnas = "";
    		String datosColumnas = "";
    		boolean contiene;
    		
    		NodeList nList = doc.getElementsByTagName("entry");
    				//raiz.getFirstChild().getNextSibling();
    		
    		for(int j = 0; j < nList.getLength(); j++) {
    			Node nodoResponsable = nList.item(j);
    			
	    		if(nodoResponsable.getNodeType() == Node.ELEMENT_NODE) {
	    			
	    			//while(nodoResponsable.getNextSibling() != null) {
	    			
						Node hijos = nodoResponsable.getFirstChild().getNextSibling();
						
						if(hijos != null && hijos.getNodeType() == Node.ELEMENT_NODE) {
							while(hijos.getNextSibling() != null) {
								if(hijos.getNodeType() == Node.ELEMENT_NODE) {
									System.out.println( "+ " + hijos.getNodeName());
		    						columnas = columnas + replaceString(hijos.getNodeName()) + ", ";
								}
								
								previoColumnas = textoXml(hijos,0);
								if(previoColumnas !=null) {
									
									String[] atributo = previoColumnas.split(",");
		
									for(int i = 0; i < atributo.length; i++) {
		
										//if(!columnas.contains(atributo[i])) {
										if(!atributo[i].isBlank())
											columnas = columnas + atributo[i] + ",";	
											
										//}
										
										/*if(contiene(atributo[i]) == true) {
											atributo
										}*/
									}
								}
								if(datos != null) {
									datosColumnas = datosColumnas + getDatos();
								}
								datos = "";					
								atributos = "";   						//Vaciado del atributo para no ralentizar el proceso
								hijos = hijos.getNextSibling();
							}
						}
		    			
		    		
		    			nodoResponsable = nodoResponsable.getNextSibling();
	    			}
	    		
	    		datosColumnas = datosColumnas + "\n";
    		}
	
    		columnas = columnas.replace("null", "");
    		contiene = columnas.contains(entry);
    		if(contiene == true) {
    			datosColumnas = datosColumnas + "\n";
    			columnas = columnas.replace(entry, " ");    			
    		}
    		
    		System.out.println("Las columnas son: " + columnas);
    		columnas = columnas + "\n" + datosColumnas;
    		
    		crearFichero(columnas, "prueba", ".txt");
	
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}

    }
    
    
    public String textoXml (Node nodo, int contador) {
    	   	
    	Node m = nodo.getFirstChild(); 
    	//int contador = 0;

    	while(m != null) {

    		if (m.getNodeType() == Node.ELEMENT_NODE ) {
    			System.out.println("- " + replaceString(m.getNodeName()));
    			setAtributos(replaceString(m.getNodeName()));
    			
    			if(contiene(replaceString(m.getNodeName())) == true) { //
    				if (m.getParentNode().getNodeName() != "entry") {  
    					System.out.println("==" + replaceString(m.getNodeName()));
    					Node nodoPadre = m.getParentNode();
    					int cont = contador;
    					while(nodoPadre.getParentNode() != null && nodoPadre.getParentNode().getNodeName()!= "entry") {
    						nodoPadre = nodoPadre.getParentNode();
    						System.out.println("El primer contador es: " + contador);
    						contador ++;
    					}
    					
    					//if(contador > 0) {
        					if (crearFichero(m.getNodeName(), replaceString(nodoPadre.getNodeName()), ".txt") == false)  {
        						contador --;
        					}
        					
    					//}
    				}
    			}
    		}
    		
    		if(m.getFirstChild() != null) {	
    			System.out.println("Se invoca a la función textoXml con el valor del contador: " + contador);
    			textoXml(m, contador);	
    		} else if (!m.getTextContent().isBlank()){
    			System.out.println("   " + m.getTextContent());
    			setDatos(m.getTextContent());
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
    
    
    public boolean crearFichero(String texto, String nombre, String extension) {
    	boolean dev = false;
    	try {
    		String ruta_fichero = "C:/Users/alexf/Documents/Grado en INGENIERÍA INFORMÁTICA/TFG/"+ nombre + extension;
    		File fichero = new File(ruta_fichero);
    		if(!fichero.exists()) {
    			fichero.createNewFile();
    			dev = true;
    		}
    		
    		FileWriter escribir = new FileWriter(fichero);
    		BufferedWriter bw = new BufferedWriter(escribir);
    		bw.write(texto);
    		bw.close();
    		
    		
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    	return dev;
    	
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
    
    
    public boolean contiene(String texto) {
    	boolean dev = false;
    	if(texto.contains("ID")) {
    		dev = true;
    	}
    	
    	return dev;
    }
}
