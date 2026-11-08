package es.studium;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Buscador {

	public static ArrayList<File> buscar(String ruta, String extension, ArrayList<File> seleccion){

		if (ruta.trim().isEmpty() || extension.trim().isEmpty()) {
			
			String mensaje = "";
			if (ruta.trim().isEmpty()) 		{ mensaje += "RUTA: Añada una ruta \n"; }
			if (extension.trim().isEmpty()) { mensaje += "EXTENSIÓN: Añada una extensión"; }
			Ventana.setMensaje(mensaje);
		    return seleccion;
		}
		
		Path dir = Paths.get(ruta);
		File  directorio = dir.toFile();
		if (!directorio.exists() || !directorio.isDirectory()) {
		    Ventana.setMensaje("RUTA: no existe o no es un directorio");
		    return seleccion;
		}
		File[] archivos = directorio.listFiles();

//		Abortar
		if (archivos == null) {
			System.out.println("No se puede acceder al directorio");
			return seleccion;
		}


		for (int i = 0; i < archivos.length; i++) {
			
//			OTRO DIRECTORIO
			if (archivos[i].isDirectory()) {
				buscar(archivos[i].getAbsolutePath(), extension, seleccion);
			} else 
			
//			EJECUTABLE
			if (extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("ejecutable")) {
				if (archivos[i].canExecute()) {
//					Se guardan los seleccionados
					seleccion.add(archivos[i]);
				}
			} 	
			
//			EXTENSION
			else {
//				buscamos ya por la extensión que toque
				if (tieneExtension(archivos[i], extension)) {
					seleccion.add(archivos[i]);
				}
			}
		}

		return seleccion;
	}
	
	public static Boolean tieneExtension(File archivo, String extension) {
		Boolean tiene = false;
		
		String 	name 	 = archivo.getName();
		Integer posicion = name.lastIndexOf(".");
		
		if (posicion == -1) {
			return tiene;
		}
		
		String 	ext = name.substring(posicion + 1);
		
		if (ext.equalsIgnoreCase(extension)) {
			tiene = true;
		}
		
		return tiene;
	}
}


