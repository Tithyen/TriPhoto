import java.awt.Font;
import javax.swing.UIManager;

import enumerations.Couleurs;
import  gui.FenetrePrincipale;
import tri.Log;
import tri.Message;
import tri.Parametres;
import utils.DateHeure;

public class Main {
	
	private static Log log;

	public static void main(String[] args) {

		/* On modifie la police et certaines couleurs pour tous les composants swing
		 *  de l'application */
		try {
		      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		      UIManager.put("Label.font",new Font("Arial",Font.CENTER_BASELINE, 16));
		      UIManager.put("ProgressBar.background", Couleurs.PANEL_FONCE.getCouleur());
		      UIManager.put("ProgressBar.foreground", Couleurs.PANEL_DEFAUT.getCouleur());
		      //UIManager.put("ProgressBar.selectionBackground", Color.RED);
		      //UIManager.put("ProgressBar.selectionForeground", Couleurs.PANEL_DEFAUT.getCouleur());
		}
		catch(Exception e) {
		      e.printStackTrace();
		}
		
		// on instancie la classe initialisation
		new Parametres();
		
		//On instancie la classe Log pour y inscrire une en-tête	
		log = new Log();
		log.ajouter("_______________________________________________________\n\n", Message.Level.NORMAL);
		log.ajouter("TriPhoto du " + DateHeure.getDateHeure(true) + "\n\n", Message.Level.NORMAL);
		log.ajouter("Log en mode " + Parametres.getValeur("log.level") + "\n\n", Message.Level.NORMAL);

		
		/* Si l'application est lancée par ligne de commande avec l'argument : 'lignecommande'
		 * alors l'application se lancera sans GUI ; sert au lancement de tâche de fond à l'ouverture
		 * de windows par exemple
		 */
		if (args.length > 0 && args[0].equals("lignecommande"))
		{
		    log.ajouter("Execution en mode ligne de commande", Message.Level.DEBUG);
			System.out.println("it's ok");
		} else {
			log.ajouter("Execution en mode GUI", Message.Level.DEBUG);
			/* on instancie la fenetre principale de l'application */
			new FenetrePrincipale();
		}	
	}

}
