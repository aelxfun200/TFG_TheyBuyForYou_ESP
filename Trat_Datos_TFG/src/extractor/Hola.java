package extractor;

public class Hola {
	
	public static void main (String [] args) throws Exception {
		
		System.out.print("Primera prueba para leer fichero xml \n");
		String path = "file:///C:/Users/alexf/Documents/Grado en INGENIERÍA INFORMÁTICA/TFG/licitacionesPerfilesContratanteCompleto3_2012/licitacionesPerfilesContratanteCompleto3_20160513_151003_pruebaReducido.atom";
		ExtractorXml e = new ExtractorXml();
		
		e.abrirDoc(path);
		
	}
}
