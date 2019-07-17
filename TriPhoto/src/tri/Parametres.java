package tri;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Permet de lire les paramètres utilisateurs de l'application ;
 * Ces paramètres sont des objets de la classe Preferences enregistrées à la racine
 * du système d'exploitation. A la première utilisation de l'application si le préférences sont
 * vides, elles sont importées depuis un fichier Tri.properties inclus dans le .jar
 * @author thier
 *
 */
public class Parametres {
	
	private  Properties prop = new Properties();	
	private static Preferences pref = Preferences.userRoot();
	
	/**
	 * Constructeur qui lance la récupération des paramètres enregistrés dans la 
	 * base de registre (cf. classe java Preferences) ou, au premier
	 * lancement de l'application dans un fichier e type Properties
	 */
	public Parametres() {
		try {
			String[] cles = pref.keys();
			if (cles == null || cles.length == 0) {
				//il n'y a pas encore de cle/valeur sauvergardées dans les Preferences.
				//On va voir s'il existe un fichier Properties pour en extraire
				//les valeurs par défaut (mises dans prop) qui seront ensuite 
				//sauvegardées dans les préférences
				getParametresFromFichier();
				sauvPreferencesFromProperties();
			}
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(e);
		}
	}
	
	public static void setParametre(String argCle, String argValeur) {
		pref.put(argCle, argValeur);
	}
	
	public static String getValeur(String argCle) {		
		return pref.get(argCle, null);
	}
	
	public static String[] getListeCle() {
		try {
			return pref.keys();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Procédure de vérification de tous les paramètres
	 * @return List<String> listant les parametres (cle) qui posent
	 * probleme
	 */
	public static List<String> ListerCleErreur() {
		List<String> listeProblemes = new ArrayList<String>();
		try {
			// tableau de string dans lequel on indiquera les parametres (cle)
			// qui posent probleme
			String[] cles = pref.keys();
			for (String cle : cles) {
				// Test si la valeur associée à la cle est vide
				if (getValeur(cle) == null || getValeur(cle).isEmpty()) {
					listeProblemes.add(cle);
				}
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
			return listeProblemes;
		}
		return listeProblemes;
	}
	
	public void affiche() {
		String[] cles;
		try {
			cles = pref.keys();
			for (String cle : cles) {
				// Test si la valeur associée à la cle est vide
				System.out.println(cle + " / " + getValeur(cle));
				}
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
		
	
	private void getParametresFromFichier() {		
		//on va chercher les paramètres d'initialisation
		//dans le fichier tri.properties
		//on teste l'existence de ce fichier
		InputStream in = null;
		BufferedReader br = null;
		try {
			in = Process.class.getResourceAsStream("Tri.properties");
			br = new BufferedReader(new InputStreamReader(in));
			prop.load(br);	
			br.close();		
			in.close();	
		} catch	(FileNotFoundException | NullPointerException  ex) {
			System.out.println("le fichier d'initialisation 'tri.properties' n'a pas été trouvé");
			System.out.println("Programme stoppé");
			System.exit(0);
		} catch	(IOException ioe) {
			System.out.println("le fichier d'initialisation 'tri.properties' ne peut pas etre lu !");
			System.out.println("Programme stoppé");
			System.exit(0);
		}
	}
	
	private void sauvPreferencesFromProperties() {
		//parcours de la hastable prop.elements
		//List<String> liste = new ArrayList<String>();
		Set<?> cles = prop.keySet();
		Iterator<?> it = cles.iterator();
		String cle = null;
		String valeur = null;
		while (it.hasNext()){
			//chaque couple cle/valeur du fichier Tri.properties sera
			//sauvegardé dans un couple cle/valeur de Préférence
			//Les cles et valeurs de properties ne peuvent etre que des String
			//contraitement aux couples cle/valeur de Preferences
			cle = (String)it.next();
			valeur = this.getValeurFromProperties(cle);
			//on enregistre ce couple dans les préférences
				pref.put(cle, valeur);
		}
	}
	
	/* une propriete (class java Properties) est composée d'une cle (ex cheminA.dossierATrier) et 
	 * d'une valeur (ex "C:/photo/A_tier ; Dossier des fichiers à trier)
	 * les différents élements d'une valeur sont séparés par un point virgule ;
	 * cela sert notamment à mettre automatiquement un label devant les chemins
	 * dans le panel Settings. GetValeur renvoie la première partie de la valeur ; 
	 * GetLegende renverra la deuxième partie
	 */
	private String getValeurFromProperties(String argCle) {
		String valeur = null;
		if (prop.containsKey(argCle)) {
			try {
				valeur = prop.getProperty(argCle).trim();
			} catch(ArrayIndexOutOfBoundsException e) {
				valeur = null;
			}		
		} else {
			valeur = null;
		}	
		return valeur;
	}
		
	
}
