package tfg.fi.upm.es;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class Main {
	
	static String file_path = "";

	
	public static void main (String [] args) throws Exception {
		
		ExtractorXml e = new ExtractorXml();
		//System.out.println(file_path + "ruta del fichero");
		getRutaDelFichero();
		e.getRutaCarpeta();
		e.abrirDoc( file_path);
		

	}
	
	
	public static void getRutaDelFichero() throws IOException
    {
        Properties archivoPropertie = new Properties();
        FileInputStream archivo;
        String ruta = "./config.properties"; //Se debe guardar en la misma ruta del JAR
        archivo = new FileInputStream(ruta);
        archivoPropertie.load(archivo);
        archivo.close();
        file_path = archivoPropertie.getProperty("file_path");

    }
	

}
