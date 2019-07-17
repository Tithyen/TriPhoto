package tri;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant de lister tous les fichiers d'un répertoire
 */
public class ListerRepertoire {
	
	private File cheminAParcourir;
	private Boolean sousRepertoire = false;
	private List<File> listeFichiers = new ArrayList<File>();
	
	/**
	 * 
	 * @param pChemin : Chemin du réperoire principal à explorer
	 * @param pSousRepertoire : oui si on veut lister les fichiers des sous répertoires
	 */
	public ListerRepertoire(String pChemin, String pSousRepertoire) {
		if (pChemin == null) {
			//on prend par défaut le chemin d'où est lancée l'application
			try {
				this.cheminAParcourir = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (pChemin.charAt(pChemin.trim().length()-1) != '\\') {
			this.cheminAParcourir = new File(pChemin + "\\");
		} else {
			this.cheminAParcourir = new File(pChemin);
		}
		if (pSousRepertoire.contains("true")) {
			this.sousRepertoire = true; //sinon prend la valeur par défaut : false
		}
	}
	
	/**
	 * Renvoi la liste des fichiers d'un répertoire et de ses sous répertoires
	 * si le parametre sousRepertoire est true
	 * @return une liste d'objets File
	 */
	public List<File> lister() {
		return this.renvoyerListeFichier(this.cheminAParcourir);
	}
	
	
	private List<File> renvoyerListeFichier(File pCheminAParcourir) {	
		File[] files = pCheminAParcourir.listFiles();
		
		if (files != null) {
			for (int i = 0; i< files.length; i++) {
				if (files[i].isDirectory() && this.sousRepertoire) {
					this.renvoyerListeFichier(files[i]);
				} else if (files[i].isFile()) { // il s'agit d'un fichier
					this.listeFichiers.add(files[i]);
				}
			}
		} else {
			Log log = new Log();
			log.ajouter("Il n'y a aucun fichier dans le répertoire " + pCheminAParcourir,
					Message.Level.DEBUG);
		}
		return listeFichiers;
	}
	
}