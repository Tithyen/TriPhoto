package enumerations;

import java.awt.Font;
import java.awt.Color;

public enum Texte {
	
	TITRE1(new Font("Arial", Font.PLAIN, 18), Couleurs.PANEL_BLANC.getCouleur()),
	TITRE2(new Font("Arial", Font.PLAIN, 18), Couleurs.PANEL_FONCE.getCouleur()),
	TITRE3(new Font("Arial", Font.PLAIN, 18), Color.BLACK),
	VERSION(new Font("Arial", Font.PLAIN, 16), Color.WHITE);
	
	Font font;
	Color couleur;
	
	/**
	 * constructeur de l'énumération Couleurs
	 * @param pCouleur de type Color
	 */
	Texte(Font argFont, Color argCouleur){
		this.font = argFont;
		this.couleur = argCouleur;
	}
	
	/**
	 * Renvoie la font de l'énumeration
	 * @return de type Font
	 */
	public Font getFont() {
		return this.font;
	}
	/**
	 * Renvoie la couelur de l'énumeration
	 * @return de type Font
	 */
	public Color getCouleur() {
		return this.couleur;
	}
}
