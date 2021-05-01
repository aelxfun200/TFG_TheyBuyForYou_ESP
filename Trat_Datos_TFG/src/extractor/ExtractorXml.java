package extractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ExtractorXml {
	
	private String atributos;
	private String datos;
	private String entrada;
	private String etiqueta;
	private String name; 
	private String nameAnterior;
	String identificador = "";
	String fichero = "";
	String cols = "";
	NodeList nList = null;
	Node nm = null;
	int num = 0;
	String f = "";
	String f_p = "";
	String f_d = "";

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
								
								f = f + getEtiqueta();								
								etiqueta = "";	
								textoXml(hijos,cont);
								cols = cols + getEntrada();
								entrada = "";
								hijos = hijos.getNextSibling();
							}
							if(!existeFichero(nodoResponsable.getNodeName())) {
								f = f + "\n" + cols + "\n";
							} else {
								f = cols + "\n";
							}
							crearFichero(f,nodoResponsable.getNodeName(), ".txt"); 
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
    						System.out.println("el valor del padre es: " + nodoPadre.getNodeName());
    						System.out.println("el valor del hijo es: " + m.getNodeName());
    						nm = nodoPadre;
    					}while(nodoPadre.getParentNode() != null && nodoPadre.getNodeName()!= "entry" && contador == cont); {
    						
    					}
    					System.out.println(nodoPadre.getNodeName() + "/////////////////////////////////////////////////////////////////");
    					if(nodoPadre.getParentNode().getNodeName()!= "feed") {     			    		
    						setNameAnterior(name);
    						name = replaceString(nodoPadre.getNodeName());
    						System.out.println("NOMBRE = " + name);
    						
    					} else {
	    					setNameAnterior(name);
	    					name ="ContractFolderStatus";		
    					} 		
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
    		
    		if(name!= null && !name.isBlank()) {    			
    			if(num == 0 && !existeFichero(name)) {   			
					fichero = "id" + getEtiqueta() + "\n" + identificador + getEntrada() + "\n";
					//crearFichero(fichero, name, ".txt");
					System.out.println("SE INSERTA EN EL FICHERO: " + name + "\n los datos:\n " + fichero + "********************************************************************************************************************");
					f_p = f_p + getEtiqueta();
					f_d = f_d + getEntrada();
					crearFichero(f_p + "\n"+ f_d, name, ".txt");
					//f_p = "";
	        		//f_d = "";
	         		entrada = "";
	        		etiqueta = "";
	        		//name = "";
    			} else if(nodoPadre != null) { 
    				System.out.println("name = " + name + " nodoPadre = " + nm.getNodeName());
    				if (num == 0 && name.equals(replaceString(nodoPadre.getNodeName()))) {
    					System.out.println();
    					f_p = f_p + getEtiqueta();
    					f_d = f_d + getEntrada();
    					crearFichero("id" + f_p + "\n" + identificador + f_d + "\n", name, ".txt");
    					//f_p = "";
    	        		//f_d = "";
    	         		entrada = "";
    	        		etiqueta = "";	  
    	        		//name = "";
    				} else if(num > 0 && name.equals(replaceString(nodoPadre.getNodeName()))) { 		
    					f_d = f_d + getEntrada();
    					crearFichero(identificador + f_d + "\n", name, ".txt");
    					//f_d = "";
    	        		entrada = "";
    	        		etiqueta = "";	
	    			}
    				
    				/*if (!name.equals(replaceString(nodoPadre.getNodeName()))) {
    					f_p = "";
    					f_d = "";
    				}*/
    			   	
    			} else {
    				fichero = identificador + getEntrada() + "\n" ;
    				f_p = f_p + getEtiqueta();
					f_d = f_d + getEntrada();
					crearFichero("id" + f_p + "\n" + identificador + f_d + "\n", name, ".txt");
	        		f_p = "";
	        		f_d = "";
	        		entrada = "";
	        		etiqueta = "";	  
    			}

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
