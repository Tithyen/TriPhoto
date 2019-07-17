package gui;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import enumerations.Couleurs;
import enumerations.Texte;

import tri.Parametres;
import tri.Message.Level;

@SuppressWarnings("serial")
public class Memo extends JTextArea {
	
	public Memo() {
		TitledBorder tb = BorderFactory.createTitledBorder("Mémo");
		tb.setTitleFont(new Font("Arial",Font.BOLD,18));
		tb.setBorder(new LineBorder(Couleurs.PANEL_FONCE.getCouleur()));
		this.setBorder(BorderFactory.createCompoundBorder(
				tb, BorderFactory.createEmptyBorder(10,10,10,10)));

		this.setBackground(Couleurs.PANEL_CLAIR.getCouleur());
		this.setForeground(Texte.TITRE3.getCouleur());
		this.setFont(new Font("Arial", Font.PLAIN,15));
        this.setEditable(true);  
	}
	
	public void ajouter(String argMessage, Level argLevel) {
		Level levelGlobal = null;
		for (Level l : Level.values()) {
			if (l.name().contains(Parametres.getValeur("log.level"))) {
				levelGlobal = l;
			}
		}
		if (argLevel.getDegre() <= levelGlobal.getDegre()) { 
			this.append(argMessage);
		}
	}
}
