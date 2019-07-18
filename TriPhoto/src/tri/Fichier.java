package tri;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.prefs.Preferences;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

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
	private static Preferences pref = Preferences.userRoot();
	
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
	
	/*
	 * extrait depuis les exif (grace au package metadat extractor)
	 * la date de la photo et la sauve dans la variable de classe dateCreation
	 * au format yyyMMdd_HHmmss
	 * Si exif inexistant, renvoie date null
	 */
	private void setDateCreation() {
		try {
			this.dateCreation="";
			Metadata metadata = ImageMetadataReader.readMetadata(this);
			// obtain the Exif directory
			ExifSubIFDDirectory directory
			    = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
			// query the tag's value
			if (directory!=null) {
				Date date
			    = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
				//formatage de la date
				//on passe de l'heure CEST renvoyée par metadata en heure GMT
				SimpleDateFormat formater = null;
				formater = new SimpleDateFormat("yyyyMMdd_HHmmss");
				formater.setTimeZone(TimeZone.getTimeZone("GMT"));
				this.dateCreation=formater.format(date);
			}		
		} catch (IOException | ImageProcessingException e) {
			// Si on ne peut pas lire l'exif
			e.printStackTrace();
		}

	}
	
	private void setTypeFichier() {
		this.extension = this.getPath().substring(this.getPath().lastIndexOf("."))
				.toLowerCase();
		String extensionSansPoint = this.extension.replace(".", "");
		if (pref.get("extension." + TypeFichier.PHOTO.name(), null).toLowerCase()
				.contains(extensionSansPoint)) {
			if (this.dateCreation == "") {
				this.typeFichier = TypeFichier.PHOTO_SANS_EXIF;
			} else {
				this.typeFichier = TypeFichier.PHOTO;
			}	
		} else if (pref.get("extension." + TypeFichier.VIDEO, null).toLowerCase()
				.contains(extensionSansPoint)) {
			this.typeFichier = TypeFichier.VIDEO;
		} else {
			this.typeFichier = TypeFichier.INCONNU;
		}
	}
	
	private void setNouveauNom() {
		String s="";
		if (this.dateCreation != "") {
			s = this.dateCreation + this.extension;
		} else {
			s = this.getName();
		}
		this.nouveauNom = s;
	}
	
}
