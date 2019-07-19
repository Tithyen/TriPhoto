package utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class VersionProjet {

	private String version="";
	
	public VersionProjet() {
		try {
			InputStream flux = this.getClass().getResourceAsStream("/ChangeLog.md");
			InputStreamReader lecture = new InputStreamReader(flux);
			BufferedReader buff = new BufferedReader(lecture);
			//on lit la premiere ligne du fichier 
			//le numéro de la version la plus récente doit figurer sur la premiere ligne non vide
			//on s'assure qu'il s'agit bien d'un format de version : ex: [v1.0.0]
			String ligne = buff.readLine();
			if (ligne.contains("[")) {
				//on supprime les crochets
				this.version = ligne.replaceAll("\\]|\\[", "");
			}
			buff.close(); 
		}		
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public String getVersion() {
		return this.version;
	}
	
}
