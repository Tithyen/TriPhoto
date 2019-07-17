package utils;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/*
 * Classe utilitaire donnant la date et l'heure courante entourée
 * de crochets.
 */
public class DateHeure {
	
	private final static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE);
	private final static DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.FRANCE);

	/**
	 * Renvoi la date et/ou l'heure, le tout entre crochets
	 * Si argComplet est true, renvoie la date + l'heure ; 
	 * sinon renvoie uniquement l'heure
	 * @param argComplet
	 * @return String
	 */
	public static String getDateHeure(Boolean argComplet){
		String s = "";
		if (argComplet) {
			s = "[" + dateFormat.format(new Date()) 
			+ " - " + timeFormat.format(new Date())
			+ "]";	
		} else {
			s = "[" + timeFormat.format(new Date()) + "]";	
		}
		return 	s;
	}
}
