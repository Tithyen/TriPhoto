package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JToggleButton;
import enumerations.Couleurs;

/**
 * Classe permettant de créer un bouton on/off de type apple
 * @author thier
 *
 */
@SuppressWarnings("serial")
public class BoutonOnOff extends JToggleButton {

	private int largeur = 70, hauteur = 40;
	Graphics2D g2;
	private static final Color couleurOn = Couleurs.PANEL_DEFAUT.getCouleur(); //couleur = new Color(51, 204, 102)
	private static final Color couleurOff = Couleurs.PANEL_FONCE.getCouleur();
	private static final Color couleurFond = Couleurs.PANEL_CLAIR.getCouleur();
	
	BoutonOnOff (Boolean argBoolean) {
		this.setSelected(argBoolean);
		this.setLayout(null);
		this.setEnabled(false);
		this.setPreferredSize(new Dimension(this.largeur, this.hauteur));
		this.setMinimumSize(new Dimension(this.largeur, this.hauteur));
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);

		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent arg0) {
				if (isSelected()) {
					setSelected(false);
					repaint();
				} else {
					setSelected(true);
					repaint();
				}
			}
			}); 
	}
	
	public void paintComponent(Graphics g) {  
		super.paintComponent(g);
		//on dessine un rectangle aux bords arrondis
		//bords arrondis
		//pour eviter les escalier sur l'arrondi
        g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
        //on dessine un cercle
        if (isSelected()) {
           	g2.setColor(couleurOn);
        	g2.fillRoundRect(0, 0, getWidth(), getHeight(), this.hauteur, this.hauteur);
        	g2.setColor(couleurFond);
        	g2.fillOval(largeur - getHeight(), 0, getHeight(), getHeight());
        } else {
           	g2.setColor(couleurOff);
        	g2.fillRoundRect(0, 0, getWidth(), getHeight(), this.hauteur, this.hauteur);
        	g2.setColor(couleurFond);
        	g2.fillOval(0, 0, getHeight(), getHeight());
        }
	}
}

	

