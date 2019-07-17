package enumerations;

import java.awt.Color;

public enum Couleurs {
	
	MENU_DEFAUT(Color.DARK_GRAY),
	MENU_SURVOLE(Color.GRAY),
	MENU_SELECTIONNE(new Color(71, 116, 176)),
	MENU_TEXTE(Color.WHITE),
	MENU_BORDURE(Color.LIGHT_GRAY),
	PANEL_DEFAUT(new Color(71, 116, 176)),
	PANEL_FONCE(Color.DARK_GRAY),
	PANEL_CLAIR(Color.LIGHT_GRAY),
	PANEL_BLANC(Color.WHITE),
	ACTION_DEFAUT(new Color(71, 116, 176)),
	ACTION_SURVOLE(new Color(114, 167, 236)),
	ACTION_CLIQUE(Color.LIGHT_GRAY);
	
	Color couleur;
	
	/**
	 * constructeur de l'énumération Couleurs
	 * @param pCouleur de type Color
	 */
	
	Couleurs(Color pCouleur){
		this.couleur = pCouleur;
	}
	
	/**
	 * Renvoie la couleur de l'énumération
	 * @return de type Color
	 */
	public Color getCouleur() {
		return this.couleur;
	}
	
}
