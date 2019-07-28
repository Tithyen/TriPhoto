package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import enumerations.Couleurs;
import enumerations.Texte;
import tri.Parametres;

@SuppressWarnings("serial")
public final class PanelProprietes extends JPanel {

	//on définit un tableau permettant de déterminer l'ordre d'affichage
	//des propriétés dans le panel
	private static final String[][] ordreParametres = {
			{"chemin.dossierATrier", "Dossier contenant les fichiers à trier"},
			{"chemin.PHOTO", "Dossier des Photos"},
			{"chemin.PHOTO_SANS_EXIF", "Dossier des Photos sans Exif"},
			{"chemin.VIDEO", "Dossier des Vidéos"},
			{"chemin.VIDEO_SANS_EXIF", "Dossier des Vidéos sans Exif"},
			{"chemin.INCONNU", "Dossier des fichiers inconnus"},
			{"chemin.log", "Dossier du Log"},
			{"chemin.choix.sousRepertoire", "Indexer les sous-répertoires"},
			{"log.choix.createNew", "Créer un Log vide"},
			{"log.choix.affiche", "Afficher le Log à la fermeture"},
			{"log.level", "Mode du Log (DEBUG ou NORMAL)"},
			{"extension.PHOTO", "Extensions des fichiers Photo"},
			{"extension.VIDEO", "Extensions des fichiers Vidéo"}
	};
	
	private static final String[][] groupeParametres = {
			{"chemin", " Sélection des chemins par défaut"},
			{"log", " Log"},
			{"extension", " Extensions traitées"}
	};
	
	public PanelProprietes(){
		super();
		creer();
	}


	private void creer() {
		//on définit un gridbaglayout poour placer les composants
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		//Ajout des choix de chemin par défaut
		int x = 0, y = 0;
		
		for (int k = 0 ; k < groupeParametres.length ; k++) {
			
			///////////////////on définit le titre du groupe
			gbc.gridx = x;
			gbc.gridy = y;
			gbc.gridheight = 1;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.anchor = GridBagConstraints.FIRST_LINE_START;
			gbc.insets = new Insets(15, 15, 15, 40);
			Label titre = new Label (groupeParametres[k][1]);
			titre.setBackground(Couleurs.PANEL_FONCE.getCouleur());
			titre.setFont(Texte.TITRE1.getFont());
			titre.setForeground(Texte.TITRE1.getCouleur());
			this.add(titre, gbc);
			
			////// on place les différents composants
			for (int i = 0 ; i < ordreParametres.length ; i++) {
				// on ne s'occupe que des paramètres ayant pour cle groupeParametres[k][0]
				if (ordreParametres[i][0].startsWith(groupeParametres[k][0])) {
					
					//// ajout du label ////
					y++;
					gbc.gridx = x;
					gbc.gridy = y;
					gbc.gridheight = 1;
					gbc.gridwidth = 1;
					gbc.weightx = 0.;
					gbc.insets = new Insets(8, 15, 0, 0);
					gbc.fill = GridBagConstraints.NONE;
					gbc.anchor = GridBagConstraints.LINE_END;
					Label label = new Label (ordreParametres[i][1]);
					label.setFont(Texte.TITRE1.getFont());
					this.add(label, gbc);
					
					//// ajout des autres composants en ligne ////
					if (ordreParametres[i][0].contains("choix")) {
						// on affiche uniquement un bouton on off
						x++;
						gbc.gridx = x;
						gbc.gridy = y;
						gbc.gridwidth = GridBagConstraints.REMAINDER;
						gbc.insets = new Insets(8, 0, 0, 40);
						gbc.weightx = 0.;
						gbc.fill = GridBagConstraints.NONE;
						gbc.anchor = GridBagConstraints.LINE_START;
						BoutonOnOff bouton = new BoutonOnOff(Parametres.getValeur(
								ordreParametres[i][0]).contains("true"));
						//On ajoute un évenement sur  pour sauvegarder le changement
						//de la propriété
						String s = ordreParametres[i][0];
						bouton.addChangeListener(new ChangeListener() {
							@Override
							public void stateChanged(ChangeEvent ce) {
								String sel = "false";
								if (bouton.isSelected()) {
									sel = "true";
								}
								Parametres.setParametre(s, sel);
							}
						});
						this.add(bouton, gbc);
					} else if (!ordreParametres[i][0].contains("chemin")) {
						// on affiche uniquement un TextField
						x++;
						gbc.gridx = x;
						gbc.gridy = y;
						gbc.gridwidth = 2;
						gbc.weightx = 1.;
						gbc.gridwidth = GridBagConstraints.REMAINDER;
						gbc.insets = new Insets(8, 0, 0, 40);
						gbc.fill = GridBagConstraints.HORIZONTAL;
						TextField texteField = new TextField(Parametres.getValeur(
								ordreParametres[i][0]));
						texteField.setFont(Texte.TITRE1.getFont());
						//On ajoute un évenement sur le Jtexfield pour sauvegarder le changement
						//de la propriété
						String s = ordreParametres[i][0];
						texteField.addTextListener(new TextListener() {
							@Override
							public void textValueChanged(TextEvent arg0) {
								Parametres.setParametre(s, texteField.getText());
							}
						});

						this.add(texteField, gbc);
					} else {
						// on affiche un TextField et un bouton de choix de chemin
						//// ajout du textfield ////
						x++; 
						gbc.gridx = x;
						gbc.gridy = y;
						gbc.gridheight = 1;
						gbc.gridwidth = 1;
						gbc.weightx = 1.;
						gbc.insets = new Insets(8, 0, 0, 20);
						gbc.fill = GridBagConstraints.HORIZONTAL;
						TextField texteField = new TextField(
								Parametres.getValeur(ordreParametres[i][0]));
						texteField.setFont(Texte.TITRE1.getFont());
						String s = ordreParametres[i][0];
						texteField.addTextListener(new TextListener() {
							@Override
							public void textValueChanged(TextEvent arg0) {
								Parametres.setParametre(s, texteField.getText());
							}
						});
						
						this.add(texteField, gbc);
						
						//// ajout d'un bouton de choix de chemin ////
						x++;
						gbc.gridx = x;
						gbc.gridy = y;
						gbc.weightx = 0.;
						gbc.gridwidth = GridBagConstraints.REMAINDER;
						gbc.fill = GridBagConstraints.NONE;
						gbc.insets = new Insets(8, 0, 0, 40);
						Bouton bChoix = new Bouton("...", 60, 30);
						bChoix.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent me) {
								evenementChoixDossier(bChoix, texteField);
							}
						});
						this.add(bChoix, gbc);
					}
				}
				x = 0;
				y++;
			}	

		}
		// on ajote un panel vide pour que le reste du formulaire reste en haut de la page
		//en cas de redimensionnement de la fenêtre
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.weightx = 1.;
		gbc.weighty = 1.;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(new JPanel(), gbc);
	}
		
		
	private void evenementChoixDossier(Bouton argBouton, TextField argTextField) {
		//création dun nouveau filechosser
		JFileChooser chooser = new JFileChooser();
		//on impose la sélection de dossiers uniquement
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//intitulé du bouton
		chooser.setApproveButtonText("Choix du dossier..."); 
		//affiche la boite de dialogue
		//chooser.showOpenDialog(null); 
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {	
			//si un fichier est selectionné, récupérer le fichier puis 
			//son path et l'afficher dans le champs de texte
			argTextField.setText(chooser.getSelectedFile().getAbsolutePath()); 
		}
	}
	
}
