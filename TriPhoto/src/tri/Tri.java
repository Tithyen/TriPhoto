 package tri;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import tri.Message.Type;

import java.io.File;
import java.io.IOException;

/**
 * Classe permettant le tri et le déplacement des fichiers
 * @author thier
 *
 */
public class Tri {
	
	private List<Fichier> listeFichiers;
	private List<File> repertoireCree;
	private List<List<String>> fichierDeplace;
	private Consumer<Message> listener;
	private static Integer progress = 0;
	
	/**
	 * Constructeur
	 * @param pListeFichiers : liste d'objets Fichier à trier
	 */
	public Tri(List<Fichier> pListeFichiers, Consumer<Message> pListener) {
		listener = pListener;
		this.listeFichiers = pListeFichiers;
		this.repertoireCree = new ArrayList<File>();
		this.fichierDeplace = new ArrayList<List<String>>();
		//this.memo = pMemo;
	}

	public void Trier() throws IOException {
		
		File fichierCible = new File("");
		
		for (Fichier fichierSource : this.listeFichiers) {		
			//On récupère le chemin du dossier dans lequel le fichier doit être placé
			//en fonction de son extension, donc du TypeFichier
			switch (fichierSource.getTypeFichier()) {
			case VIDEO: case PHOTO_SANS_EXIF: case INCONNU:
				fichierCible = new File(fichierSource.getCheminRacine()
						+ "/" + fichierSource.getNouveauNom());
				break;
			case PHOTO:
				fichierCible = new File(fichierSource.getCheminRacine() 
						+ fichierSource.getDateCreation().substring(0,4) + "/"
						+ fichierSource.getDateCreation().substring(4,6)
						+ "/" + fichierSource.getNouveauNom());
				break;
			}		
			//on teste si le fichier cible existe
			//s'il existe on vérifie si le fichier source a une taille plus grande.
			//Si oui, on deplacera le fichier source vers le dossier cible
			//et le fichier déjà existant dans le dossier cible ira dans le dossier "doublon"
			if (fichierCible.exists()) {
				//on créé un File "Doublon" pour y placer le fichier en double
				File fichierDoublon = new File(fichierSource.getCheminRacine()
						+ "/" + "Doublons/" + fichierSource.getNouveauNom());
				if (fichierDoublon.exists()) {
					int i = 0;
					while (fichierDoublon.exists()) {
						i++;
						fichierDoublon = new File (fichierSource.getCheminRacine()
								+ "/" + "Doublons/" 
								+ fichierSource.getNouveauNom().replace(fichierSource.getExtension(), "")
								+ "_" + i + fichierSource.getExtension());
					}
				}
				if (fichierSource.length() > fichierCible.length()) {
					//on teste si le fichierDoublon existe.
					//si oui, on incrémentera le nom du fichier doublon
					this.deplacement(fichierCible, fichierDoublon);
					this.deplacement(fichierSource, fichierCible);
				} else {
					this.deplacement(fichierSource, fichierDoublon);
				}
			} else {
				this.deplacement(fichierSource, fichierCible);
			}
		}	
	}

	private void deplacement(File pSource, File pCible) {
		//test de l'existence du dossier cible
		if (!pCible.getParentFile().exists()) {
			pCible.getParentFile().mkdirs();
			this.repertoireCree.add(pCible.getParentFile());
			listener.accept(new Message("Création répertoire : " + pCible.getParentFile() + "\n",
					Type.Memo, Message.Level.NORMAL));
		}
		//on deplace le fichier
		//si le fichier cible existe déjà, on lève une exception
		try {
			Files.move(pSource.toPath(), pCible.toPath());
			// on envoie l'info du traitement à l'ecouteur pour affichage eccran et log
			listener.accept(new Message("Déplacement : " + pSource.getAbsolutePath() 
							+ " --> " + pCible.getAbsolutePath() + "\n",
							Type.Memo, Message.Level.NORMAL));
			//pour la JpRogressBar
			listener.accept(new Message(progress++, Type.Progression, null));
			Thread.sleep(6);
			
		} catch (FileAlreadyExistsException fae) {
			listener.accept(new Message(pSource.length() + "/" + pCible.length(), Type.Memo, Message.Level.DEBUG));
			listener.accept(new Message("Le fichier ne peut être copié "
					+ "car il existe déjà dans le dossier de destination.\n"
					+ fae.getMessage(),  Type.Memo, Message.Level.DEBUG));
		} catch (Exception e) {
			listener.accept(new Message(pSource.length() + "/" + pCible.length(), Type.Memo, Message.Level.DEBUG));
			listener.accept(new Message("Le fichier ne peut être copié.\n"
					+ e.getMessage(), Type.Memo, Message.Level.DEBUG));
		}
	}
	
	public List<File> GetRepertoireCree(){
		return this.repertoireCree;
	}
	
	public List<List<String>> GetFichierDeplace(){
		return this.fichierDeplace;
	}
	
}
