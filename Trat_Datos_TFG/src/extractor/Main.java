package extractor;

public class Main {
	
	public static void main (String [] args) throws Exception {
		
		System.out.print("Primera prueba para leer fichero xml \n");
		String path = "file:///C:/Users/alexf/Documents/Grado en INGENIERÍA INFORMÁTICA/TFG/licitacionesPerfilesContratanteCompleto3_2012/licitacionesPerfilesContratanteCompleto3.atom";
		ExtractorXml e = new ExtractorXml();
		
		e.abrirDoc(path);
		
	}
}
