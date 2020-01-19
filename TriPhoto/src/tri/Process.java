package tri;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import enumerations.TypeFichier;
import tri.Message.Type;
import utils.DateHeure;

/**
 * Classe permettant le lancement des actions de tri
 * @author thier
 *
 */
public class Process {
	
	//ecouteurs qui permet de  renvoyer de l'information au Thread GUI 
	//pendant que ce Thread effectue la tâche de tri ; voir SwingWorker
	private Consumer<Message> listener; //pour renvoyer des mémos
	private Log log;
	
	/**
	 * Constructeur
	 * @param pListener un écouteur qui permet d'envoyer au thread GUI les informations
	 * de déroulement du process
	 */
	public Process(Consumer<Message> pListener)  {	
		this.listener = pListener;
		run();
		}		

	public void run() {
			
			//on instancie un objet log
			log = new Log();
			//on envoi l'info de début de traitement au GUI
			this.listener.accept(new Message(DateHeure.getDateHeure(false) + " DEBUT du tri " +  "\n", 
					Type.Memo, Message.Level.NORMAL));
			//entrée du log pour debug
			log.ajouter("Chemin du dossier de tri : " + Parametres.getValeur("chemin.dossierATrier"),
					Message.Level.DEBUG);	
			
			//On récupere la liste des fichiers du repertoire dossierATrier
			ListerRepertoire lr = new ListerRepertoire(Parametres.getValeur("chemin.dossierATrier"),
					Parametres.getValeur("chemin.choix.sousRepertoire"));
			List<File> lf = lr.lister();
			
			// création des objets Fichier à partir de la liste de File retournée
			List<Fichier> listeFichiers = new ArrayList<Fichier>();
			//on definit le max de la progressBar
			this.listener.accept(new Message(lf.size(), Type.Max, null));
			//on ajouter à la variable listeFichiers les objets Fichiers créés 
			//à partie de lf
			int i = 0;
			for (File f : lf) {
				listeFichiers.add(new Fichier(f.getPath()));
				//on met a jour la progressBar
				listener.accept(new Message(i++, Type.Progression, null));
			}
			
			//on envoie le nombre de fichier au GUI pour la JprogressBar par l'intermédiaire
			//d'un Message
			this.listener.accept(new Message(lf.size(), Type.Max, null));
			this.listener.accept(new Message("Nombre de fichiers à trier : " + lf.size(),
					Type.Memo, Message.Level.NORMAL));
			if (lf.size() != 0) {
				for (TypeFichier t : TypeFichier.values()) {
					this.listener.accept(new Message(
							"    * dont : " + CompteTypeFichier(listeFichiers, t) 
							+ " " + t + "(s)",
							Type.Memo, Message.Level.NORMAL));
				}
				this.listener.accept(new Message("\n", Type.Memo, Message.Level.NORMAL));
			}
			
			//Opération de tri et de déplacement des Fichiers
			Tri t = new Tri(listeFichiers, this.listener);
			try {
				t.Trier();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.listener.accept(new Message("probleme\n", Type.Memo, Message.Level.DEBUG));
			}	
			
			//inscription fin d'opération au log
			this.listener.accept(new Message("\n" + DateHeure.getDateHeure(false) + " FIN du tri",
					Type.Memo, Message.Level.NORMAL));
		}
		
		private static int CompteTypeFichier(List<Fichier> pListe, TypeFichier pTypeFichier) {
			int nb = 0;
			for (Fichier f : pListe) {
				if (f.getTypeFichier() == pTypeFichier) nb++;
			}
			return nb;
		}
}
