package tfg.fi.upm.es;

import javax.swing.JFileChooser;


public class Main {
	
	public static void main (String [] args) throws Exception {
		
		
		JFileChooser elegirFichero = new JFileChooser();
		elegirFichero.setDialogTitle("Seleccionar fichero de datos");
		elegirFichero.showOpenDialog(elegirFichero);
		
		String path = elegirFichero.getSelectedFile().getAbsolutePath();

		path = path.replace("\"", "/");
		System.out.println(path);
		ExtractorXml e = new ExtractorXml();
		
		e.guardarComo();
		
		if(e.ruta != null || !e.ruta.isEmpty()) {
			e.abrirDoc("file:///" + path);
		}
		
		e.loadConfig();
	}
	
	

}
