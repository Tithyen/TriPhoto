package tri;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.imaging.ImageReadException;

import enumerations.TypeFichier;

/**
 * Classe fichier fille de File permettant d'affecter 
 * des attributs notamment date de creation et type de fichier
 * @author thier
 *
 */
public class Fichier extends File {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String extension;
	private TypeFichier typeFichier;
	private String dateCreation;
	private String nouveauNom;
	private long taille;
	
	/**
	 * constructeur
	 * @param pChemin : chemin du fichier
	 * permet de définir la date de création d'une photo JPEG à partir de la classe
	 * Metadata trouvée sur Internet
	 */
	public Fichier(String pChemin) {
		super(pChemin);
		// TODO Auto-generated constructor stub
		this.taille = this.length();
		this.setDateCreation();
		this.setTypeFichier();
		this.setNouveauNom();
	}

	
	public String getDateCreation() {
		return this.dateCreation;
	}
	
	public TypeFichier getTypeFichier() {
		return this.typeFichier;
	}
	
	public String getNouveauNom() {
		return this.nouveauNom;
	}
	
	public String getExtension() {
		return this.extension;
	}
	
	public String getCheminRacine() {
		String chemin = Parametres.getValeur("chemin." + this.getTypeFichier());
		//On vérifie que le chemin se termine bien par un antislash
		if (chemin.trim().charAt(chemin.length()-1) != '\\') {
			chemin += "\\";
		}
		return chemin;		
	}
	
	public long getTaille() {
		return this.taille;
	}
	
	private void setDateCreation() {
		try {
			//on supprime les "'" de la chaine renvoyée par Metadata
			this.dateCreation = Metadata.RenvoiDateToString(this).replace("'", ""); 
		} catch (ImageReadException | IOException e) {
			// Si on ne peut pas lire l'exif
			e.printStackTrace();
			this.dateCreation="";
		}
	}
	
	private void setTypeFichier() {
		this.extension = this.getPath().substring(this.getPath().lastIndexOf(".")).toLowerCase();

		if (Arrays.toString(TypeFichier.PHOTO.getExtensionOk()).contains(extension)) {
			if (this.dateCreation == "") {
				this.typeFichier = TypeFichier.PHOTO_SANS_EXIF;
			} else {
				this.typeFichier = TypeFichier.PHOTO;
			}	
		} else if (Arrays.toString(TypeFichier.VIDEO.getExtensionOk()).contains(extension)) {
			this.typeFichier = TypeFichier.VIDEO;
		} else {
			this.typeFichier = TypeFichier.INCONNU;
		}
	}
	
	private void setNouveauNom() {
		String s="";
		if (this.dateCreation != "") {
			String[] sp = this.dateCreation.split(" ");
			String[] date = sp[0].split(":");
			String[] time = sp[1].split(":");
			for (int i = 0; i < date.length; i++) {
				s += date[i];				
			}
			s += "_";
			for (int i = 0; i < time.length; i++) {
				s += time[i];
			}
			s += this.extension;
		} else {
			s = this.getName();
		}
		this.nouveauNom = s;
	}
}
