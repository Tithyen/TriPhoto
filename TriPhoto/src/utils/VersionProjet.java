package utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant de lire le fichier Changelog
 * et d'extraire le numéro de version, ainsi que le détail du changeLog
 * @author Thier Tithyen@gmail.com
 */
public final class VersionProjet {

	private final List<String[]> version = new ArrayList<String[]>();
	
	/**
	 * Constructeur qui lit le fichier Changelog et enregistre les données dans 
	 * la variable version
	 */
	public VersionProjet() {
		super();
		lectureFichierVersion();
	}
	
	// méthode qui lit le contenu du fichier changelog pour en extraire les numéros de versions
	// et le détail des chnagements apportés par chaque version ; éléments stockés dans une liste de tableau 2D nommée "version"
	private void lectureFichierVersion() {
		try {
			//on lit le fichier ChangeLog et on enregistre toutes les lignes 
			//dans une variable version
			InputStream flux = this.getClass().getResourceAsStream("/ChangeLog.md");
			InputStreamReader lecture = new InputStreamReader(flux);
			BufferedReader buff = new BufferedReader(lecture);
			String l = "";
			String num = ""; //variable qui permet de stocker les numéros de version.
			String detail = ""; //variable qui sert à stocker le détail des modifications.
			
			while ((l = buff.readLine()) != null) {
				if (l.startsWith("[")) {
					if (num != "") {
						String[] tab = {num, detail};
						this.version.add(tab);
						num = "";
						detail = "";
					}
					//on supprime les crochets
					num = l.replaceAll("\\]|\\[", "") + "\n";
				} else {
					detail += l + "\n";
				}	
			}
			buff.close(); 
		}		
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	/**
	 * Retourne le numéro de la dernière version de l'application
	 * @return une chaine texte le numéro de la version la plus récente de l'application au format "[1.1.1]"
	 */
	public final String getLastVersionNumber() {
		return this.version.get(0)[0];
	}

	/**
	 * Retourne le détail des modifications de la dernière version de l'application
	 * @return une chaine texte comportant le détail des modifications de la dernière version de l'application
	 */
	public final String getLastVersionDetail() {
		return this.version.get(0)[1];
	}
	
	/**
	 * Retourne dans une chaine l'ensemble des versions et du détail des modifications de l'application
	 * @return une chaine texte avec l'ensemble des versions et du détail des modifications de l'application
	 */
	public final String toString() {
		String result = "";
		for (String[] s : this.version) {	
			result += s[0] + " " + s[1];
		}
		return result;
	}
}
