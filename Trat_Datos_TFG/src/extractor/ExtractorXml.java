package extractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
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
	private String name; 
	private String nameAnterior;
	HashMap<String, String> data = new HashMap<String, String>();
	ArrayList<HashMap<String, String>> entradas = new ArrayList<HashMap<String, String>>();
	String identificador = "";
	String fichero = "";
	String cols = "";
	String nm = "";
	Node p = null;
	NodeList nList = null;
	int num = 0;

    public void abrirDoc(String fichero) {

    	try {
    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    		Document doc = dBuilder.parse(fichero);
    		doc.getDocumentElement().normalize();	
    		nList = doc.getElementsByTagName("entry");
    		
    		for(int j = 0; j < nList.getLength(); j++) {
    			Node nodoResponsable = nList.item(j);
    			num = j;
    			
	    		if(nodoResponsable.getNodeType() == Node.ELEMENT_NODE) {	    			
	    			
						Node hijos = nodoResponsable.getFirstChild().getNextSibling();
						
						if(hijos != null && hijos.getNodeType() == Node.ELEMENT_NODE) {
							while(hijos.getNextSibling() != null) {
								int cont = 0;
								if(hijos.getNodeType() == Node.ELEMENT_NODE) {
									System.out.println( "+ " + hijos.getNodeName());
									setEtiqueta(replaceString(hijos.getNodeName()));
									if(hijos.getNodeName() == "id") {
										identificador = hijos.getTextContent().substring(75,81);
										System.out.println(identificador);
									}
								}
								
								textoXml(hijos,cont);
								hijos = hijos.getNextSibling();
							}
						}			    		
		    			nodoResponsable = nodoResponsable.getNextSibling();
	    			}
    		}
	
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}

    }
    
    
    public String textoXml (Node nodo, int contador) {
    	   	
    	Node m = nodo.getFirstChild(); 
    	Node nodoPadre = null;

    	while(m != null) {

    		if (m.getNodeType() == Node.ELEMENT_NODE ) {
    			System.out.println("- " + replaceString(m.getNodeName()));
    			setAtributos(replaceString(m.getNodeName()));
    			if(num == 0) {
    				setEtiqueta(replaceString(m.getNodeName()));
    			} 
    			
    			if(contiene(replaceString(m.getNodeName())) == true) { //
    				if (m.getParentNode().getNodeName() != "entry" && m.getParentNode().getNodeName() != "feed") {  
    					System.out.println("==" + replaceString(m.getNodeName()));
    					
    					nodoPadre = m.getParentNode();
    					int cont = 0;
    					do {
    						if(nodoPadre.getParentNode().getNodeName() != "cac-place-ext:ContractFolderStatus") {
    							nodoPadre = nodoPadre.getParentNode();
    						}
    						
    						System.out.println();
    						System.out.println("El primer contador es: " + contador);
    						contador ++;
    						System.out.println("El valor del contador con el que se sale del bucle es: " + contador);
    						System.out.println("el valor del padre es: " + nodoPadre.getNodeName());
    						nm = replaceString(nodoPadre.getNodeName());
    						p = nodoPadre;
    						System.out.println("el valor del hijo es: " + m.getNodeName());
    						System.out.println("el valor del PADRE es: " + m.getParentNode().getNodeName());
    					}while(nodoPadre.getParentNode() != null && nodoPadre.getNodeName()!= "entry" && contador == cont); {
    						
    					}
    					System.out.println("nm = " + nm + "/////////////////////////////////////////////////////////////////");
    					if(nodoPadre.getParentNode().getNodeName()!= "feed") {     			    		
    						System.out.println("SE INTENTA CREAR EL FICHERO " + replaceString(nodoPadre.getNodeName()));
    						nm = replaceString(nodoPadre.getNodeName());
    						if(existeFichero(replaceString(nodoPadre.getNodeName())) == true) {
    							crearFichero("", replaceString(nodoPadre.getNodeName()), ".csv");
    							cont = contador --;
    						} else {
	    						crearFichero("", replaceString(nodoPadre.getNodeName()), ".csv");
		    					System.out.println("Se entra en el bucle +++++++++++++++++++++++++++++++++++++++++++++++");
		    					
	    					}
    						setNameAnterior(name);
    						name = replaceString(nodoPadre.getNodeName());
    						System.out.println("NOMBRE = " + name);
	    				System.out.println("Se ha insertado en el fichero: " + replaceString(nodoPadre.getNodeName()) + " el dato: " + m.getNodeName() + " " + m.getTextContent() + "------------------------------------");

    					} else {
    						if(existeFichero("ContractFolderStatus") == true) {
    							crearFichero("", "ContractFolderStatus" , ".csv");
    							cont = contador --;
    						} else {
    						crearFichero("", "ContractFolderStatus" , ".csv");
	    					System.out.println("Se entra en el bucle +++++++++++++++++++++++++++++++++++++++++++++++");
    						}	
    					System.out.println("Se ha insertado en el fichero: " + "ContractFolderStatus" + " el dato: " + m.getNodeName() + " " + m.getTextContent() + "------------------------------------");
    					setNameAnterior(name);
    					name ="ContractFolderStatus";		
    					} 
    					
						/*if(existeFichero("entry") == true) {
							crearFichero("", "entry" , ".txt");
							cont = contador --;
						} else {
						crearFichero("", "entry" , ".txt");
    					System.out.println("Se entra en el bucle +++++++++++++++++++++++++++++++++++++++++++++++");
						}	
					System.out.println("Se ha insertado en el fichero: " + "entry" + " el dato: " + m.getNodeName() + " " + m.getTextContent() + "------------------------------------");
					name ="entry";*/
    					
    				}
    			}
    		}
    		
    		if(m.getFirstChild() != null) {	
    			System.out.println("Se invoca a la función textoXml con el valor del contador: " + contador);
    			textoXml(m, contador);	
    		} else if (!m.getTextContent().isBlank()){
    			System.out.println("   " + m.getTextContent());
    			setDatos(m.getTextContent());
    			setEntrada(m.getTextContent());
    			
    		}
    		
    		if(name!= null && !name.isBlank() ) {  //Pos = final   HAY QUE CONSEGUIR QUE SE INSERTEN LAS COLUMNAS EN SU FICHERO
    			// DESDE QUE CONSIGO CREAR UN FICHERO HASTA QUE CREO OTRO, SE MEZCLAN NODOS EN EL FICHERO NUEVO, ¿CÓMO EVITAR ESTO?  ¿GUARDAR LOS NODOS EN UNA VARIABLE TEMPORAL?
    			
    			if(num == 0 ) {   				
					fichero = "id" + getEtiqueta() + "\n" + identificador + getEntrada() + "\n";
					//cols = "id, " + getEtiqueta() + "\n";
					crearFichero(fichero, name, ".csv");
					System.out.println("SE INSERTA EN EL FICHERO: " + name + "\n" + cols + " los datos:\n " + fichero + "********************************************************************************************************************");
					//Node n = m.getParentNode();	
    			} else {
    				fichero = identificador + getEntrada() + "\n" ;
    				crearFichero(fichero, name, ".csv");
    			}
    			entrada= "";
				etiqueta = "";
				name ="";
			}
    		
    		m = m.getNextSibling();
    	}
 
    	return getAtributos();
    }     	
    
    public String textoXmlSinHijos (Node nodo) {
    	
    	Node m = nodo.getFirstChild(); 

    	while(m != null) {
    		if (m.getNodeType() == Node.ELEMENT_NODE) {
    			System.out.println("- " + replaceString(m.getNodeName()));
    			setEtiqueta(replaceString(m.getNodeName()));
    			
    		}

    		if(m.getFirstChild() != null) {	
    			textoXmlSinHijos(m);	
    		} else if (!m.getTextContent().isBlank()){
				System.out.println("   " + m.getTextContent());
    			setAtributos(m.getTextContent());	
    		}
	
    		m = m.getNextSibling();
    	}
    	

    	return getAtributos();
    }
    
    
    public boolean crearFichero(String texto, String nombre, String extension) {
    	boolean dev = false;
    	try {
    		String ruta_fichero = "C:/Users/alexf/Documents/Grado en INGENIERÍA INFORMÁTICA/TFG/"+ nombre + extension;
    		File fichero = new File(ruta_fichero);
    		FileWriter escribir = new FileWriter(fichero, true);
    		BufferedWriter bw = new BufferedWriter(escribir);
    		if(!fichero.exists()) {
    			fichero.createNewFile();
    			dev = true;
    			
    		}
    		
    		bw.write(texto);
    		bw.close();
    		
    		
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    	return dev;
    	
    }
    
    public boolean existeFichero(String texto) {
    	boolean dev = false;
    	String ruta_fichero = "C:/Users/alexf/Documents/Grado en INGENIERÍA INFORMÁTICA/TFG/"+ texto + ".txt";
		File fichero = new File(ruta_fichero);
		if(fichero.exists()) {
			dev = true;
		}
    	return dev;
    }
    
    public void setAtributos (String att) {
    	this.atributos = atributos + ", " + att;
    }
    
    public String getAtributos () {
    	return atributos;
    }
    
    public void setDatos (String dat) {
    	this.datos = datos + ", " + dat ;
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
    
    
    public void setEntrada(String ent) {
    	this.entrada = entrada + ", " + ent;
    }
    
    
    public String getEntrada() {
    	return entrada;
    }
    
    public void setEtiqueta(String etiq) {
    	this.etiqueta = etiqueta + ", " + etiq;
    }
    
    public String getEtiqueta() {
    	return etiqueta;
    }
    
    public void setNombre(String name) {
    	this.name = name;
    }
    
    public String getNombre() {
    	return name;
    }
    
    public void setNameAnterior(String nameAnterior) {
    	this.nameAnterior = nameAnterior;
    }
    
    public String getNameAnterior() {
    	return nameAnterior;
    }
    
    public boolean contiene(String texto) {
    	boolean dev = false;
    	if(texto.contains("ID") || texto.contains("Code")) {
    		dev = true;
    	}
    	
    	return dev;
    }
}
