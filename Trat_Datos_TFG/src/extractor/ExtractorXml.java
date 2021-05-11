package extractor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

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
	String f_p_1 = "";
	String f_d_1 = "";
	String temporal = "";
	int contad;
	Document doc = null;

    public void abrirDoc(String fichero) {

    	try {
    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    		doc = dBuilder.parse(fichero);
    		doc.getDocumentElement().normalize();	
    		nList = doc.getElementsByTagName("entry");
    		
    		for(int j = 0; j < nList.getLength(); j++) {
    			Node nodoResponsable = nList.item(j);
    			num = j;
    			setAtributos(nodoResponsable.getNodeName());
    			name = "";
    			
	    		if(nodoResponsable.getNodeType() == Node.ELEMENT_NODE) {	    			
	    			
						Node hijos = nodoResponsable.getFirstChild().getNextSibling();
						
						if(hijos != null && hijos.getNodeType() == Node.ELEMENT_NODE) {
							while(hijos.getNextSibling() != null) {
								int cont = 0;
								if(hijos.getNodeType() == Node.ELEMENT_NODE) {
									System.out.println( "+ " + hijos.getNodeName());
									setEtiqueta(replaceString(hijos.getNodeName()));
									if(hijos.getNodeName() == "id") {
										f_p = "";
										f_d = "";
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
							f = f.replace("null, ", "");
							f = f.replace("\n, ", "");
							addFichero(f, nodoResponsable.getNodeName() + "_1", ".csv"); 
							f = "null";
							cols = "null";
						}
						
		    			nodoResponsable = nodoResponsable.getNextSibling();
		    			
		    			String[] atributo = getAtributos().split(",");
		    			ArrayList<String> tt = new ArrayList<String>();
		    			
						for(int i = 0; i < atributo.length; i++) {

							if(!tt.contains(atributo[i])) {
								tt.add(atributo[i]);
							}
						}	
						
		    			System.out.println("los ficheros son: " + tt.toString());
		    			for (int i = 1; i< tt.size(); i++) {
		    				String leer = leerFichero(tt.get(i).toString().substring(1) + "_1", ".csv");
		    				addFichero(leer, tt.get(i).toString().substring(1), ".csv");
		    				borrarFichero(tt.get(i).toString().substring(1) + "_1", ".csv");
		    			}
	
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
    				setEtiqueta(replaceString(m.getNodeName()));
 
    			if(contiene(replaceString(m.getNodeName())) == true) { 
					System.out.println("==" + replaceString(m.getNodeName()));
					
					nodoPadre = m.getParentNode();
					int cont = 0;
					do {
						if(!nodoPadre.getParentNode().getNodeName().equals("cac-place-ext:ContractFolderStatus")) {
							nodoPadre = nodoPadre.getParentNode();
						}
						
						System.out.println("El primer contador es: " + contador);
						contador ++;
						System.out.println("el valor del padre es: " + nodoPadre.getNodeName());
						System.out.println("el valor del hijo es: " + m.getNodeName());
						nm = nodoPadre;
					}while(nodoPadre.getParentNode() != null && !nodoPadre.getNodeName().equals("entry") && contador == cont); {
						
					}
					System.out.println(nodoPadre.getNodeName() + "/////////////////////////////////////////////////////////////////");
					if(!nodoPadre.getParentNode().getNodeName().equals("feed")) { 
						setNameAnterior(name);
						System.out.println(getNameAnterior() + ".....................");
						name = replaceString(nodoPadre.getNodeName());
						
					} else {
						setNameAnterior(name);
						System.out.println(getNameAnterior() + ".....................");
    					name ="ContractFolderStatus";		
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
    			if(!existeFichero(name + "_1") ) { 	
    				
	        		if(num > 0 && !getAtributos().contains(name)) {
	        			num = -1;
	        		}
    				
	        		if (num == -1) {
	        			crearFichero("", name + "_1", ".csv");
						f_p_1 = "id" + getEtiqueta();
						f_d_1 = identificador + getEntrada();
		        		//fichero ="id" + getEtiqueta() + "\n" + identificador + getEntrada();
		        		entrada = ""; 
		        		etiqueta = "";
	        		} else {
						crearFichero("", name + "_1", ".csv");
						f_p = "id" + getEtiqueta();
						f_d = identificador + getEntrada();
		        		fichero ="id" + getEtiqueta() + "\n" + identificador + getEntrada();
		        		entrada = ""; 
		        		etiqueta = "";
	        		}
	        		
    			}else if(nm != null && m.getNodeType() == Node.ELEMENT_NODE) { 
    				System.out.println("name = " + name + " nodoPadre = " + replaceString(nm.getNodeName()));
    				System.out.println("Bucle para hacer llamada a devolverPadre");
    				System.out.println("m = " + m.getNodeName() + " nm = " + nm.getNodeName());
    				 				
    				if (num == 0 && name.equals(devolverPadre(m, nm, contador))) {    					
    					f_p = f_p + getEtiqueta();
    					f_d = f_d + getEntrada();
    					entrada = "";
    	        		etiqueta = "";
    	        		fichero = f_p + "\n" + f_d;
    	        		
    				} else if(num > 0 && name.equals(devolverPadre(m, nm, contador))) {   					
    					f_d = f_d + getEntrada();
    					entrada = "";
    	        		etiqueta = "";
    	        		fichero = f_d;  	        		
	    			} else if(num == - 1 && name.equals(devolverPadre(m, nm, contador))) {
	    				f_p_1 = f_p_1 + getEtiqueta();
    					f_d_1 = f_d_1 + getEntrada();
    					entrada = "";
    	        		etiqueta = "";
    	        		fichero = f_p_1 + "\n" + f_d_1;
	    			}
    				
    				if(!devolverPadre(m, nm, contador).equals(replaceString(nm.getNodeName()))) {
    					if(num == -1) {
    						crearFichero(f_p_1 + "\n" + f_d_1 + "\n", name + "_1", ".csv");
    						System.out.println("Se inserta en el fichero: " + name  + "_1" + "***********************************************************\n " + f_p_1 + "\n" + f_d_1 + "\n *********************************************************");
    					}else {
    						crearFichero(fichero + "\n", name + "_1", ".csv");
    						System.out.println("Se inserta en el fichero: " + name  + "_1" + "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n " + f_p + "\n" + f_d + "\n +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    					}
        				
        				if(!getNameAnterior().equals(name) && num == -1 && existeFichero(name) == true) { // SI PONGO NUM A 1 NO SE INSERTAN LOS NOMBRES DE LAS COLUMNAS, BIEN POR LOS FICHEROS QUE FALLAN, MAL POR EL FICHERO NUEVO. SI PONGO NUM A 0 SE INSERTE EL TÍTULO PERO FALLAN LOS FICHEROS PRECENDENTES
        					num = 1;
        					System.out.println("SE HA MODIFICADO EL VALOR DE NUM A 1");
        				}
    					
        				setAtributos(name);
    				} 			
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
    		}
	
    		m = m.getNextSibling();
    	}
    	

    	return getAtributos();
    }
    
    
    public String crearFichero(String texto, String nombre, String extension) {
    	String dev = texto;
    	
    	try {
    		String ruta_fichero = "C:/Users/alexf/Documents/Grado en INGENIERÍA INFORMÁTICA/TFG/"+ nombre + extension;
    		File fichero = new File(ruta_fichero);
    		FileWriter escribir = new FileWriter(fichero, false);
    		BufferedWriter bw = new BufferedWriter(escribir);
    		if(!fichero.exists()) {
    			fichero.createNewFile();
    		}
    		
    		bw.write(texto);
    		bw.close();
    		
    		
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    	return dev;
    	
    }
    
    public void addFichero(String texto, String nombre, String extension) {
    	
    	try {
    		String ruta_fichero = "C:/Users/alexf/Documents/Grado en INGENIERÍA INFORMÁTICA/TFG/"+ nombre + extension;
    		File fichero = new File(ruta_fichero);
    		FileWriter escribir = new FileWriter(fichero, true);
    		BufferedWriter bw = new BufferedWriter(escribir);
    		if(!fichero.exists()) {
    			fichero.createNewFile();
    			
    		}	
    		bw.write(texto);
    		bw.close();
    		
    		
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}

    	
    }
    
    public String leerFichero(String nombre, String extension) {
    	String d = "";
    	try {
    		String ruta_fichero = "C:/Users/alexf/Documents/Grado en INGENIERÍA INFORMÁTICA/TFG/"+ nombre + extension;
    		File fichero = new File(ruta_fichero);
    		
    		FileReader fr = new FileReader(fichero);
    		BufferedReader br = new BufferedReader(fr);
    		String ln;  
    		while((ln= br.readLine()) != null) {
	    		 d =  d + ln + "\n";
	    		System.out.println("se lee del fichero: " + ln);
    		}
    		
    		br.close();
    		
    		
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    	return d;
    	
    }
    
    public void borrarFichero(String nombre, String extension) {
    	if(existeFichero(nombre) == true) {
	    	try {
	    		String ruta_fichero = "C:/Users/alexf/Documents/Grado en INGENIERÍA INFORMÁTICA/TFG/"+ nombre + extension;
	    		File fichero = new File(ruta_fichero);
	    		fichero.delete();
	
	    	} catch (Exception e) {
				// TODO: handle exception
	    		e.printStackTrace();
			}
    	}
	
    }
    
    public boolean existeFichero(String texto) {
    	boolean dev = false;
    	String ruta_fichero = "C:/Users/alexf/Documents/Grado en INGENIERÍA INFORMÁTICA/TFG/"+ texto + ".csv";
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
    
    public String devolverPadre(Node n, Node p, int c) {
    	Node np = null;
    	if (n.getNodeType() == Node.ELEMENT_NODE ) {
    		//System.out.println("Primer bucle");
	     if (!n.getParentNode().getNodeName().equals("entry") && !n.getParentNode().getNodeName().equals("feed")) { 
	    	 //System.out.println("Segundo bucle");
			np = n.getParentNode();
			int cont = 0;
			
			if(p!= null) {
				//System.out.println("Tercer bucle");
				while(!np.getParentNode().getNodeName().equals("entry") && !replaceString(np.getNodeName()).equals(replaceString(p.getNodeName())) && c == cont){
					//System.out.println("While bucle");
				    np = np.getParentNode();		
				}
		     }
	     }	

	    }
	    String rt = replaceString(np.getNodeName());
	    System.out.println("+Se devuelve el padre:" + rt);
	    return rt;
    }
}
