package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Classe de boutons de menu ou d'action.
 * Ces boutons gèrent automatiquement les événements souris overflied avec
 * changement de couleur.
 * Une propriété 'selectionne' (true, false) permet de déterminer si un bouton
 * est selectionné pour gérer des événements overfly différents
 * @author thier
 *
 */
@SuppressWarnings("serial")
public class Bouton extends JPanel {
	
	String legende = ""; //texte affiché sur le bouton
	ImageIcon icon ;
	int largeur = 100, hauteur = 40;
	Color couleurNotEnabled = Color.DARK_GRAY;
	Color couleurFond = new Color(71, 116, 176); //violet
	Color couleurFondSurvole = new Color(114, 167, 236); //violet clair
	Color couleurFondSelectionne = new Color(71, 116, 176);
	Color couleurTexte = Color.white;
	Color couleurTexteSurvole = Color.white;
	Font font = new Font("Arial", Font.PLAIN, 18);
	JPanel panel;
	JLabel label;
	JLabel labelImage;
	Boolean selectionne = false;
	
	/**
	 * constructeur simple sans paramètre
	 */
	public Bouton() {
		super(new GridBagLayout(), true);
		this.creerBouton();
	}
	
	/**
	 * Constructeur permettant de définir le texte affiché sur le bouton
	 * @param argLegende
	 */
	public Bouton(String argLegende) {
		super(new GridBagLayout(), true);
		this.legende = argLegende;
		this.creerBouton();
	}
	
	/**
	 * Constructeur permettant de définir le texte affiché sur le bouton
	 * ainsi que sa hauteur et sa largeur
	 * @param argLegende
	 * @param argLargeur
	 * @param argHauteur
	 */
	public Bouton(String argLegende, int argLargeur, int argHauteur) {
		super(new GridBagLayout(), true);
		this.legende = argLegende;
		this.largeur = argLargeur;
		this.hauteur = argHauteur;
		this.creerBouton();
	}
	
	/**
	 * constructeur permettant de définir le texte du bouton et
	 * une image ; l'image est placée au dessus du texte
	 * @param argLegende
	 * @param argCheminIcone
	 */
	public Bouton(String argLegende, ImageIcon pIcon) {
		super(new GridBagLayout(), true);
		this.legende = argLegende;
		this.icon = pIcon;
		this.creerBouton();
	}	
	
	/**
	 * constructeur permettant de définir le texte du bouton et
	 * une image ; l'image est placée au dessus du texte
	 * On fixe également la hauteur et la largeur du bouton
	 * @param argLegende
	 * @param icon
	 * @param argLargeur
	 * @param argHauteur
	 */
	public Bouton(String argLegende, ImageIcon pIcon, int argLargeur, int argHauteur) {
		super(new GridBagLayout(), true);
		this.legende = argLegende;
		this.icon = pIcon;
		this.largeur = argLargeur;
		this.hauteur = argHauteur;
		this.creerBouton();
	}	
	
	public void setCouleurTexte(Color argColor) {
		this.couleurTexte = argColor;
		label.setForeground(argColor);
	}
	
	public void setCouleurTexteSurvole(Color argCouleur) {
		this.couleurTexteSurvole = argCouleur;
	}
	
	public void setCouleurFond(Color argCouleur) {
		this.couleurFond = argCouleur;
		this.setBackground(argCouleur);
	}
		
	public void setCouleurFondSurvole(Color argCouleur) {
		this.couleurFondSurvole = argCouleur;
	}
	
	public void setCouleurFondSelectionne(Color argCouleur) {
		this.couleurFondSelectionne = argCouleur;
	}
	
	public Color getCouleurFondSelectionne() {
		return this.couleurFondSelectionne;
	}
	
	/**
	 * Place le bouton en état "Sélectionné"
	 * la couleur de fond prend la couleur définie pour
	 * les états Sélectionné et la propriété Booléenne 
	 * selectionne devient true ; voir aussi setNonSelectionne
	 */
	public void setSelectionne(Boolean argVraiFaux) {
		if (argVraiFaux) {
			this.setBackground(this.couleurFondSelectionne);
		} else {
			this.setBackground(this.couleurFond);
		}
		
		this.selectionne = argVraiFaux;
	}
	
	public void setNonSelectionne() {
		this.setBackground(this.couleurFond);
		this.selectionne = false;
	}
	
	public String getLegende() {
		return this.legende;
	}
	
	public Boolean isSelectionne() {
		return this.selectionne;
	}
	
	private void creerBouton() {
		
		//couleur et bordure du JPanel
		this.setEnabled(true);
		this.setBackground(this.couleurFond);
		this.setPreferredSize(new Dimension(this.largeur, this.hauteur));
		this.setMaximumSize(new Dimension(this.largeur, this.hauteur));
		
		//création d'un Jlabel
		label = new JLabel(this.legende);
		label.setForeground(this.couleurTexte);
		
		//création d'une icone si un chemin icône est indiqué dans le 
		//constructeur
		if (this.icon != null) {
			labelImage = new JLabel(this.icon);
		}
		//on créé un GridBagContrainst
		GridBagConstraints gbc = new GridBagConstraints();
		//on place les labels sur le panel
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		
		if (this.icon != null) {
			this.add(labelImage);
			gbc.gridy = 1;
			gbc.insets = new Insets(10, 0, 0, 0);
		}
		this.add(label, gbc);
		
		//on ajoute les listeners pour l'effet au survol de la souris et au clic
		this.addMouseListener(new MouseAdapter() { 
	          public void mouseEntered(MouseEvent me) { 
	        	  if (isEnabled() && !selectionne) {
	        		 setBackground(couleurFondSurvole);
		        	 label.setForeground(couleurTexteSurvole); 
	        	  } 	  
	          } 
	          public void mouseExited(MouseEvent me) { 
	        	  if (isEnabled() && !selectionne) {
	        		 setBackground(couleurFond);
		        	 label.setForeground(couleurTexte);  
		        	  }         	     	  
	          } 
	          public void mouseClicked(MouseEvent me) { 
	        	  //setEnabled(!isEnabled());
	        	  
	          } 
	        }); 
		
		//on ajoute un listener sur le changement de propriete enabled
		this.addPropertyChangeListener("enabled", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
            	if ((boolean) evt.getNewValue()) {
            		setBackground(couleurFond);
            	} else {
            		setBackground(couleurNotEnabled);
            	}
            }
        });	
	}	
	
	
}
