package tri;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import tri.Message.Level;
import utils.DateHeure;

public class Log {

	private static File fichierLog = new File("");
	private static StandardOpenOption modeEcriture = StandardOpenOption.APPEND; // valeur par défaut

	/**
	 * constructeur
	 * 
	 * @throws URISyntaxException
	 */
	public Log() {
		if (!fichierLog.exists()) {
			// definition du cheminLog
			try {
				String cheminDossier = Parametres.getValeur("chemin.log").toString();
				// On s'assure que le chemin se termine bien par "\"
				if (cheminDossier.charAt(cheminDossier.trim().length() - 1) != '\\') {
					cheminDossier += "\\";
				}
				fichierLog = new File(cheminDossier + "triphoto.log.txt");
				// on vérifie que le dossier existe sinon on le créé
				if (!fichierLog.getParentFile().exists()) {
					boolean bool = fichierLog.getParentFile().mkdirs();
					// cas ou on ne peut pas creer le repertoire (reseau inaccessible par ex)
					// on créé le fichier log au niveau du réertoire racine du fichier jar
					if (!bool) {
						try {
							String urlCourante = getClass().getProtectionDomain().getCodeSource().getLocation()
									.toURI().getPath();
							urlCourante = urlCourante.substring(1, urlCourante.length()); // pour enlever le '/' en 
																							//premier caractere
							fichierLog = new File(urlCourante + ".log.txt");
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}	
				}
			} catch (NullPointerException npe) {
				System.out.println("Le chemin du fichier Log n'est pas défini : \n" + npe.getMessage());
			}

			// définition du creatNew
			if (Parametres.getValeur("log.choix.createNew").contains("true")) {
				modeEcriture = StandardOpenOption.TRUNCATE_EXISTING; // APPEND par défaut
			}

			// On inscrit une première ligne blanche pour effacer le log
			// si le parametre est sur true
			this.ajouter("", Message.Level.NORMAL);

			// On place le modeEcriture en Append
			modeEcriture = StandardOpenOption.APPEND;

		}
	}

	public synchronized void ajouter(String pS, Level argLevel) {
		// On enregistre dans le log si le degre de gravite est celui défini
		// dans les paramètres ini
		// récupération du niveau (Debug, Normal...) dans les préférences
		// définition du levelLog
		Level levelGlobal = null;
		for (Level l : Level.values()) {
			if (l.name().contains(Parametres.getValeur("log.level"))) {
				levelGlobal = l;
			}
		}
		if (argLevel.getDegre() <= levelGlobal.getDegre()) {
			// Si le niveau est à Leval.DEBUG, on ajoute l'heure devant
			String s = "";
			if (argLevel == Level.DEBUG) {
				s = DateHeure.getDateHeure(false) + " ";
			}
			s += pS;
			List<String> ls = new ArrayList<String>();
			ls.add(s);
			try {
				Files.write(fichierLog.toPath(), ls, StandardOpenOption.CREATE, modeEcriture);
			} catch (IOException io) {
				System.out.println("Erreur d'écriture dans le fichier log : " + io.getMessage());
			} catch (InvalidPathException ipe) {
				System.out.println("Le fichier log n'a pas été trouvé: \n" + ipe.getMessage());
				System.out.println("Programme arrêté");
				System.exit(0);
			}
		}
	}

	public void clore(Boolean pAfficherEcran) throws IOException {
		try {
			// si pAfficherEcran est à true, alors on fait apparaitre le log à l'écran
			if (pAfficherEcran) {
				ProcessBuilder pb = new ProcessBuilder();
				pb.command("notepad.exe", fichierLog.getAbsolutePath());
				pb.start();
			}
		} catch (IOException io) {
			System.out.println("Erreur d'écriture dans le fichier log : " + io.getMessage());
		}
	}
}
