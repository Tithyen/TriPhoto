package tri;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import java.util.prefs.Preferences;

import com.drew.imaging.FileType;
import com.drew.imaging.FileTypeDetector;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.mov.QuickTimeDirectory;
import com.drew.metadata.mp4.Mp4Directory;

import enumerations.TypeFichier;
import tri.Message.Level;

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
	private FileType[] typePhotoAuto = {FileType.Jpeg, FileType.Cr2};
	private FileType[] typeVideoAuto = {FileType.Flv, FileType.Mov, FileType.Mp4};
	private String extension;
	private TypeFichier typeFichier;
	private String dateCreation = "";
	private String nouveauNom;
	private long taille;
	private static Preferences pref = Preferences.userRoot();
	private Log log = new Log();
	
	/**
	 * constructeur
	 * @param pChemin : chemin du fichier
	 * permet de définir la date de création d'une photo JPEG à partir de la classe
	 * Metadata trouvée sur Internet
	 */
	public Fichier(String pChemin) {
		super(pChemin);
		this.taille = this.length();
		this.setDateCreation();
		this.setTypeFichier();
		this.setNouveauNom();
	}

	/**
	 * Retourne la date de création du Fichier (image ou vidéo)
	 * @return date de création du Fichier au format String
	 */
	public String getDateCreation() {
		return this.dateCreation;
	}
	
	/**
	 * Retourne une constante définissant la nature du Fichier 
	 * (photo, video, photo sans exif, format inconnu...)
	 * @return Objet TypeFichier = constante définissant la nature du fichier
	 */
	public TypeFichier getTypeFichier() {
		return this.typeFichier;
	}
	
	/**
	 * Renvoi le nouveau nom du fichier formatté en fonction de sa date de création
	 * @return le nouveau nom du fichier formatté en fonction de sa date de création
	 */
	public String getNouveauNom() {
		return this.nouveauNom;
	}
	
	/**
	 * Renvoi l'extension du Fichier
	 * @return l'extension du Fichier
	 */
	public String getExtension() {
		return this.extension;
	}
	
	/**
	 * Renvoi le chemin du fichier avant son déplacement
	 * @return le chemin du fichier avant son déplacement
	 */
	public String getCheminRacine() {
		String chemin = Parametres.getValeur("chemin." + this.getTypeFichier());
		//On vérifie que le chemin se termine bien par un antislash
		if (chemin.trim().charAt(chemin.length()-1) != '\\') {
			chemin += "\\";
		}
		return chemin;		
	}
	
	/**
	 * Retourne la taille du fichier
	 * @return la taille du fichier
	 */
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
			Metadata metadata = ImageMetadataReader.readMetadata(this);
			// on recherche les directories du fichier concerné
			for (Directory directory : metadata.getDirectories()) {
				// si on a un Directory de type Mp4 (fonctionne aussi pour fichiers quicktime (mov)
				// on enregistre la date sinon on teste un Directory de type photo
				// si aucun, la date sera de type chaine vide
				if ((directory.getDate(Mp4Directory.TAG_CREATION_TIME)) != null) {
					this.dateCreation = formattageDate(directory.getDate(0x0100));
		        } else if ((directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) != null) {
			    	this.dateCreation = formattageDate(directory.getDate(0x9003));
		        }
			}	
		} catch (IOException | ImageProcessingException e) {
			// Si on ne peut pas lire l'exif
			log.ajouter("Exif du fichier non reconnu", Level.DEBUG);
			e.printStackTrace();
		}
	}
	
	private void setTypeFichier() {
		
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileType fileType = null;
		
		try {
			fis = new FileInputStream(this);
			bis = new BufferedInputStream(fis);
			fileType = FileTypeDetector.detectFileType(bis);

			//On traite d'abord les cas où Metadata reconnait automatiquement un type de fichier (extension)
			//qui fait partie de la liste des extensions photo ou video définies au début de cette classe ;
			//puis on traite les cas où l'extension n'est pas automatiquement reconnue ou ne fait pas partie
			//de liste des extensions définies au début de la classe. Dans ce cas, on va cherhcer dans les paramètres
			//si l'utilisateur a défini des extensions dans l'interface GUI permettant d'identifier le type
			//de fichier.
			if (Arrays.asList(typePhotoAuto).contains(fileType)) { // Metadata a reconnu un fichier Photo
				if (this.dateCreation != "") {
					this.typeFichier = TypeFichier.PHOTO;
				} else {
					this.typeFichier = TypeFichier.PHOTO_SANS_EXIF;
				}
				this.extension = "." + fileType.getCommonExtension();

			} else if (Arrays.asList(typeVideoAuto).contains(fileType)) { //Metadata a reconnu un fichier video
				if (this.dateCreation != "") {
					this.typeFichier = TypeFichier.VIDEO;
				} else {
					this.typeFichier = TypeFichier.VIDEO_SANS_EXIF;
				}
				this.extension = "." + fileType.getCommonExtension();
			} else { //gestion des cas ou Metadata ne reconnait pas un fichier 
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
				} else if (pref.get("extension." + TypeFichier.VIDEO.name(), null).toLowerCase()
						.contains(extensionSansPoint)) {
					if (this.dateCreation == "") {
						this.typeFichier = TypeFichier.VIDEO_SANS_EXIF;
					} else {
						this.typeFichier = TypeFichier.VIDEO;
					}	
				} else {
					this.typeFichier = TypeFichier.INCONNU;
				}
			}
		} catch (FileNotFoundException e) {
			log.ajouter(this.getClass() + " --> Fichier non trouvé", Level.DEBUG);
			e.printStackTrace();
		} catch (Exception e) {
			log.ajouter(this.getClass() + " --> Le traitement a échoué", Level.DEBUG);
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				bis.close();
			} catch (IOException e) {
				log.ajouter(this.getClass() + " --> Impossible de clore le Stream", Level.DEBUG);
				e.printStackTrace();
			}
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
	
	private String formattageDate (Date pDateCEST) {
		//formatage de la date
		//on passe de l'heure CEST renvoyée par metadata en heure GMT
		try {
			 SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd_HHmmss");
			formater.setTimeZone(TimeZone.getTimeZone("GMT"));
			return formater.format(pDateCEST);
		} catch (NullPointerException | IllegalArgumentException e) {
			// Si on ne peut pas lire l'exif
			log.ajouter("Formattage de date impossible", Level.DEBUG);
			e.printStackTrace();
			return "";
		}
			

	}
	
}
